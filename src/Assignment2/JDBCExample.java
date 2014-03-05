package Assignment2;
// Credits: http://www.fankhausers.com/postgresql/jdbc/
//          http://jdbc.postgresql.org/documentation/91/


// Instructions:
// 1) Connect to "ssh dbsrv1.cdf.toronto.edu" using your cdf username and password
// 2) Download the JDBC driver (version 9.1-903 JDBC 4) from http://jdbc.postgresql.org/download.html and 
//    copy jdbc jar file (using sftp) to dbsrv1 server.
// 3) On line 60 connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/csc343h-username", "username", "");
//    -leave as is the host and port number ("localhost:5432"); 
//    -replace "username" with your cdf username, where the fields "csc343h-username" is the database name and "username" is the username that will be used to login into the database
//    - you may need to set the password field. The default one is set to empty (""); 
// 4) Compile the code:
//         javac JDBCExample.java
// 5) Run the code:
//         java -cp /*****path-to-jdbc-directory*****/postgresql-9.1-903.jdbc4.jar:. JDBCExample   
//    where postgresql-9.1-903.jdbc4.jar is jdbc jar file downloaded in step 2


import java.sql.*;

 
public class JDBCExample {
 
	public static void main(String[] argv) {

		// A connection to the database		
		Connection connection;

		// Statement to run queries
		Statement sql;

		//Prepared Statement
		PreparedStatement ps;

		//Resultset for the query
		ResultSet rs;
 
		System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");
 
		try {
		
 			// Load JDBC driver
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
			return;
 
		}
 
		System.out.println("PostgreSQL JDBC Driver Registered!");
 
		try {
			
 			//Make the connection to the database, ****** but replace "username" with your username ******
			System.out.println("*** Please make sure to replace 'username' with your cdf username in the jdbc connection string!!!");
			connection = DriverManager.getConnection("jdbc:postgresql://dbsrv1.cdf.toronto.edu:5432/csc343h-c4lisunp", "c4lisunp", "");
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
 
		}
 
		if (connection != null) {
			System.out.println("You made it, take control of your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

		try{

			//Create a Statement for executing SQL queries
			sql = connection.createStatement(); 

			//---------------------------------------------------------------------------------------
			//Create jdbc_demo table
			String sqlText;
			sqlText = "CREATE TABLE jdbc_demo(                  " +
				  "                       code int,         " +
                	          "                       text varchar(20)  " +
                        	  "                      ) ";

			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
    			sql.executeUpdate(sqlText);


			//---------------------------------------------------------------------------------------			
			//Insert into jdbc_demo table
			sqlText = "INSERT INTO jdbc_demo " +
                                  "VALUES (1, 'One')";
			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
			sql.executeUpdate(sqlText);
			//System.exit(1);

 
			sqlText = "INSERT INTO jdbc_demo   " +
                                  "VALUES (4, 'Five')";
			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
			sql.executeUpdate(sqlText);



			//---------------------------------------------------------------------------------------
			//Upate values of jdbc_demo
			sqlText = "UPDATE jdbc_demo      " +
                                  "   SET text = 'Four'  " +
                                  " WHERE  code = 4      "; 
			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
			sql.executeUpdate(sqlText);
			System.out.println (sql.getUpdateCount() + " rows were update by this statement.\n");

	

			//---------------------------------------------------------------------------------------
			//Use Prepared Statement for insertion
			System.out.println("\n\nNow demostrating a prepared statement (inserting 5 tuples)...");
		        sqlText = "INSERT INTO jdbc_demo " + 
        			  "VALUES (?,?)          ";
    			System.out.println("Prepared Statement: " + sqlText.replaceAll("\\s+", " ") + "\n");
    			ps = connection.prepareStatement(sqlText);
			for (int i=1; i<=5; i++){
				System.out.println(i + "...\n");
		      		
				//Populate the prepared statement
				//Set column one (code) to i
				ps.setInt(1,i);         
				
				//Column two gets a string
			      	ps.setString(2,"random insertion");

				//Execute insert
				ps.executeUpdate();
		    	}
			ps.close();


			
			//---------------------------------------------------------------------------------------
			//Select from jdbc_demo table
			sqlText = "SELECT *       " +
                                  " FROM jdbc_demo";
			System.out.println("Now executing the command: " + sqlText.replaceAll("\\s+", " ") + "\n");
                       	rs = sql.executeQuery(sqlText);
			if (rs != null){
				while (rs.next()){
					System.out.println("code = " + rs.getInt("code") + "; text = "+rs.getString(2)+"\n");
      				}
    			}

			//Close the resultset
			rs.close();

		

			//---------------------------------------------------------------------------------------
			//Drop jdbc_demo table
			sqlText = "DROP TABLE jdbc_demo";
			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
        	        sql.executeUpdate(sqlText);

			connection.close();
		} catch (SQLException e) {

                        System.out.println("Query Exection Failed!");
                        e.printStackTrace();
                        return;

                }		
	}
 
}