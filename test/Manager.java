import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Scanner;

public class Manager {

	public static void main(String[] args) {
		// try {
		// 	/**
		// 	 * Returns the Class object associated with the class or interface with the
		// 	 * given string name. Given the fully qualified name for a class or interface
		// 	 * this method attempts to locate, load, and link the class or interface.
		// 	 * https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html
		// 	 */
		// 	Class.forName("com.mysql.cj.jdbc.Driver");
		// 	try (Connection conn = DriverManager
		// 			.getConnection("jdbc:mysql://localhost:3306/university?user=root&password=db123");
		// 			Statement stmt = conn.createStatement();) {
		// 		try {
		// 			stmt.executeUpdate("insert into instructor values ('24', 'Green', 'Finance', 98000)");

		// 		} catch (SQLException sqle) {
		// 			System.out.println("Could not insert tuple. " + sqle);
		// 		}

		// 		ResultSet rset = stmt
		// 				.executeQuery("select dept_name, avg(salary) as att2 from instructor group by dept_name");
		// 		while (rset.next()) {
		// 			System.out.println(rset.getString("dept_name") + " " + rset.getFloat("att2"));
		// 		}	
				
		// 	} catch (SQLException e) {
		// 		e.printStackTrace();
		// 	}
		// } catch (ClassNotFoundException e) {
		// 	e.printStackTrace();
		// }

		Scanner scan = new Scanner(System.in);
		String userInput;

		System.out.println("Welcome to Erik's Project 3 for DBMS! To start, please login to the DB.");
		System.out.println("What is your username?");
		userInput = scan.next();
		if (userInput instanceof String);
	}	
}


