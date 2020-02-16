package jp.empressia.app.empressia_oidc.db;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * このアプリケーションでのユーザーの保有するロールを表現します。
 * @author すふぃあ
 */
@Entity
@IdClass(UserRole.PK.class)
@SuppressWarnings("serial")
public class UserRole implements Serializable {

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

	/** Name。 */
	@Id
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
	 * ユーザーの保有するロールのPrimaryKey。
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
	
		/** Name。 */
		private String Name;
		/** Name。 */
		public String getName() { return this.Name; }
		/** Name。 */
		public void setName(String Name) { this.Name = Name; }
	
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
			result = prime * result + Objects.hashCode(this.Name);
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
			if(Objects.equals(this.Name, other.Name) == false) { return false; }
			return true;
		}

	}

}
