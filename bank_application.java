package bank_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class bank_application {
	static Scanner sc = new Scanner(System.in);
	
	private long validateAccountNumber(String prompt) {
	    long accountnumber = 0;
	    boolean valid = false;

	    while (!valid) {
	        System.out.print(prompt);
	        String input = sc.nextLine().trim();
	        
	        if (input.matches("\\d{12}")) {
	            try {
	                accountnumber = Long.parseLong(input);
	                valid = true; 
	            } catch (NumberFormatException e) {
	                System.out.println("Error parsing number. Please try again.");
	            }
	        } else {
	            System.out.println("Invalid account number! It must be exactly 12 digits.");
	        }
	    }
	    return accountnumber;
	}

	void new_registration() throws ClassNotFoundException, SQLException {
		Connection con = DataBase.getConnection();

		PreparedStatement ps = con.prepareStatement("insert into bank values(?,?,?,?,?)");
		
		System.out.println("---- New Registration ------");
		
		long account_number = validateAccountNumber("Enter account_number (12 digits) : ");
		
		System.out.print("enter the name : ");
		String name = sc.nextLine();
		
		System.out.print("enter the mobile number (10 digit) : ");
		long contact = sc.nextLong();
		sc.nextLine();
		
		System.out.print("enter the address : ");
		String address = sc.nextLine();
		
		ps.setLong(1, account_number);
		ps.setString(2, name);
		ps.setLong(3, contact);
		ps.setString(4, address);
		ps.setLong(5, 0);
		
		ps.executeUpdate();
		
		System.out.println("Register successfully!!!");

		DataBase.closeConnection(con);
	}
	
	void Deposit_money() throws ClassNotFoundException, SQLException {
		Connection con = DataBase.getConnection();
		
		long account_number = validateAccountNumber("Enter account_number (12 digits) : ");

		
		System.out.print("enter the amount : ");
		long Amount = sc.nextLong();

		PreparedStatement ps = con.prepareStatement("update bank set amount = amount + ? where account_number = ?;");
		
		ps.setLong(1, Amount);
		ps.setLong(2, account_number);
		
		int rowsUpdate = ps.executeUpdate();
		if(rowsUpdate>0)
			System.out.println("Money deposited successfully!!!");
		else
			System.out.println("No record found with "+account_number+" account number.");
		
		DataBase.closeConnection(con);
	}
	
	void withdraw_money() throws ClassNotFoundException,SQLException {
		Connection con = DataBase.getConnection();

		System.out.println("---- Withdraw money ----");
		
		long account_number = validateAccountNumber("Enter account_number (12 digits) : ");
		
		System.out.print("Enter the amount to withdraw : ");
		long Amount = sc.nextLong();
		
		PreparedStatement ps = con.prepareStatement("update bank set amount = amount - ? where account_number = ?;");
		
		ps.setLong(1, Amount);
		ps.setLong(2, account_number);
		
		String getamount = "select amount from bank where account_number ="+account_number; 
		Statement s = con.createStatement();
		ResultSet rs=s.executeQuery(getamount);
		if(rs.next()) {
			long amount = rs.getLong("amount");
			if(Amount<=amount) {
				int rowsUpdate = ps.executeUpdate();
				if(rowsUpdate>0) {
					System.out.println("Money withdraw successfully!!!");
				} else {
					System.out.println("No record found with "+account_number+" account number.");
				}
			}
			else {
				System.out.println("amount is greater than your balance!!!");
			}
		}
		
		DataBase.closeConnection(con);

	}
	
	void check_balance() throws ClassNotFoundException, SQLException {
		Connection con = DataBase.getConnection();
		
		long account_number = validateAccountNumber("Enter account_number (12 digits) : ");
		
		String show = "select amount from bank where account_number = ?;";
		PreparedStatement ps = con.prepareStatement(show);
		ps.setLong(1, account_number);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			System.out.println("Your Bank Balance is : "+rs.getInt("amount")+" ₹");
		} else {
			System.out.println("No record found with "+account_number+" account number.");
		}
		
		DataBase.closeConnection(con);
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		bank_application b = new bank_application();
		
		while(true) {
			System.out.println("-- Welcome to the Bank Application --");
			System.out.println("----- Choose options :  ------------");
			System.out.println("1. New registration");
			System.out.println("2. Deposit money");
			System.out.println("3. Withdraw money");
			System.out.println("4. Check balance");
			System.out.println("5. Exit");
			
			System.out.print("Choice is : ");
			int ch = sc.nextInt();
			sc.nextLine(); 
			
			switch(ch) {
			case 1:
				b.new_registration();
				break; 
			case 2:
				b.Deposit_money();
				break; 
			case 3:
				b.withdraw_money();
				break; 
			case 4:
				b.check_balance();
				break; 
			case 5: 
				System.out.println("Thank you for using the Bank Application!");
				sc.close(); 
				System.exit(0);
				break;
			default:
				System.out.println("Please enter a correct choice (1-5)!");
			}
		}
	}
}
