package jp.empressia.app.empressia_oidc.db;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * このアプリケーションでのユーザーの認可を表現します。
 * OpenID Connect前提なので認証のもとに作成します。
 * @author すふぃあ
 */
@Entity
@IdClass(UserAuthorization.PK.class)
@SuppressWarnings("serial")
public class UserAuthorization implements Serializable {

	/** Issuer。 */
	@Id
	private String Issuer;
	/** Issuer。 */
	public String getIssuer() { return this.Issuer; }
	/** Issuer。 */
	@Deprecated
	public void setIssuer(String Issuer) { this.Issuer = Issuer; }

	/** Subject。 */
	@Id
	private String Subject;
	/** Subject。 */
	public String getSubject() { return this.Subject; }
	/** Subject。 */
	@Deprecated
	public void setSubject(String Subject) { this.Subject = Subject; }

	/** AccessToken。 */
	@Id
	private String AccessToken;
	/** AccessToken。 */
	public String getAccessToken() { return this.AccessToken; }
	/** AccessToken。 */
	public void setAccessToken(String AccessToken) { this.AccessToken = AccessToken; }

	/** RefreshToken。 */
	private String RefreshToken;
	/** RefreshToken。 */
	public String getRefreshToken() { return this.RefreshToken; }
	/** RefreshToken。 */
	public void setRefreshToken(String RefreshToken) { this.RefreshToken = RefreshToken; }

	/** ExpiresIn。 */
	private int ExpiresIn;
	/** ExpiresIn。 */
	public int getExpiresIn() { return this.ExpiresIn; }
	/** ExpiresIn。 */
	public void setExpiresIn(int ExpiresIn) { this.ExpiresIn = ExpiresIn; }

	/** CreatedAt。 */
	private LocalDateTime CreatedAt;
	/** CreatedAt。 */
	public LocalDateTime getCreatedAt() { return this.CreatedAt; }
	/** CreatedAt。 */
	public void setCreatedAt(LocalDateTime CreatedAt) { this.CreatedAt = CreatedAt; }

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

	/**
	 * ユーザーの認可のPrimaryKey。
	 * @author すふぃあ
	 */
	public static class PK implements Serializable {

		/** Issuer。 */
		private String Issuer;
		/** Issuer。 */
		public String getIssuer() { return this.Issuer; }
		/** Issuer。 */
		public void setIssuer(String Issuer) { this.Issuer = Issuer; }

		/** Subject。 */
		private String Subject;
		/** Subject。 */
		public String getSubject() { return this.Subject; }
		/** Subject。 */
		public void setSubject(String Subject) { this.Subject = Subject; }

		/** AccessToken。 */
		private String AccessToken;
		/** AccessToken。 */
		public String getAccessToken() { return this.AccessToken; }
		/** AccessToken。 */
		public void setAccessToken(String AccessToken) { this.AccessToken = AccessToken; }

		/**
		 * 主キーによるハッシュ値を返します。
		 * @return int ハッシュ値
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Objects.hashCode(this.Issuer);
			result = prime * result + Objects.hashCode(this.Subject);
			result = prime * result + Objects.hashCode(this.AccessToken);
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
			if(Objects.equals(this.Issuer, other.Issuer) == false) { return false; }
			if(Objects.equals(this.Subject, other.Subject) == false) { return false; }
			if(Objects.equals(this.AccessToken, other.AccessToken) == false) { return false; }
			return true;
		}

	}

}
