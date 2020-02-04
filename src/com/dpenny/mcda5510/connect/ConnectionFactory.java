package com.dpenny.mcda5510.connect;

import java.sql.Connection;

public class ConnectionFactory {
	public Connection getConnection(String connectionType) {
		if (connectionType == null) {
			return null;
		}
		if (connectionType.equalsIgnoreCase("mySQLJDBC")) {
			MySQLJDBCConnection dbConnection = new MySQLJDBCConnection();
			return dbConnection.setupConnection();
		}
		return null;
	}

}
