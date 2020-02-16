package jp.empressia.app.empressia_oidc.db;

import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author すふぃあ
 */
@DataSourceDefinition(
	name="java:app/TestDataSource",
	className="org.sqlite.SQLiteDataSource",
	url="jdbc:sqlite::memory:"
)
@RequestScoped
public class EntityManagerProducer {

	@PersistenceContext
	private EntityManager EntityManager;

	@Produces
	public EntityManager entityManager() {
		return this.EntityManager;
	}

}
