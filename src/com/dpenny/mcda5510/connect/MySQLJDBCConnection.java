package com.dpenny.mcda5510.connect;
import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLJDBCConnection implements DBConnection{

	public Connection setupConnection()  {

		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/transaction?" + "user=xxxxx&password=xxxxx"
					+ "&useSSL=false"
					+ "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"); 

		} catch (Exception e) {
			System.out.println("Error setting up connection");
			e.printStackTrace();
		} finally {

		}
		return connection;
	}		
	
	
}
