package jp.empressia.app.empressia_oidc.db;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * このアプリケーションでのユーザーの認証を表現します。
 * @author すふぃあ
 */
@Entity
@IdClass(UserAuthentication.PK.class)
@SuppressWarnings("serial")
public class UserAuthentication implements Serializable {

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

	/** IDToken。 */
	@Id
	private String IDToken;
	/** IDToken。 */
	public String getIDToken() { return this.IDToken; }
	/** IDToken。 */
	public void setIDToken(String IDToken) { this.IDToken = IDToken; }

	/** ExpirationTime。 */
	private long ExpirationTime;
	/** ExpirationTime。 */
	public long getExpirationTime() { return this.ExpirationTime; }
	/** ExpirationTime。 */
	public void setExpirationTime(long ExpirationTime) { this.ExpirationTime = ExpirationTime; }

	/** IssuedAt。 */
	private long IssuedAt;
	/** IssuedAt。 */
	public long getIssuedAt() { return this.IssuedAt; }
	/** IssuedAt。 */
	public void setIssuedAt(long IssuedAt) { this.IssuedAt = IssuedAt; }

	/** MailAddress。 */
	private String MailAddress;
	/** MailAddress。 */
	public String getMailAddress() { return this.MailAddress; }
	/** MailAddress。 */
	public void setMailAddress(String MailAddress) { this.MailAddress = MailAddress; }

	/** Name。 */
	private String Name;
	/** Name。 */
	public String getName() { return this.Name; }
	/** Name。 */
	public void setName(String Name) { this.Name = Name; }

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
	 * ユーザーの認証のPrimaryKey。
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

		/** IDToken。 */
		private String IDToken;
		/** IDToken。 */
		public String getIDToken() { return this.IDToken; }
		/** IDToken。 */
		public void setIDToken(String IDToken) { this.IDToken = IDToken; }

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
			result = prime * result + Objects.hashCode(this.IDToken);
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
			if(Objects.equals(this.IDToken, other.IDToken) == false) { return false; }
			return true;
		}

	}

	/** 引数の主キー以外を適用します。 */
	public void merge(UserAuthentication other) {
		this.ExpirationTime = other.ExpirationTime;
		this.IssuedAt = other.IssuedAt;
		this.MailAddress = other.MailAddress;
		this.Name = other.Name;
	}

}
