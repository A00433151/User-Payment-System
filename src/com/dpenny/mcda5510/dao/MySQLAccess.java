package com.dpenny.mcda5510.dao;

import java.io.IOException;

/**
 * Original source code from 
 * http://www.vogella.com/tutorials/MySQLJava/article.html
 * 
**/

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;
import java.text.*;

import com.dpenny.mcda5510.entity.Transaction;
import com.dpenny.mcda5510.connect.ConnectionFactory;
import com.dpenny.mcda5510.dao.MySQLAccess;

public class MySQLAccess {
	Scanner sc = new Scanner(System.in);
	Connection connection;
	MySQLAccess dao;
	public Logger logger;
	
	public Logger callLogger() {
		if (logger == null) {
			SimpleFormatter simpleFormatter = null;
			Handler fileHandler = null;

			logger = Logger.getLogger("Main");

			try {
				fileHandler = new FileHandler("./output_logs/crud_logs.log");
				simpleFormatter = new SimpleFormatter();

				logger.addHandler(fileHandler);

				fileHandler.setLevel(Level.FINE);
				logger.setLevel(Level.ALL);
				fileHandler.setFormatter(simpleFormatter);

				logger.config("Configuration done.");

				logger.setLevel(Level.ALL);

			} catch (SecurityException | IOException e) {
				e.printStackTrace();
				logger.log(Level.WARNING, e.getMessage());
			}

			return logger;

		} else
			return logger;

	}
	
	public Connection getConnect() {
		dao = new MySQLAccess();
		try {
			ConnectionFactory connectionFactory = new ConnectionFactory();
			connection = connectionFactory.getConnection("mysqljdbc");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.log(Level.WARNING, e.getMessage());
		}
		return connection;
	}

