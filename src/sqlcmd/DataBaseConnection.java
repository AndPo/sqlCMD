package sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConnection {
	public static void main(String[] args) {
		System.out.println("------- PostgreSQL JDBC Connection Testing --------------");

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("problem with a Postgre JDBCDriver!");
			e.printStackTrace();
			return;
		}
		System.out.println("Postrgre JDBC Driver registered!");

		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres",
					"postgres");
		} catch (SQLException e) {
			System.out.println("Connection failed!");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("You made it! Take control of your database now!");
		} else {
			System.out.println("Failed to make connection...");
		}

		DataBaseConnection dbconn = new DataBaseConnection();

		// dbconn.insertValues("users", "Roman", "Lupak", connection);

		// dbconn.tables(connection);

		// dbconn.clearTable("users4delete2", connection);

		// dbconn.dropTable("users4delete3", connection);

		// dbconn.createTable(connection, "New_Table", "first_column",
		// "secon_column", "third_column");

		// dbconn.find(connection, "users");

		// dbconn.insert(connection, "users", "Name", "Taras", "surname",
		// "Luka");
		// dbconn.insert(connection, "users", "Name", "Andriy", "surname",
		// "Baev");

		//dbconn.update(connection, "users", "surname", "Baev", "Name", "Andreyko");
		
		//dbconn.delete(connection, "users", "id", "28");
		
		//dbconn.help();
		
		//dbconn.exit(connection);
		
		

	}

	public void insertValues(String tableName, String name, String surname, Connection connection) {
		try {

			Statement statement = connection.createStatement();
			int res = statement
					.executeUpdate("insert into " + tableName + " values ('" + name + "', '" + surname + "');");
			System.out.println("inserted into table USERS");
		} catch (SQLException e) {
			System.out.println("not inserted into table USERS");
			e.printStackTrace();
		}
	}

	public void tables(Connection connection) {
		try {
			Statement statement = connection.createStatement();
			ResultSet rs;
			rs = statement
					.executeQuery("SELECT table_name FROM INFORMATION_SCHEMA.TABLES where table_schema = 'public';");
			while (rs.next()) {
				System.out.println("[ " + rs.getString("table_name") + " ] ");
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong ");
			e.printStackTrace();
		}
	}

	public void clearTable(String tableName, Connection conn) {
		try {
			Statement statement = conn.createStatement();
			int res = statement.executeUpdate("truncate " + tableName + ";");
			System.out.println("all has been deleted from table " + tableName);
		} catch (SQLException e) {
			System.out.println("Hasn't been deleted from table " + tableName);
			e.printStackTrace();
		}
	}

	public void dropTable(String tableName, Connection conn) {
		try {
			Statement statement = conn.createStatement();
			int res = statement.executeUpdate("drop table " + tableName + ";");
			System.out.println("Table " + tableName + " has been dropped");
		} catch (SQLException e) {
			System.out.println("Table " + tableName + " has not been deleted");
			e.printStackTrace();
		}
	}

	public void createTable(Connection conn, String tableName, String... columnName) {
		try {
			String resultColumns = "";
			for (String str : columnName) {
				resultColumns += str;
				resultColumns += " text,";
			}
			resultColumns = resultColumns.substring(0, resultColumns.length() - 1);
			Statement statement = conn.createStatement();
			int rest = statement.executeUpdate("Create table " + tableName + " (" + resultColumns + " );");
			System.out.println("Table " + tableName + " created!");
		} catch (SQLException e) {
			System.out.println("Table " + tableName + " isn't created");
			e.printStackTrace();
		}
	}

	public void find(Connection conn, String tableName) {
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT column_name FROM information_schema.columns where table_name='" + tableName
							+ "' and table_schema='public';");
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			System.out.println("-------------------------------------------------");
			System.out.print("|");
			while (rs.next()) {

				for (int i = 1; i <= columnsNumber; i++) {
					System.out.format("%14s", rs.getString(i) + "	|");
				}
			}
			System.out.println();
			System.out.println("-------------------------------------------------");
			rs = statement.executeQuery("select * from " + tableName + ";");
			columnsNumber = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				System.out.print("|");
				for (int i = 1; i <= columnsNumber; i++) {

					System.out.format("%14s", rs.getString(i) + "	|");
				}
				System.out.println();
			}
			System.out.println("-------------------------------------------------");

		} catch (SQLException e) {
			System.out.println("there is no such table");
			e.printStackTrace();
		}
	}

	public void insert(Connection conn, String table_name, String... values) {
		try {
			String columnNames = "";
			String valuesNames = "";
			for (int i = 0; i < values.length; i++) {
				columnNames += "\"" + values[i++] + "\", ";
				valuesNames += "'" + values[i] + "', ";
			}
			columnNames = columnNames.substring(0, columnNames.length() - 2);
			valuesNames = valuesNames.substring(0, valuesNames.length() - 2);
			Statement statement = conn.createStatement();
			statement
					.executeUpdate("insert into " + table_name + " (" + columnNames + ") values(" + valuesNames + ");");
			System.out.println("added successfuly into table " + table_name);
		} catch (SQLException e) {
			System.out.println("not added");
			e.printStackTrace();
		}
	}

	public void update(Connection conn, String tableName, String checkingColumn, String checkingValue,
			String setingColumn, String setingValue) {
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate("update " + tableName + " set \"" + setingColumn + "\"='" + setingValue
					+ "' where \"" + checkingColumn + "\"='" + checkingValue + "';");

		} catch (SQLException e) {
			System.out.println("not updated:(...");
			e.printStackTrace();
		}
	}

	public void delete(Connection conn, String tableName, String column, String value) {
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate("delete from " + tableName + " where \"" + column + "\" = \'" + value + "\';");
			System.out.println("value " + value + " was deleted from " + tableName);
		} catch (SQLException e) {
			System.out.println("value was not deleted:(...");
			e.printStackTrace();
		}

	}
	
	public void exit(Connection conn){
		try {
			conn.close();
			System.out.println("connection closed!");
		} catch (SQLException e) {
			System.out.println("connection was not closed:(...");
			e.printStackTrace();
		}
	}
}
