package br.com.shorturl.connection;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnection {

	private static ComboPooledDataSource cpds;
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_CONNECTION = "jdbc:h2:~/test";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";	
	
	public static Connection getConnectionDB() throws SQLException,
			PropertyVetoException {

		if (cpds == null) {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass(DB_DRIVER);
			cpds.setJdbcUrl(DB_CONNECTION);
			cpds.setUser(DB_USER);
			cpds.setPassword(DB_PASSWORD);
			cpds.setMaxStatements(2000);
			cpds.setMinPoolSize(15);			
		}
		return cpds.getConnection();

	}

	public static void closeConnection() {
		if (cpds != null)
			cpds.close();
	}

}
