package assignment2.embededSql; //TODO delete before handing in.
import java.sql.*;

public class Assignment2 {
    
  // A connection to the database  
  Connection connection;
  
  // Statement to run queries
  Statement sql;
  
  // Prepared Statement
  PreparedStatement ps;
  
  // Resultset for the query
  ResultSet rs;
  
  //CONSTRUCTOR
  Assignment2(){
  }
  
  //Using the input parameters, establish a connection to be used for this session. Returns true if connection is sucessful
  public boolean connectDB(String URL, String username, String password){
	  try {
			// Load JDBC driver
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Cannot locate PostgreSQL classes.");
			e.printStackTrace();
			return false;
		}

		System.out.println("PostgreSQL JDBC Driver Registered.");

		try {
			//Make the connection to the database
			connection = DriverManager.getConnection(URL, username, password);
			return true;
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}
  }
  
  //Closes the connection. Returns true if closure was sucessful
  public boolean disconnectDB(){
      try{
    	  connection.close();
    	  return true;
      } catch (SQLException e){
    	  return false;
      }
  }
    
  //Inserts row into the winemaker table
  public boolean insertCountry (int cid, String name, String coach) {
   return false;
  }
  
  public int getPlayersCount(int cid){
	return -1;  
  }
   
  public String getPlayerInfo(int pid){
   return "";
  }

  public boolean chgStadiumLocation(int sid, String newCity){
   return false;
  }

  public boolean deleteCountry(int cid){
   return false;        
  }
  
  public String listPlayers(String fcname){
	return "";
  }
  
  public boolean updateValues(String cname, int incrV){
    return false;
  }
  
  public String query7(){
   return "";
  }
    
  public boolean updateDB(){
	return false;    
  }
  
}