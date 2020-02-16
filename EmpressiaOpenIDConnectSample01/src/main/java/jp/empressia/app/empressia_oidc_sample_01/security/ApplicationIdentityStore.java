package jp.empressia.app.empressia_oidc_sample_01.security;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.jsonwebtoken.Claims;
import jp.empressia.app.empressia_oidc.db.Token;
import jp.empressia.app.empressia_oidc.db.User;
import jp.empressia.app.empressia_oidc.db.UserAuthentication;
import jp.empressia.app.empressia_oidc.db.UserAuthorization;
import jp.empressia.app.empressia_oidc.db.accessor.UserAccessor;
import jp.empressia.app.empressia_oidc.service.UserService;
import jp.empressia.enterprise.security.oidc.IOpenIDConnectAuthenticationMechanism;
import jp.empressia.enterprise.security.oidc.OpenIDConnectCredential;
import jp.empressia.enterprise.security.oidc.OpenIDConnectIdentityStore;

/**
 * アプリケーションでのOpenID Connect対応のIdentityStoreです。
 * @author すふぃあ
 */
@ApplicationScoped
public class ApplicationIdentityStore extends OpenIDConnectIdentityStore {

	private UserService UserService;
	private UserAccessor UserAccessor;

	@Inject
	public ApplicationIdentityStore(IOpenIDConnectAuthenticationMechanism Mechanism, UserService UserService, UserAccessor UserAccessor) {
		super(Mechanism);
		this.UserService = UserService;
		this.UserAccessor = UserAccessor;
	}

	@Override
	public OpenIDConnectCredential findCredential(String token) {
		Token t = this.UserAccessor.findTokenWithFetchAll(token);
		if(t == null) {
			return null;
		}
		OpenIDConnectCredential c;
		{
			UserAuthentication authentication = t.getUserAuthentication();
			UserAuthorization authorization = t.getUserAuthorization();
			if(authorization != null) {
				c = new OpenIDConnectCredential(
					authentication.getIssuer(), authentication.getSubject(), authentication.getIDToken(), authentication.getExpirationTime(), authentication.getIssuedAt(),
					authorization.getAccessToken(), authorization.getRefreshToken(), authorization.getExpiresIn(), authorization.getCreatedAt(),
					t.getScope()
				);
			} else {
				c = new OpenIDConnectCredential(
					authentication.getIssuer(), authentication.getSubject(), authentication.getIDToken(), authentication.getExpirationTime(), authentication.getIssuedAt(),
					t.getScope()
				);
			}
		}
		return c;
	}

	/**
	 * 指定されたOpenIDConnectCredentialを登録します。
	 * トークンを同時に発行してストアに登録しておいても良いかもしれません。
	 * ここで発行しておくと、OpenIDConnectRememberMeIdentityStore#generateLoginTokenInternalでは、
	 * それを読み読む形になります。
	 */
	@Override
	protected void registerCredential(OpenIDConnectCredential credential) {
		User user;
		{
			User.PK pk = new User.PK();
			pk.setIssuer(credential.getIssuer());
			pk.setSubject(credential.getSubject());
			user = this.UserAccessor.find(pk);
			if(user == null) {
				user = new User();
				user.setIssuer(credential.getIssuer());
				user.setSubject(credential.getSubject());
				this.UserService.register(user);
			}
		}
		Token token = new Token();
		token.setUser(user);
		token.setScope(credential.getScope());
		token.setCreatedAt(LocalDateTime.now());

		UserAuthentication authentication = new UserAuthentication();
		authentication.setUser(user);
		authentication.setIDToken(credential.getIDToken());
		authentication.setExpirationTime(credential.getExpirationTime());
		authentication.setIssuedAt(credential.getIssuedAt());
		Claims claims = credential.getClaims();
		authentication.setMailAddress(claims.containsKey("email") ? claims.get("email", String.class) : null);
		authentication.setName(claims.containsKey("name") ? claims.get("name", String.class) : null);
		token.setUserAuthentication(authentication);

		if(credential.getAccessToken() != null) {
			UserAuthorization authorization = new UserAuthorization();
			authorization.setUser(user);
			authorization.setAccessToken(credential.getAccessToken());
			authorization.setRefreshToken(credential.getRefreshToken());
			authorization.setExpiresIn(credential.getExpiresIn());
			authorization.setCreatedAt(credential.getCreatedAt());
			token.setUserAuthorization(authorization);
		}
		this.UserService.register(token);
	}

	/**
	 * 指定されたtokenを更新します。
	 * @param credential リフレッシュされた新しい認証認可情報。IDトークンは亡い可能性がある店に注意する。
	 */
	@Override
	protected void updateToken(String token, OpenIDConnectCredential credential) {
		String scope = credential.getScope();
		// id_tokenは必要なければ来ない。
		UserAuthentication authentication;
		Claims id_tokenBody = credential.getClaims();
		if(id_tokenBody != null) {
			authentication = new UserAuthentication();
			String id_token = credential.getIDToken();
			long expirationTime = id_tokenBody.getExpiration().toInstant().toEpochMilli() / 1000;
			long issuedAt = id_tokenBody.getIssuedAt().toInstant().toEpochMilli() / 1000;
			String email = id_tokenBody.get("email", String.class);
			String name = id_tokenBody.get("name", String.class);
			authentication.setIDToken(id_token);
			authentication.setExpirationTime(expirationTime);
			authentication.setIssuedAt(issuedAt);
			authentication.setMailAddress(email);
			authentication.setName(name);
		} else {
			authentication = null;
		}
		// リフレッシュできると言うことは、アクセストークンとリフレッシュトークンはあると言うこと。
		UserAuthorization authorization = new UserAuthorization();
		String access_token = credential.getAccessToken();
		String refresh_token = credential.getRefreshToken();
		int expires_in = credential.getExpiresIn();
		LocalDateTime createdAt = credential.getCreatedAt();
		authorization.setAccessToken(access_token);
		authorization.setRefreshToken(refresh_token);
		authorization.setExpiresIn(expires_in);
		authorization.setCreatedAt(createdAt);
		this.UserService.mergeTokenWith(token, scope, createdAt, null, authentication, authorization);
	}

	/**
	 * 指定されたtokenを無効にします。
	 */
	@Override
	protected void removeToken(String token, OpenIDConnectCredential credential) {
		this.UserService.remove(token);
	}

	/**
	 * 指定されたcredentialgroupsを生成します。
	 * CredentialValidationResultに渡されるgroupsです。
	 * 役割や権限の表現に相当すると思われます。
	 */
	@Override
	protected Set<String> generateGroups(OpenIDConnectCredential credential) {
		LinkedHashSet<String> groups = new LinkedHashSet<String>(Arrays.asList("User"));
		return groups;
	}

}
