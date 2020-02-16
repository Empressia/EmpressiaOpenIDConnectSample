package jp.empressia.app.empressia_oidc_sample_01.security;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.empressia.app.empressia_oidc.db.Token;
import jp.empressia.app.empressia_oidc.db.User;
import jp.empressia.app.empressia_oidc.db.UserAuthentication;
import jp.empressia.app.empressia_oidc.db.accessor.UserAccessor;
import jp.empressia.enterprise.security.oidc.IOpenIDConnectIdentityStore;
import jp.empressia.enterprise.security.oidc.OpenIDConnectCredential;
import jp.empressia.enterprise.security.oidc.OpenIDConnectRememberMeIdentityStore;
import jp.empressia.enterprise.security.oidc.OpenIDConnectIdentityStore.OpenIDConnectPrincipal;

/**
 * アプリケーションでのOpenID Connect対応のRememberMeIdentityStoreです。
 * @author すふぃあ
 */
@ApplicationScoped
public class ApplicationRememberMeIdentityStore extends OpenIDConnectRememberMeIdentityStore {

	private UserAccessor UserAccessor;

	@Inject
	public ApplicationRememberMeIdentityStore(TokenCache TokenCache, IOpenIDConnectIdentityStore IdentityStore, UserAccessor UserAccessor) {
		super(TokenCache, IdentityStore);
		this.UserAccessor = UserAccessor;
	}

	@Override
	protected String generateLoginTokenInternal(OpenIDConnectPrincipal principal, Set<String> groups) {
		OpenIDConnectCredential credential = principal.getCredential();
		String issuer = credential.getIssuer();
		String subject = credential.getSubject();
		String id_token = credential.getIDToken();
		User user = new User();
		user.setIssuer(issuer);
		user.setSubject(subject);
		UserAuthentication authentication = new UserAuthentication();
		authentication.setUser(user);
		authentication.setIDToken(id_token);
		Token token = this.UserAccessor.findToken(authentication);
		return token.getID();
	}

}
