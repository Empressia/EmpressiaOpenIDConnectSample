package jp.empressia.app.empressia_oidc.db;

import org.eclipse.persistence.platform.database.DatabasePlatform;

/**
 * @author すふぃあ
 */
@SuppressWarnings("serial")
public class SQLitePlatform extends DatabasePlatform {

	@Override
	public boolean supportsForeignKeyConstraints() {
		return false;
	}

}
