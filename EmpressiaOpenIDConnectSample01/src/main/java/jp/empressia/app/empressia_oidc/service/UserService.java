package jp.empressia.app.empressia_oidc.service;

import java.time.LocalDateTime;

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

	/** トークンを再発行する間隔です。これを経過したら適当なときに再発行します。 */
	private int TokenReissueInterval;

	@Inject
	public UserService(
		UserAccessor UserAccessor,
		@ConfigProperty(name="jp.empressia.app.empressia_oidc.service.UserService.TokenReissueInterval") int TokenReissueInterval
	) {
		this.UserAccessor = UserAccessor;
		this.TokenReissueInterval = TokenReissueInterval;
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
	 * トークンを登録、ないしは更新します。
	 * 古い認証と認可は、新しいものが指定された場合は、削除されます。
	 * userは、更新時にはnullにできます。
	 */
	@Transactional
	public void mergeTokenWith(String ID, String scope, LocalDateTime createdAt, User user, UserAuthentication authentication, UserAuthorization authorization) {
		Token oldToken = this.UserAccessor.findTokenWithFetchAll(ID);
		UserAuthentication oldAuthentication = (oldToken != null) ? oldToken.getUserAuthentication() : null;
		UserAuthorization oldAuthorization = (oldToken != null) ? oldToken.getUserAuthorization() : null;
		if(authentication != null) {
			authentication.setUser((oldToken != null) ? oldToken.getUser() : user);
			this.UserAccessor.register(authentication);
		}
		if(authorization != null) {
			authorization.setUser((oldToken != null) ? oldToken.getUser() : user);
			this.UserAccessor.register(authorization);
		}
		Token resultToken;
		boolean generateNewToken;
		if(oldToken == null) {
			resultToken = new Token();
			resultToken.setUser(user);
			if(scope == null) { throw new IllegalStateException("新規のトークンいscope指定がありません。"); }
			if(scope != null) { resultToken.setScope(scope); }
			{
				resultToken.setCreatedAt(createdAt);
				generateNewToken = true;
			}
		} else {
			resultToken = oldToken;
			if((user != null) && (oldToken.getUser().equals(user) == false)) {
				throw new IllegalStateException("指定されたユーザーが不整合を起こしています。");
			}
			if(scope != null) { resultToken.setScope(scope); }
			// トークンの期限が切れていたら、新しくする。
			if(oldToken.getCreatedAt().plusSeconds(this.TokenReissueInterval).isBefore(createdAt)) {
				resultToken.setCreatedAt(createdAt);
				generateNewToken = true;
			} else {
				generateNewToken = false;
			}
		}
		if(authentication != null) { resultToken.setUserAuthentication(authentication); }
		if(authorization != null) { resultToken.setUserAuthorization(authorization); }
		// 更新系はすでに管理下にあるからそのままで反映されるはず。
		if(generateNewToken) {
			this.registerInternal(resultToken);
		}
		if((oldToken != null) && (generateNewToken)) {
			this.remove(ID);
		}
		if(authentication != null) {
			if(oldAuthentication != null) {
				this.UserAccessor.remove(oldAuthentication);
			}
		}
		if(authorization != null) {
			if(oldAuthorization != null) {
				this.UserAccessor.remove(oldAuthorization);
			}
		}
	}

}
