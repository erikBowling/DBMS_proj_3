import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Manager {

	public static void main(String[] args) {

		String url = "jdbc:mysql://localhost:3306/university";
		Scanner inputStream = new Scanner(System.in);
		Connection DBConnection = null;

		System.out.println("\t\t** WELCOME **");

		printInstructions();

		//Connection DBConnection = establishConnection(url, inputStream);
		try{
			DBConnection = DriverManager.getConnection(url, "root", "db123");
		}catch(SQLException e){
			System.out.println("bad connection");
			System.exit(1);
		}

		printMenu();

		System.out.println("Please enter in your choice. (0-5)");
		int userChoice = -1;

		// Loop while the user choice isn't valid
		while(userChoice < 0 || userChoice > 5){
			try{
		 		userChoice = inputStream.nextInt();
			}catch(InputMismatchException e){
				System.out.println("That's not a valid input. Please enter an integer between 0 and 5.");
				inputStream.nextLine(); // Clear the buffer in our scanner in case of wrong input
			}

			if(userChoice < 0 || userChoice > 5)
				System.out.println("Please enter a number between 0 and 5.");
		}

		String query;
		switch(userChoice){
			case 0:
				System.out.println("\t** Goodbye **");
				System.exit(0);
				break;
			case 1:
				query = "SELECT * FROM department;";
				try{
					PreparedStatement state = DBConnection.prepareStatement(query);
					ResultSet rs = state.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					System.out.println(columnCount);
					while(rs.next()){
						System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t\t");
					}
				} catch(SQLException e){
					System.out.println(e);
				}
				break;

			case 2:
				query = "SELECT * FROM course;";
				try{
					PreparedStatement state = DBConnection.prepareStatement(query);
					ResultSet rs = state.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					System.out.println(columnCount);
					while(rs.next()){
						System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t\t");
					}
				} catch(SQLException e){
					System.out.println(e);
				}
				break;

			case 3:
				break;

			case 4:
				break;

			case 5:
				break;
		}

		// Finish the program by closing out the inputStream
		inputStream.close();
	}


	private static void printMenu(){
		System.out.println("\t\t*** MENU ***");
		System.out.println("1.) Display all information about departments.");
		System.out.println("2.) Display all information about courses.");
		System.out.println("3.) Add a course");
		System.out.println("4.) Delete course");
		System.out.println("5.) Update a course");
		System.out.println("0.) Exit program\n");
	}

	private static void printInstructions(){
		System.out.println("instruction");

	}


	private static Connection establishConnection(String url, Scanner inputStream){
		Boolean connectedStatus = false; // Used to loop. Set to true upon successful connection
		Connection DBConnection = null; // DB connection to be returned

		System.out.print("** Please enter in your username and password. Enter in the empty string for username to exit. **\n");

		// Loop is used to create the connection to the DB. If the user enters nothing for either username or password
		// the program exits
		while(!connectedStatus){
			// Take in the username
			System.out.println("Username: ");
			String username = inputStream.nextLine();
			if(username.isEmpty()){
				inputStream.close();
				System.exit(0);
			}
			// Take in the password
			System.out.println("Password: ");
			String password = inputStream.nextLine();

			// Attempt to make connection. If no connection made, print the error and loop back around
			try {
				DBConnection = DriverManager.getConnection(url, username, password);
				System.out.println("Database connected!");
				connectedStatus = true;
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

		return DBConnection;
	}
}


