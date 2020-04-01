package jp.empressia.app.empressia_oidc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jp.empressia.app.empressia_oidc.db.Token;
import jp.empressia.app.empressia_oidc.db.User;
import jp.empressia.app.empressia_oidc.db.UserAuthentication;
import jp.empressia.app.empressia_oidc.db.UserAuthorization;
import jp.empressia.app.empressia_oidc.db.accessor.UserAccessor;
import jp.empressia.enterprise.security.oidc.OpenIDConnectUtilities;

/**
 * @author すふぃあ
 */
@Dependent
public class UserService {

	private UserAccessor UserAccessor;

	private int MaxTokenCount = 10;

	@Inject
	public UserService(
		UserAccessor UserAccessor,
		@ConfigProperty(name="jp.empressia.app.empressia_oidc.service.UserService.TokenReissueInterval") int TokenReissueInterval
	) {
		this.UserAccessor = UserAccessor;
	}

	@Transactional
	public void register(User user) {
		this.UserAccessor.register(user);
	}

	/**
	 * 重複しないトークンを発行して登録します。
	 * 引数のIDに発行したトークンを設定して登録したトークンを返します。
	 */
	@Transactional
	public Token register(Token token) {
		UserAuthentication authentication = token.getUserAuthentication();
		UserAuthorization authorization = token.getUserAuthorization();
		if(authentication != null) {
			this.UserAccessor.register(authentication);
		}
		if(authorization != null) {
			this.UserAccessor.register(authorization);
		}
		Token resultToken = this.registerInternal(token);
		return resultToken;
	}

	/**
	 * トークンに新しいIDを設定して登録します。
	 * @param token 対象のトークン
	 * @return 新しくIDが設定されたトークン。
	 */
	private Token registerInternal(Token token) {
		int c = 0;
		while(true) {
			String ID = OpenIDConnectUtilities.generateToken();
			token.setID(ID);
			try {
				this.UserAccessor.register(token);
				// 一定量以上は許可しない。
				List<Token> tokens = this.UserAccessor.queryTokens(token.getUser());
				for(Token oldToken : tokens.stream().skip(this.MaxTokenCount).collect(Collectors.toList())) {
					this.remove(oldToken.getID());
				}
				break;
			} catch(Exception ex) {
				++c;
				if(c >= 5) {
					throw new IllegalStateException("トークンのID登録に失敗しました。");
				}
			}
		}
		return token;
	}

	@Transactional
	public void remove(String id) {
		Token token = this.UserAccessor.findTokenWithFetchAll(id);
		if(token != null) {
			this.UserAccessor.remove(token);
			UserAuthentication authentication = token.getUserAuthentication();
			if(authentication != null) {
				this.UserAccessor.remove(authentication);
			}
			UserAuthorization authorization = token.getUserAuthorization();
			if(authorization != null) {
				this.UserAccessor.remove(authorization);
			}
		}
	}

	/**
	 * トークンに対応する認証と認可を更新します。
	 * 古い認証と認可は、新しいものが指定された場合は、削除されます。
	 * userは、更新時にはnullにできます。
	 */
	@Transactional
	public void mergeTokenWith(String ID, String scope, LocalDateTime createdAt, UserAuthentication authentication, UserAuthorization authorization) {
		Token oldToken = this.UserAccessor.findTokenWithFetchAll(ID);
		if(oldToken == null) { throw new IllegalStateException("トークンの認証と認可を更新しようとしましたが、元のトークンがありませんでした。"); }
		if(authentication != null) {
			UserAuthentication oldAuthentication = oldToken.getUserAuthentication();
			authentication.setUser(oldToken.getUser());
			if(authentication.equals(oldAuthentication)) {
				oldAuthentication.merge(authentication);
			} else {
				this.UserAccessor.register(authentication);
				oldToken.setUserAuthentication(authentication);
				if(oldAuthentication != null) {
					this.UserAccessor.remove(oldAuthentication);
				}
			}
		}
		if(authorization != null) {
			UserAuthorization oldAuthorization = oldToken.getUserAuthorization();
			authorization.setUser(oldToken.getUser());
			if(authorization.equals(oldAuthorization)) {
				oldAuthorization.merge(authorization);
			} else {
				this.UserAccessor.register(authorization);
				oldToken.setUserAuthorization(authorization);
				if(oldAuthorization != null) {
					this.UserAccessor.remove(oldAuthorization);
				}
			}
		}
	}

}
