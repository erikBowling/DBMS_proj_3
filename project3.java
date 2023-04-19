/*
 * Name: Erik Bowling
 * Date: April 18, 2023
 * Assignment: Project 3
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class project3 {
	public static void main(String[] args) {

		// Url to the local host mysql server with DB university
		String url = "jdbc:mysql://localhost:3306/university";
		Connection DBConnection = null;
		
		// Instantiate scanner for user input. Set delimiter to be only newlines
		Scanner inStream = new Scanner(System.in);
		inStream.useDelimiter(Pattern.compile("\\n"));

		// Print the introduction message
	 	printIntroduction();

		// Connection to DB
		DBConnection = establishConnection(url, inStream);

		// Main loop. Only exit point is by entering 0 at the main menu
		while(true){
			printMenu();

			// Get user input for menu
			System.out.print("\nChoice: ");
			int userChoice = -1;

			// Loop while the user choice isn't valid
			while(userChoice < 0 || userChoice > 5){
				// Try to set user choice as next int in scanner. Handle type error
				try{
					userChoice = inStream.nextInt();
				}catch(InputMismatchException e){
					System.out.println("That's not a valid input. Please enter an integer between 0 and 5.");
					inStream.nextLine(); // Clear the buffer in our scanner in case of wrong input
					continue;
				}
				
				// If user input is an integer but not in range.
				if(userChoice < 0 || userChoice > 5)
					System.out.println("That's not a valid input. Please enter an integer between 0 and 5.");
			}
			
			// Switch on user input. This is how we handle user choice.
			switch(userChoice){
				case 0:
					System.out.format("+------------------------------------------------------+%n");
					System.out.format("|                        GOODBYE                       |%n");
					System.out.format("+------------------------------------------------------+%n");
					inStream.close(); // Close scanner to free up memory
					System.exit(0);
					break;
				case 1:
					displayDepartment(DBConnection);
					break;

				case 2:
					displayCourse(DBConnection);
					break;

				case 3:
					addCourse(DBConnection, inStream);
					break;

				case 4:
					deleteCourse(DBConnection, inStream);
					break;

				case 5:
					updateCourse(DBConnection, inStream);
					break;
			}
			
			// User will hang at the result screen of the last query ran until they hit enter
			System.out.println("\nPress enter to continue...");
			inStream.next();
		}
	}

	private static void printIntroduction(){
		System.out.format("+------------------------------------------------------+%n");
		System.out.format("|                     Introduction                     |%n");
		System.out.format("+------------------------------------------------------+%n");

		System.out.println("\nThis CLI is used to manipulate data in a local mysql");
		System.out.println("database. It assumes you are using the Silberschatz");
		System.out.println("university schema and have a database instantiated");
		System.out.println("already.\n");
		System.out.println("How to use:");
		System.out.println("  * Follow the onscreen prompts and menus.");
		System.out.println("  * Enter the numbers from the menus");
		System.out.println("  * Have fun!");
		System.out.println("\n\t-Erik\n");
	}

	private static void printMenu(){

		// Menu Formatting
		String PMF = "| %-6d | %-23s |%n"; // Pretty menu format
		System.out.format("+----------------------------------+%n");
		System.out.format("|             MAIN MENU            |%n");
		System.out.format("+--------+-------------------------+%n");
		System.out.format("| Enter  |         Option          |%n");
		System.out.format("+--------+-------------------------+%n");
		System.out.format(PMF, 1,  "Display all departments");
		System.out.format(PMF, 2,  "Display all courses");
		System.out.format(PMF, 3,  "Add a course");
		System.out.format(PMF, 4,  "Delete a course");
		System.out.format(PMF, 5,  "Update a course");
		System.out.format(PMF, 0,  "Exit");
		System.out.format("+--------+-------------------------+%n");
	}

	private static Connection establishConnection(String url, Scanner inStream){
		Boolean connectedStatus = false; // Used to loop. Set to true upon successful connection
		Connection DBConnection = null; // DB connection to be returned

		System.out.format("+------------------------------------+%n");
		System.out.format("|                LOGIN               |%n");
		System.out.format("+------------------------------------+%n");
		System.out.println("Login with your mysql login credentials");
		System.out.println("Enter nothing for the username to quit.\n");
		// Loop is used to create the connection to the DB. If the user enters nothing for either username or password
		// the program exits
		while(!connectedStatus){
			// Take in the username
			System.out.print("Username: ");
			String username = inStream.nextLine();
			if(username.isEmpty()){
				inStream.close();
				System.exit(0);
			}
			// Take in the password
			System.out.print("\nPassword: ");
			String password = inStream.nextLine();

			// Attempt to make connection. If no connection made, print the error and loop back around
			try {
				DBConnection = DriverManager.getConnection(url, username, password);
				System.out.println("\nDatabase connected!\n");
				connectedStatus = true;
			} catch (SQLException e) {
				System.out.println("\n" + e.getMessage() + "\nTry again\n");
			}
		}

		return DBConnection;
	}

	private static void displayDepartment(Connection DBConnection){
		// Get everything from department.
		// Only show dept_name and Building
		String query = "SELECT dept_name, building FROM department;";
		try{
			// Execute query
			PreparedStatement state = DBConnection.prepareStatement(query);
			ResultSet rs = state.executeQuery();

			String PMF = "| %-12s | %-12s |%n"; // Pretty menu format
			System.out.format("+--------------+--------------+%n");
			System.out.format(PMF, "DEPT NAME", "BUILDING");
			System.out.format("+--------------+--------------+%n");
			
			// While the result set still has rows in it, print the current row
			while(rs.next()){
				System.out.format(PMF, rs.getString(1), rs.getString(2));
			}
			System.out.format("+--------------+--------------+%n");

		} catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}

	private static void displayCourse(Connection DBConnection){
		// QUERY
		String query = "SELECT * FROM course;";
		try{
			// Execute query
			PreparedStatement state = DBConnection.prepareStatement(query);
			ResultSet rs = state.executeQuery();
			
			// String formatting
			String PMF = "| %-9s | %-35s | %-10s | %-8s |%n"; // Pretty menu Format
			System.out.println("+-----------+-------------------------------------+------------+----------+");
			System.out.format(PMF, "Course ID", "Title", "Department", "Credits");
			System.out.println("+-----------+-------------------------------------+------------+----------+");

			// While there are still results, print them
			while(rs.next()){
				System.out.format(PMF, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}

			System.out.println("+-----------+-------------------------------------+------------+----------+");

		}catch(SQLException e){
			System.out.println(e);
		}
	}

	private static void addCourse(Connection DBConnection, Scanner inStream){
		// QUERY
		String query = "INSERT into course values(?, ?, ?, ?);";

		// Pretty Menu
		System.out.format("+----------------------------------+%n");
		System.out.format("|             ADD COURSE           |%n");
		System.out.format("+----------------------------------+%n");
		try {
			PreparedStatement state = DBConnection.prepareStatement(query);

			// course_id
			System.out.print("Course ID: ");
			String course_id = inStream.next();

			// If user enters in a blank id, return
			if(course_id.isBlank()){
				System.out.println("\nThe Course ID can't be empty.");
				return;
			}
			state.setString(1, course_id);

			// title
			String title;
			System.out.print("Course Title: ");
			title = inStream.next();
			if(title.isBlank()){
				title = null;
			}
			state.setString(2, title);

			// department
			System.out.print("Department Name (Must match existing dept): ");
			state.setString(3, inStream.next());

			// credits
			System.out.print("Credit Hours: ");
			state.setInt(4, inStream.nextInt());

			// run query
			state.executeUpdate();
			System.out.format("+----------------------------------+%n");
			System.out.format("|     COURSE ADDED SUCCESSFULLY    |%n");
			System.out.format("+----------------------------------+%n");

			// Catch sql exceptions or data integrity checks. Also type errors on credits
		}catch(SQLException e){
			System.out.println("\nERROR:" + e.getMessage() + "\n");

		}catch(InputMismatchException e){
			System.out.println("\nERROR: Credits expects an integer > 0.\n");
		}

	}

	private static void deleteCourse(Connection DBConnection, Scanner inStream){
		// QUERY
		String query = "DELETE FROM course WHERE course_id = ?;";

		// Pretty Menu
		System.out.format("+----------------------------------+%n");
		System.out.format("|           DELETE COURSE          |%n");
		System.out.format("+----------------------------------+%n");

		try{
			PreparedStatement state = DBConnection.prepareStatement(query);

			// course_id
			System.out.print("Course ID: ");
			String course_id = inStream.next();
			if(course_id.isBlank()){
				System.out.println("ERROR: The course can't be blank.\n");
				return;
			}

			// Execute Query
			state.setString(1, course_id);
			state.executeUpdate();

			// Pretty Success message
			System.out.format("+----------------------------------+%n");
			System.out.format("|    SUCCESFULLY DELETED COURSE    |%n");
			System.out.format("+----------------------------------+%n");

		}catch(SQLException e){
			System.out.println("\n" + e.getMessage() + "\n");
		}

	}

	private static void updateCourse(Connection DBConnection, Scanner inStream){
		// Formatted menu
		String PMF = "| %-6d | %-25s |%n"; // Pretty menu format
		System.out.format("+--------+---------------------------+%n");
		System.out.format("| Enter  |         Option            |%n");
		System.out.format("+--------+---------------------------+%n");
		System.out.format(PMF, 1, "Update Title");
		System.out.format(PMF, 2, "Update Credits");
		System.out.format(PMF, 3, "Update Title and credits");
		System.out.format(PMF, 0, "Exit");
		System.out.format("+--------+---------------------------+%n");

		// Initialize userChoice to -1
		int userChoice = -1;

		// Validate user input by stored in userChoice
		while(userChoice < 0 || userChoice > 3){
			try{
				System.out.print("Choice: ");
				userChoice = inStream.nextInt();

			// If user enters in something other than int
			}catch(InputMismatchException e){
				System.out.println("\nThat's not a valid input. Please enter an integer between 0 and 3.\n");
				inStream.nextLine(); // Clear the buffer in our scanner in case of wrong input
				continue;
			}

			// If user entered in integer but it wasn't in range
			if(userChoice < 0 || userChoice > 3)
				System.out.println("\nThat's not a valid input. Please enter an integer between 0 and 3\n");
				
		} // end while

		// Exit function
		if(userChoice == 0){
			System.out.println("\nAborted the update. Heading back to the main menu.");
			return;
		}

		// Initialize query variable. Updates to appropriate query depending on user input
		String query = "";

		// Get course_id for course to modify
		System.out.print("\nWhat course to modify? (course_id): ");
		String course_id = inStream.next();

		// If course_id is empty, leave function
		if(course_id.isEmpty()){
			System.out.println("The course ID can't be blank. Aborting");
			return;
		}

		// Switch on user choice. 1-3
		switch(userChoice){
			case 1:
				try{
					// Update to appropriate query
					query = "UPDATE course SET title = ? WHERE course_id = ?;";
					PreparedStatement state = DBConnection.prepareStatement(query);

					// Get title
					System.out.print("What is the new title?: ");
					String title = inStream.next();

					// If title is empty, set it to null
					if(title.isEmpty()){
						title = null;
					}

					// Execute Query
					state.setString(1, title);
					state.setString(2, course_id);
					state.executeUpdate();
					
					// Pretty menu formatting
					String PF = "| %-10s | %-35s |%n";
					System.out.format("%n+------------+-------------------------------------+%n");
					System.out.format("| UPDATED    |                 VALUE               |%n");
					System.out.format("+------------+-------------------------------------+%n");
					System.out.format(PF, "TITLE", title);
					System.out.format("+------------+-------------------------------------+%n");
					
					// Catch any sql errors.
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				break;

			case 2:
				try{
					// QUERY
					query = "UPDATE course SET credits = ? WHERE course_id = ?;";
					PreparedStatement state = DBConnection.prepareStatement(query);
					System.out.print("How many credits is the course?: ");

					try{
						// Query 
						int credits = inStream.nextInt();
						state.setInt(1, credits);
						state.setString(2, course_id);
						state.executeUpdate();
						
						// Output formatting
						String PF = "| %-10s | %-10d |%n";
						System.out.format("%n+------------+------------+%n");
						System.out.format("| UPDATED    |   VALUE    |%n");
						System.out.format("+------------+------------+%n");
						System.out.format(PF, "Credits", credits);
						System.out.format("+------------+------------+%n");
					
						// Catch if credits isn't an integer
					}catch(InputMismatchException e){
						System.out.println("\nERROR: Credits requires an integer input > 0.");
						return;
					}

					// Catch an sql exception. Mostly if the credits aren't in range
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				break;

			case 3:
				try{
					// Update Query
					query = "UPDATE course SET title = ?, credits = ? WHERE course_id = ?;";
					PreparedStatement state = DBConnection.prepareStatement(query);

					// Title
					System.out.print("What is the new title?: ");
					String title = inStream.next();
					if(title.isEmpty()){
						title = null;
					}

					// Credits and execution
					System.out.print("How many credits is the course?: ");
					try{
						int credits = inStream.nextInt();
					
						state.setString(1, title);
						state.setInt(2, credits);
						state.setString(3, course_id);
						state.executeUpdate();

						// Output formatting
						String PF = "| %-10s | %-35s |%n";
						System.out.format("%n+------------+-------------------------------------+%n");
						System.out.format("| UPDATED    |                 VALUE               |%n");
						System.out.format("+------------+-------------------------------------+%n");
						System.out.format(PF, "Title", title);
						System.out.format(PF, "Credits", credits);
						System.out.format("+------------+-------------------------------------+%n");

					// Catch if the user inputs something other than integer for credits
					}catch(InputMismatchException e){
						System.out.println("\nERROR: Credits requires an integer input > 0.");
						return;
					}

					// Catch any sql error.
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				break;
		}
	}
}
