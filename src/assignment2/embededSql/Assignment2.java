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

	// CONSTRUCTOR
	Assignment2() {
	}

	// Using the input parameters, establish a connection to be used for this
	// session. Returns true if connection is sucessful
	public boolean connectDB(String URL, String username, String password) {
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
			// Make the connection to the database
			connection = DriverManager.getConnection(URL, username, password);
			return true;
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}
	}

	// Closes the connection. Returns true if closure was sucessful
	public boolean disconnectDB() {
		try {
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Inserts row into the winemaker table
	public boolean insertCountry(int cid, String name, String coach) {
		try {
			String sqlText = ("INSERT INTO country " + "VALUES (" + cid + ", '"
					+ name + "', '" + coach + "')");
			System.out.println("Executing command: " + sqlText);
			sql.executeUpdate(sqlText);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getPlayersCount(int cid) {
		try {
			int result;
			String sqlText = ("SELECT SUM(pid) AS cnt FROM player GROUP BY cid");
			System.out.println("Executing command: " + sqlText);
			rs = sql.executeQuery(sqlText);
			if (rs.first())
				result = rs.getInt("cnt");
			else
				result = -1;
			rs.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public String getPlayerInfo(int pid) {
		try {
			String result;
			String sqlText = ("SELECT fname, lname, position, goals "
					+ "FROM player WHERE pid = " + pid);
			System.out.println("Executing command: " + sqlText);
			rs = sql.executeQuery(sqlText);
			if (rs.first())
				result = (rs.getString("fname") + ":" + rs.getString("lname")
						+ ":" + rs.getString("position") + ":" + rs
						.getString("goals"));
			else
				result = "";
			rs.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean chgStadiumLocation(int sid, String newCity) {
		try {
			String sqlText = ("UPDATE stadium SET city = '" + newCity
					+ "' WHERE sid = " + sid);
			System.out.println("Executing command: " + sqlText);
			int updated = sql.executeUpdate(sqlText);
			System.out.println(updated + " rows were updated.");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteCountry(int cid) {
		try {
			String sqlText = ("DELETE FROM country WHERE cid = " + cid);
			System.out.println("Executing command: " + sqlText);
			int updated = sql.executeUpdate(sqlText);
			System.out.println(updated + " rows were updated.");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String listPlayers(String fcname) {
		try {
			String sqlText = ("SELECT p.fname AS fn, p.lname AS ln, p.position AS po, p.goals AS go, c.name AS cn "
					+ "FROM player AS p, country AS c, club AS b "
					+ "WHERE p.cid = c.cid AND p.fcid = b.fcid AND "
					+ "b.name = '" + fcname + "'");
			System.out.println("Executing command: " + sqlText);
			rs = sql.executeQuery(sqlText);
			String result = "";
			while (rs.next()) {
				// Parse all the rows.
				result += (rs.getString("fn") + ":" + rs.getString("ln") + ":"
						+ rs.getString("po") + ":" + rs.getString("go") + ":"
						+ rs.getString("cn") + "#");
			}
			rs.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean updateValues(String cname, int incrV) {
		try {
			boolean updated = false;
			String sqlText = 
					("SELECT p.pid AS pid, p.value AS val "
					+ "FROM player AS p, country AS c "
					+ "WHERE p.cid = c.cid AND c.name = '" + cname + "'");

			System.out.println("Executing command: " + sqlText);
			rs = sql.executeQuery(sqlText);
			while (rs.next()){
				int pid = rs.getInt("pid");
				int val = rs.getInt("val") + incrV;
				sql.executeUpdate("UPDATE player SET value = " + val
						+ " WHERE pid = " + pid);
				updated = true;
			}
			rs.close();
			return updated;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String query7() {
		return "";
	}

	public boolean updateDB() {
		return false;
	}

}