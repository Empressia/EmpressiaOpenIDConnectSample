package jp.empressia.app.empressia_oidc.db;

import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * このアプリケーションでのトークンを表現します。
 * @author すふぃあ
 */
@Entity
@IdClass(Token.PK.class)
@SuppressWarnings("serial")
public class Token implements Serializable {

	/** ID。 */
	@Id
	private String ID;
	/** ID。 */
	public String getID() { return this.ID; }
	/** ID。 */
	public void setID(String ID) { this.ID = ID; }

	/** Issuer。 */
	private String Issuer;
	/** Issuer。 */
	public String getIssuer() { return this.Issuer; }
	/** Issuer。 */
	@Deprecated
	public void setIssuer(String Issuer) { this.Issuer = Issuer; }

	/** Subject。 */
	private String Subject;
	/** Subject。 */
	public String getSubject() { return this.Subject; }
	/** Subject。 */
	@Deprecated
	public void setSubject(String Subject) { this.Subject = Subject; }

	/** Scope。 */
	private String Scope;
	/** Scope。 */
	public String getScope() { return this.Scope; }
	/** Scope。 */
	public void setScope(String Scope) { this.Scope = Scope; }

	/** CreatedAt。 */
	private LocalDateTime CreatedAt;
	/** CreatedAt。 */
	public LocalDateTime getCreatedAt() { return this.CreatedAt; }
	/** CreatedAt。 */
	public void setCreatedAt(LocalDateTime CreatedAt) { this.CreatedAt = CreatedAt; }

	/** IDToken。 */
	private String IDToken;
	/** IDToken。 */
	public String getIDToken() { return this.IDToken; }
	/** IDToken。 */
	public void setIDToken(String IDToken) { this.IDToken = IDToken; }

	/** AccessToken。 */
	private String AccessToken;
	/** AccessToken。 */
	public String getAccessToken() { return this.AccessToken; }
	/** AccessToken。 */
	public void setAccessToken(String AccessToken) { this.AccessToken = AccessToken; }

	/** User。 */
	@ManyToOne
	@JoinColumn(name="Issuer", referencedColumnName="Issuer", insertable=false, updatable=false)
	@JoinColumn(name="Subject", referencedColumnName="Subject", insertable=false, updatable=false)
	private User User;
	/** User。 */
	public User getUser() { return this.User; }
	/** User。 */
	public void setUser(User User) {
		this.User = User;
		this.Issuer = (User != null) ? User.getIssuer() : null;
		this.Subject = (User != null) ? User.getSubject() : null;
	}

	/** UserAuthentication。 */
	@OneToOne
	@JoinColumn(name="Issuer", referencedColumnName="Issuer", insertable=false, updatable=false)
	@JoinColumn(name="Subject", referencedColumnName="Subject", insertable=false, updatable=false)
	@JoinColumn(name="IDToken", referencedColumnName="IDToken", insertable=false, updatable=false)
	private UserAuthentication UserAuthentication;
	/** UserAuthentication。 */
	public UserAuthentication getUserAuthentication() { return this.UserAuthentication; }
	/** UserAuthentication。 */
	public void setUserAuthentication(UserAuthentication UserAuthentication) {
		if(((UserAuthentication != null) && this.Issuer.equals(UserAuthentication.getIssuer())) == false) {
			String message = MessageFormat.format("引数のUserAuthenticationが、主キーIssuerの値に一致していません。", this.Issuer, UserAuthentication.getIssuer());
			throw new IllegalArgumentException(message);
		}
		if(((UserAuthentication != null) && this.Subject.equals(UserAuthentication.getSubject())) == false) {
			String message = MessageFormat.format("引数のUserAuthenticationが、主キーSubjectの値に一致していません。", this.Subject, UserAuthentication.getSubject());
			throw new IllegalArgumentException(message);
		}
		this.UserAuthentication = UserAuthentication;
		this.IDToken = (UserAuthentication != null) ? UserAuthentication.getIDToken() : null;
	}

	/** UserAuthorization。 */
	@OneToOne
	@JoinColumn(name="Issuer", referencedColumnName="Issuer", insertable=false, updatable=false)
	@JoinColumn(name="Subject", referencedColumnName="Subject", insertable=false, updatable=false)
	@JoinColumn(name="AccessToken", referencedColumnName="AccessToken", insertable=false, updatable=false)
	private UserAuthorization UserAuthorization;
	/** UserAuthorization。 */
	public UserAuthorization getUserAuthorization() { return this.UserAuthorization; }
	/** UserAuthorization。 */
	public void setUserAuthorization(UserAuthorization UserAuthorization) {
		if(((UserAuthorization != null) && this.Issuer.equals(UserAuthorization.getIssuer())) == false) {
			String message = MessageFormat.format("引数のUserAuthorizationが、主キーIssuerの値に一致していません。", this.Issuer, UserAuthorization.getIssuer());
			throw new IllegalArgumentException(message);
		}
		if(((UserAuthorization != null) && this.Subject.equals(UserAuthorization.getSubject())) == false) {
			String message = MessageFormat.format("引数のUserAuthorizationが、主キーSubjectの値に一致していません。", this.Subject, UserAuthorization.getSubject());
			throw new IllegalArgumentException(message);
		}
		this.UserAuthorization = UserAuthorization;
		this.AccessToken = (UserAuthorization != null) ? UserAuthorization.getAccessToken() : null;
	}

	/**
	 * ユーザーのトークンのPrimaryKey。
	 * @author すふぃあ
	 */
	public static class PK implements Serializable {

		/** ID */
		private String ID;
		/** ID */
		public String getID() { return this.ID; }
		/** ID */
		public void setID(String ID) { this.ID = ID; }

		/**
		 * 主キーによるハッシュ値を返します。
		 * @return int ハッシュ値
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Objects.hashCode(this.ID);
			return result;
		}

		/**
		 * 主キーによる一致検査を行います。
		 * @param obj 比較対象
		 */
		@Override
		public boolean equals(Object obj) {
			if(this == obj) { return true; }
			if(obj == null) { return false; }
			if(this.getClass() != obj.getClass()) { return false; }
			PK other = (PK)obj;
			if(Objects.equals(this.ID, other.ID) == false) { return false; }
			return true;
		}

	}

}
