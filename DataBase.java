package bank_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
	static String URL="jdbc:postgresql://localhost:5432/bank_application";
	static String username="postgres";
	static String password="pass@123";
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(URL,username,password);
		return con;
	}
	
	public static void closeConnection(Connection con) throws SQLException {
		con.close();
	}
}
