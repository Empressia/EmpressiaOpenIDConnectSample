package jp.empressia.app.empressia_oidc.db;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * このアプリケーションでのOpenID Connectベースのユーザーを表現します。
 * @author すふぃあ
 */
@Entity
@IdClass(User.PK.class)
@SuppressWarnings("serial")
public class User implements Serializable {

	/** Issuer。 */
	@Id
	private String Issuer;
	/** Issuer。 */
	public String getIssuer() { return this.Issuer; }
	/** Issuer。 */
	public void setIssuer(String Issuer) { this.Issuer = Issuer; }

	/** Subject。 */
	@Id
	private String Subject;
	/** Subject。 */
	public String getSubject() { return this.Subject; }
	/** Subject。 */
	public void setSubject(String Subject) { this.Subject = Subject; }

	/**
	 * ユーザーのPrimaryKey。
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
			return true;
		}

	}

}
