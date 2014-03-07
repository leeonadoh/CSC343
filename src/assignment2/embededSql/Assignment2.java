package assignment2.embededSql; //TODO delete before handing in.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

	// Inserts row into the country table
	public boolean insertCountry(int cid, String name, String coach) {
		try {
			String sqlText = ("INSERT INTO country "
					+ "VALUES (" + cid + ", '" + name + "', '" + coach + "')");
			System.out.println("Executing command: " + sqlText);
			connectStatement();
			sql.executeUpdate(sqlText);
			releaseResources();
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
			connectStatement();
			rs = sql.executeQuery(sqlText);
			if (rs.first())
				result = rs.getInt("cnt");
			else
				result = -1;
			releaseResources();
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
			connectStatement();
			rs = sql.executeQuery(sqlText);
			if (rs.first())
				result = (rs.getString("fname") + ":" + rs.getString("lname")
						+ ":" + rs.getString("position") + ":" + rs
						.getString("goals"));
			else
				result = "";
			releaseResources();
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
			connectStatement();
			int updated = sql.executeUpdate(sqlText);
			System.out.println(updated + " rows were updated.");
			releaseResources();
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
			connectStatement();
			int updated = sql.executeUpdate(sqlText);
			System.out.println(updated + " rows were updated.");
			releaseResources();
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
			connectStatement();
			rs = sql.executeQuery(sqlText);
			String result = "";
			while (rs.next()) {
				// Parse all the rows.
				result += (rs.getString("fn") + ":" + rs.getString("ln") + ":"
						+ rs.getString("po") + ":" + rs.getString("go") + ":"
						+ rs.getString("cn") + "#");
			}
			releaseResources();
			// Strip last pound sign.
			return result.isEmpty() ? result : result.substring(0,
					result.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean updateValues(String cname, int incrV) {
		try {
			boolean updated = false;
			String sqlText = ("SELECT p.pid AS pid, p.value AS val "
					+ "FROM player AS p, country AS c "
					+ "WHERE p.cid = c.cid AND c.name = '" + cname + "'");

			System.out.println("Executing command: " + sqlText);
			connectStatement();
			rs = sql.executeQuery(sqlText);
			while (rs.next()) {
				int pid = rs.getInt("pid");
				int val = rs.getInt("val") + incrV;
				sql.executeUpdate("UPDATE player SET value = " + val
						+ " WHERE pid = " + pid);
				updated = true;
			}
			releaseResources();
			return updated;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String query7() {
		try {
			String view1 = "CREATE VIEW LowestBudgetTeam AS "
					+ "Select cid, budget "
					+ "FROM CountryBudget "
					+ "WHERE budget = (SELECT min(budget) from CountryBudget)";

			String view1Drop = "DROP VIEW LowestBudgetTeam";

			String view2 = "CREATE VIEW TopScorer AS "
					+ "SELECT pid, cid "
					+ "FROM player "
					+ "WHERE goals = (SELECT max(goals) FROM player)";

			String view2Drop = "DROP VIEW TopScorer";

			String query = "SELECT c.name as name, c.coach as coach, cb.budget as budget "
					+ "FROM Country as c, LowestBudgetTeam as cb, TopScorer as ts "
					+ "WHERE c.cid = cb.cid AND cb.cid = ts.cid";

			connectStatement();
			System.out.println("Creating View: " + view1);
			sql.executeUpdate(view1);
			System.out.println("Creating View: " + view2);
			sql.executeUpdate(view2);
			System.out.println("Executing Query: " + query);
			rs = sql.executeQuery(query);
			String result = "";
			while (rs.next()) {
				result += (rs.getString("name") + ":" + rs.getString("coach")
						+ ":" + rs.getString("budget") + "#");
			}
			sql.executeUpdate(view1Drop);
			sql.executeUpdate(view2Drop);
			releaseResources();
			// Strip last pound sign.
			return result.isEmpty() ? result : result.substring(0,
					result.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean updateDB() {
		try {
			String query = "SELECT DISTINCT p.pid AS pid, p.lname AS ln"
					+ "FROM player AS p, appearance AS a "
					+ "WHERE p.pid = a.pid AND a.minutes = 90 "
					+ "ORDER BY pid";
			System.out.println("Executing Query: " + query);
			connectStatement();
			rs = sql.executeQuery(query);

			System.out.println("Creating table valueablePlayers");
			String create = "CREATE TABLE valuablePlayers (pid INTEGER, lname VARCHAR(20))";
			sql.executeUpdate(create);

			String insert = "INSERT INTO valuablePlayers VALUES (?,?)";
			System.out.println("Prepared Statement: " + insert);
			ps = connection.prepareStatement(insert);
			while(rs.next()){
				// Populate prepared statement.
				ps.setInt(1, rs.getInt("pid"));
				ps.setString(2, rs.getString("ln"));
				// Execute insert.
				ps.executeUpdate();
			}
			releaseResources();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void releaseResources() throws SQLException {
		if (!sql.isClosed())
			sql.close();
		if (!rs.isClosed())
			rs.close();
		if (!ps.isClosed())
			ps.close();
	}

	private void connectStatement() throws SQLException {
		sql = connection.createStatement();
	}

}