package jp.empressia.app.empressia_oidc.db.accessor;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import jp.empressia.app.empressia_oidc.db.Token;
import jp.empressia.app.empressia_oidc.db.User;
import jp.empressia.app.empressia_oidc.db.UserAuthentication;
import jp.empressia.app.empressia_oidc.db.UserAuthorization;

/**
 * @author すふぃあ
 */
@Dependent
public class UserAccessor {

	@Inject
	private EntityManager em;

	public void register(User user) {
		this.em.persist(user);
	}

	public void register(Token token) {
		this.em.persist(token);
	}

	public void register(UserAuthentication authentication) {
		this.em.persist(authentication);
	}

	public void register(UserAuthorization authorization) {
		this.em.persist(authorization);
	}

	public Token merge(Token token) {
		Token merged = this.em.merge(token);
		return merged;
	}

	public void remove(Token token) {
		this.em.remove(token);
	}

	public void remove(UserAuthentication authentication) {
		this.em.remove(authentication);
	}

	public void remove(UserAuthorization authorization) {
		this.em.remove(authorization);
	}

	/**
	 * 指定されたユーザーを返します。
	 * @return 存在しない場合はnullです。
	 */
	public User find(User.PK pk) {
		User user = this.em.find(User.class, pk);
		return user;
	}

	/**
	 * 指定されたユーザー認証に対応するトークンを返します。
	 * @return 存在しない場合はnullです。
	 */
	public Token findToken(UserAuthentication authentication) {
		var query = this.em.createQuery("SELECT t FROM Token t WHERE t.UserAuthentication = ?1", Token.class);
		query = query.setParameter(1, authentication);
		List<Token> tokens = query.getResultList();
		Token token;
		if(tokens.isEmpty()) {
			token = null;
		} else if(tokens.size() != 1) {
			String message = "トークンが複数の情報に振られてしまっています。";
			throw new IllegalStateException(message);
		} else {
			token = tokens.get(0);
		}
		return token;
	}

	/**
	 * トークンに対応するユーザー情報を得ます。
	 * @params id トークン。
	 * @return トークンに対応するユーザー情報です。存在しない場合はnullです。
	 */
	public Token findTokenWithFetchAll(String id) {
		var query = this.em.createQuery("SELECT t FROM Token t JOIN FETCH t.User JOIN FETCH t.UserAuthentication JOIN FETCH t.UserAuthorization WHERE t.ID = ?1", Token.class);
		query = query.setParameter(1, id);
		List<Token> tokens = query.getResultList();
		Token token;
		if(tokens.isEmpty()) {
			token = null;
		} else if(tokens.size() != 1) {
			String message = "トークンが複数の情報に振られてしまっています。";
			throw new IllegalStateException(message);
		} else {
			token = tokens.get(0);
		}
		return token;
	}

	/**
	 * ユーザーのトークンを返します。
	 * 新しい順に返します。最新が先頭です。
	 */
	public List<Token> queryTokens(User user) {
		var query = this.em.createQuery("SELECT t FROM Token t WHERE t.User = ?1 ORDER BY t.CreatedAt DESC", Token.class);
		query = query.setParameter(1, user);
		List<Token> tokens = query.getResultList();
		return tokens;
	}

	public void flush() {
		this.em.flush();
	}

}
