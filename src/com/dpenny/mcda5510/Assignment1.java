package com.dpenny.mcda5510;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dpenny.mcda5510.dao.MySQLAccess;


public class Assignment1 {
	public static Logger logger;

	public static void main(String[] args) {
	
		MySQLAccess MySQLAccess = new MySQLAccess();
		Scanner sc = new Scanner(System.in);
		logger = MySQLAccess.callLogger();

		char option;
		try {
			do {
				System.out.println("SELECT ANY CRUD OPERATION");
				System.out.println("\n1. View Transaction\n2. Create Transaction\n3. Update Transaction\n4. Delete Transaction");
				System.out.print("\nEnter your Choice: ");
				MySQLAccess.logger.log(Level.INFO, "Enter your Choice: ");
				int optionIs = sc.nextInt();	
				MySQLAccess.logger.log(Level.INFO, Integer.toString(optionIs));
				System.out.print("\n");
				System.out.println("Enter TransactionID: ");
				MySQLAccess.logger.log(Level.INFO, "Enter TransactionID: ");
				int id = sc.nextInt();
				switch (optionIs) {

				case 1: {
						MySQLAccess.viewResult(id);
						MySQLAccess.logger.log(Level.INFO, Integer.toString(id));
						break;
				}
				case 2: {
						MySQLAccess.createResult(id);
						MySQLAccess.logger.log(Level.INFO, Integer.toString(id));
						break;
				}
				case 3: {
						MySQLAccess.updateResult(id);
						MySQLAccess.logger.log(Level.INFO, Integer.toString(id));
						break;
				}
				case 4: {
						MySQLAccess.deleteResult(id);
						MySQLAccess.logger.log(Level.INFO, Integer.toString(id));
						break;
				}
				default: {
						System.out.println("Run Program Again!!! ");
						MySQLAccess.logger.log(Level.INFO, "Run Program Again");
						System.exit(0);
						break;
				}
		}
				System.out.println("To Continue press (Y/y) or press any other key to exit");
				MySQLAccess.logger.log(Level.INFO, "To Continue press (Y/y) or press any other key to exit");
				option = sc.next().charAt(0);
				MySQLAccess.logger.log(Level.INFO, Character.toString(option));

			} while ((option == 'Y' || option == 'y'));
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			MySQLAccess.logger.log(Level.WARNING, e.getMessage());
		}

		finally {
			sc.close();
		}
	}
}
		