	public Collection<Transaction> getTransaction(Connection connection, int txId) {

		Statement statement = null;
		ResultSet resultSet = null;
		Collection<Transaction> result1 = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from transaction where ID=" + txId);
			result1 = createResult(resultSet);

			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			
		} finally {
			statement = null;
			resultSet = null;
		}
		return result1;

	}

	public Collection<Transaction> createResult(ResultSet resultSet) throws SQLException {
		Collection<Transaction> result1 = new ArrayList<Transaction>();
		while (resultSet.next()) {
			Transaction trxn = new Transaction();
			trxn.setId(resultSet.getInt("ID"));
			trxn.setNameOnCard(resultSet.getString("nameonCard"));
			trxn.setCardNumber(resultSet.getString("CardNumber"));
			trxn.setUnitPrice(resultSet.getDouble("UnitPrice"));
			trxn.setQuantity(resultSet.getInt("Quantity"));
			trxn.setTotalPrice(resultSet.getDouble("TotalPrice"));
			trxn.setExpDate(resultSet.getString("ExpDate"));
			trxn.setCreatedOn(resultSet.getString("CreatedOn"));
			trxn.setCreatedBy(resultSet.getString("CreatedBy"));
			result1.add(trxn);

		}

		return result1;

	}
	
	public boolean viewResult(int id) {
		boolean idIs = validateId(id);
		if (idIs == false) {
			System.out.println("ID does not exist");
			logger.log(Level.SEVERE, "ID does not exist");
			return false;
		} else {

			try {
				connection = getConnect();
				Collection<Transaction> trxn = getTransaction(connection, id);
				if (!trxn.isEmpty()) {
					for (Transaction transaction : trxn) {

						System.out.println(transaction.toString());
						logger.log(Level.INFO, transaction.toString());
					}
				} else {
					if (connection != null) {
						connection.close();
					}
					return false;
				}
			} catch (Exception e) {
					e.printStackTrace();
					logger.log(Level.WARNING, e.getMessage());
			}

			return true;
		}

	}
	
	public void createResult(int id) {

		Collection<Transaction> transRecords = new ArrayList<Transaction>();
		 
		char option;
		boolean idIs = validateId(id);
		if (idIs == true) {
			System.out.println("ID exists!!! \n Update record with this ID? (Y/y/N/n)");
			logger.log(Level.INFO, "ID exists!!! \\n Update record with this ID? (Y/y/N/n)");
			option = sc.next().charAt(0);
			if ((option == 'Y' || option == 'y'))
				updateResult(id);
		} else {

			Connection connection = null;
			Transaction trans = new Transaction();

			trans.setId(id);
			userInput(transRecords, trans);

			try {
				connection = getConnect();
				boolean createStatus = createTransaction(connection, transRecords);
				if (createStatus == true)
					System.out.println("Record inserted successfully");
				logger.log(Level.INFO, "Record inserted successfully");

			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.WARNING, e.getMessage());

			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	public boolean validateId(int id) {

		try {
			connection = getConnect();
			Collection<Transaction> trxn = getTransaction(connection, id);
			for (Transaction trans : trxn) {
				if (trans.getId() == id)
					return true;
				else
					return false;
			}

		} catch (Exception e) {
				System.out.println(e.getMessage());
				logger.log(Level.WARNING, e.getMessage());
		}
		return false;
	}
	
	public boolean createTransaction(Connection connection, Collection<Transaction> txn) {

		try {

			for (Transaction trxn : txn) {

				String insertSql = "INSERT INTO transaction (ID,nameonCard,CardNumber,UnitPrice,Quantity,TotalPrice,ExpDate,CreatedOn,CreatedBy)"
						+ " VALUES (?,?,?,?,?,?,?,?,?)";

				PreparedStatement prepStatement = connection.prepareStatement(insertSql);
				prepStatement.setInt(1, trxn.getId());
				prepStatement.setString(2, trxn.getNameOnCard());
				prepStatement.setString(3, trxn.getCardNumber());
				prepStatement.setDouble(4, trxn.getUnitPrice());
				prepStatement.setInt(5, trxn.getQuantity());
				prepStatement.setDouble(6, trxn.getTotalPrice());
				prepStatement.setString(7, trxn.getExpDate());

				prepStatement.setString(8, trxn.getCreatedOn());

				prepStatement.setString(9, trxn.getCreatedBy());

				int recordInsert = prepStatement.executeUpdate();

				if (recordInsert > 0) {
					return true;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public void deleteResult(int id) {

		Connection connection = null;
		boolean idStatus = validateId(id);
		if (idStatus == true) {
			try {
				connection = getConnect();
				boolean delStatus = removeTransaction(connection, id);
				if (delStatus == true)
					System.out.println("Record deleted successfully");
				logger.log(Level.INFO, "Record deleted successfully");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
						logger.log(Level.WARNING, e.getMessage());
					}
				}
			}

		} else
			 System.out.println("Delete Failed! ID doesn't exist");
		logger.log(Level.INFO, "Delete Failed! ID doesn't exist");

	}

	public boolean removeTransaction(Connection connection, int id) {

		try {

			String deleteSql = "DELETE FROM transaction WHERE ID=?";

			PreparedStatement prepStatement = connection.prepareStatement(deleteSql);
			prepStatement.setInt(1, id);

			int recordDelete = prepStatement.executeUpdate();

			if (recordDelete > 0) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	public boolean updateResult(int id) {

		Collection<Transaction> transRecords = new ArrayList<Transaction>();
		 

		char option;
		boolean idStatus = validateId(id);
		if (idStatus == false) {
				System.out.println("ID doesn't exist!! \n Create new transaction with ID? (Y/N)");
				logger.log(Level.INFO, "ID doesn't exist!! \\n Create new transaction with ID? (Y/N)");
				option = sc.next().charAt(0);
				if ((option == 'y' || option == 'Y'))
				createResult(id);
		} else {

			Connection connection = null;
			Transaction trans = new Transaction();
			trans.setId(id);
			userInput(transRecords, trans);

			try {
				connection = getConnect();
				boolean updateStatus = updateTransaction(connection, transRecords);
				if (updateStatus == true) {
					System.out.println("Record updated successfully");
					logger.log(Level.INFO, "Record updated successfully");
					return true;
				}
			} catch (Exception e) {
						e.printStackTrace();
						logger.log(Level.WARNING, e.getMessage());
			} finally {

				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
								e.printStackTrace();
					}

				}

			}

		}
		return false;

	}

	public boolean updateTransaction(Connection connection, Collection<Transaction> txn) {

		try {
			for (Transaction trxn : txn) {
				String updatesql = "UPDATE transaction SET nameonCard=?,CardNumber=?,UnitPrice=?,Quantity=?,TotalPrice=?,"
						+ "ExpDate=?,CreatedOn=?,CreatedBy=? WHERE ID=?";

				PreparedStatement statement = connection.prepareStatement(updatesql);
				statement.setInt(1, trxn.getId());
				statement.setString(2, trxn.getNameOnCard());
				statement.setString(3, trxn.getCardNumber());
				statement.setDouble(4, trxn.getUnitPrice());
				statement.setInt(5, trxn.getQuantity());
				statement.setDouble(6, trxn.getTotalPrice());

				statement.setString(7, trxn.getExpDate());

				statement.setString(8, trxn.getCreatedOn());

				statement.setString(9, trxn.getCreatedBy());

				int rowUpdated = statement.executeUpdate();

				if (rowUpdated > 0) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}
		return false;
	}

	private void userInput(Collection<Transaction> transRecords, Transaction trans) {
		 

		String nameOnCard;
		do {
				System.out.println("Enter Name on Card: ");
				logger.log(Level.INFO, "Enter Name on Card:");
				nameOnCard = sc.nextLine();
				logger.log(Level.INFO, nameOnCard);
			} while (nameOnCard.matches(".*[;:!@#$%^*+?<>].*") || nameOnCard.matches(".*[1-9].*") || nameOnCard.isEmpty());
		trans.setNameOnCard(nameOnCard);

		String CardNumber;

		boolean flag = false;
		do {
			System.out.println("Enter Card Number: ");
			logger.log(Level.INFO, "Enter Card Number: ");
			CardNumber = sc.next();
			logger.log(Level.INFO, CardNumber);
			if ((CardNumber.matches("^4[0-9]{15}$|^5[1-5][0-9]{14}$|^34[0-9]{13}$|^37[0-9]{13}$"))) {
				flag = true;
			}

			else {
				flag = false;
				System.out.println("Invalid Card Number");
				logger.log(Level.INFO, "Invalid Card Number ");
			}
		} while (flag == false);
		trans.setCardNumber(CardNumber);

		System.out.println("Enter unit price: ");
		logger.log(Level.INFO, "Enter unit price:  ");
		double unitPrice = sc.nextDouble();
		logger.log(Level.INFO, Double.toString(unitPrice));
		trans.setUnitPrice(unitPrice);
		System.out.println("Enter quantity: ");
		logger.log(Level.INFO, "Enter quantity:  ");
		int quantity = sc.nextInt();
		logger.log(Level.INFO, Integer.toString(quantity));
		trans.setQuantity(quantity);
		double totalPrice = (unitPrice * quantity);
		trans.setTotalPrice(totalPrice);
		logger.log(Level.INFO, "Total Item Price:  ");
		logger.log(Level.INFO, Double.toString(totalPrice));
		System.out.print("\nEnter Expiry Month (MM): ");
		logger.log(Level.INFO, "Enter Expiry Month (MM):  ");
		int month = sc.nextInt();
		logger.log(Level.INFO, Integer.toString(month));
		if (month >= 01 && month <= 12) {

		} else {
			do {
				System.out.print("\nEnter valid month in 'MM' format: ");
				logger.log(Level.INFO, "Enter valid month in 'MM' format:  ");
				month = sc.nextInt();
				logger.log(Level.INFO, Integer.toString(month));
			} while (month < 1 || month > 12);
		}
		System.out.print("\nEnter Expiry Year (YY): ");
		logger.log(Level.INFO, "Enter Expiry Year (YY):  ");
		int year = sc.nextInt();
		logger.log(Level.INFO, Integer.toString(year));
		if (year >= 20 && year <= 30 ) {

		} else {
			do {
				System.out.print("\nEnter valid year in 'YY' format: ");
				logger.log(Level.INFO, "Enter valid year in 'YY' format:  ");
				year = sc.nextInt();
				logger.log(Level.INFO, Integer.toString(year));
			} while (year < 20 || year > 30);
		}

		String ExpDate = month + "/" + year;
		trans.setExpDate(ExpDate);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		String dateTime = (dateFormat.format(date));
		trans.setCreatedOn(dateTime);
		logger.log(Level.INFO, "Created On:  ");
		logger.log(Level.INFO, dateTime);

		String createdBy = System.getProperty("user.name");
		trans.setCreatedBy(createdBy);
		transRecords.add(trans);
		logger.log(Level.INFO, "Created By:  ");
		logger.log(Level.INFO, createdBy);

	}

}
