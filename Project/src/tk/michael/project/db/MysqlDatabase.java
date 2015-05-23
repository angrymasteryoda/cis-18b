package tk.michael.project.db;

import com.michael.api.IO.IO;
import com.michael.api.security.AES;
import tk.michael.project.util.Database;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created By: Michael Risher
 * Date: 5/6/15
 * Time: 4:01 PM
 */
public class MysqlDatabase {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private boolean isConnected = false;
	private PreparedStatement preparedStatement = null;
	private String url;
	private String user;
	private String password;

	/**
	 * load with manual input
	 * @param url connection url
	 * @param user username of db
	 * @param password password of the db
	 */
	public MysqlDatabase( String url, String user, String password ) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * Load a users database
	 * @param db database object
	 */
	public MysqlDatabase( Database db ) {
		this.url = db.getUrl();
		this.user = db.getUsername();
		this.password = db.getPassword();
	}

	/**
	 * load the internal db from the file
	 */
	public MysqlDatabase() {
		try{
			Properties props = new Properties(  );
			props.load( this.getClass().getResourceAsStream( "/database.props" ) );
			String mode = props.getProperty( "mode", "local" );
			this.url = props.getProperty( "db." + mode + ".url" );
			this.user = props.getProperty( "db." + mode + ".user" );
			try {
				this.password = AES.decrypt( props.getProperty( "db." + mode + ".password" ) );
			} catch ( Exception e ){
				this.password = props.getProperty( "db." + mode + ".password" );
			}
		} catch( IOException e ){
			e.printStackTrace();
		}
	}

	/**
	 * Test if the connection is valid
	 * @param url url for database
	 * @param username username for database
	 * @param password password for database
	 * @return true if successful
	 */
	public static ConnectionStatus testConnection( String url, String username, String password ){
		try {
			Connection connection = DriverManager.getConnection( url, username, password );
			return new ConnectionStatus( connection.isValid( 5 ) );
		} catch ( SQLException e ) {
			return new ConnectionStatus( e );
		}
	}

	/**
	 * Tet if the connection is valid
	 * @param db Database onject
	 * @return true if successful
	 */
	public static ConnectionStatus testConnection( Database db ){
		try {
			Connection connection = DriverManager.getConnection( db.getUrl(), db.getUsername(), db.getPassword() );
			return new ConnectionStatus( connection.isValid( 5 ) );
		} catch ( SQLException e ) {
			return new ConnectionStatus( e );
		}
	}

	/**
	 * open the connection
	 * @return true if connected
	 */
	public boolean open(){
		try {
			connection = DriverManager.getConnection( url, user, password );
			statement = connection.createStatement();
			resultSet = statement.executeQuery( "SELECT VERSION()" );
			if ( resultSet.next() ) {
				isConnected = true;
				//todo log stuff
			}
		} catch ( SQLException e ){
			IO.println( "Failed to open connection" );
			e.printStackTrace();
		}

		return isConnected;
	}

	/**
	 * will close the connection
	 */
	public void close(){
		try {
			if ( resultSet != null ) {
				resultSet.close();
				resultSet = null;
			}
			if ( statement != null ) {
				statement.close();
				statement = null;
			}
			if ( connection != null ) {
				connection.close();
				connection = null;
				isConnected = false;
			}
		} catch ( SQLException ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Creates a prepared MySQL statement
	 * <p/>
	 * To create a proper prepared statement your query should use ? in
	 * place of actual values. You will then need to use the appropriate
	 * .set on the return value prep to change the question marks to their
	 * actual values before executing the query.
	 * <p/>
	 * If you do not plan on using .set on the returned value and instead
	 * want to immediately execute it, you may send a normal query.
	 *
	 * @param query A prepared SQL query with ? in place of actual values. Normal queries are still accepted though.
	 * @return
	 */
	public PreparedStatement query( String query ) {
		if ( isConnected == true ) {
			try {
				preparedStatement = connection.prepareStatement( query );
				return preparedStatement;
			} catch ( SQLException ex ) {
				ex.printStackTrace();
			}
		} else {
			return null;
			// LOG ERROR, not connected
		}
		return null;
	}

	/**
	 * Check if a tables exists
	 *
	 * @param table name of the table to check
	 * @return boolean
	 */
	public boolean tableExists( String table ) {
		if ( isConnected == true ) {
			try {
				DatabaseMetaData meta = connection.getMetaData();
				resultSet = meta.getTables( null, null, table, null );
				if ( resultSet.next() ) {
					resultSet.close();
					return true;
				} else {
					resultSet.close();
					return false;
				}
			} catch ( SQLException ex ) {
				ex.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * Create a table
	 *
	 * @param table table name
	 * @param query string of table create query
	 * @param check boolean to check if table exists first
	 * @return true if created
	 */
	public boolean createTable( String table, String query, boolean check ) {
		if ( isConnected == true ) {
			if ( check == true ) {
				if ( tableExists( table ) ) {
					System.out.println( "MySQL Notice: Table not created, table already exists" );
					return false;
				}
			}
			try {
				statement = connection.createStatement();
				statement.executeUpdate( query );
				statement.executeUpdate( "FLUSH TABLES" );
				return true;
			} catch ( SQLException ex ) {
				ex.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * Count the amount of expected rows returned from a MySQL query
	 * <p/>
	 * This function should only be called before you run a query where you will need to know
	 * the specific amount of rows being returned or to check if simple
	 * information exists in the database already, such as a username.
	 * <p/>
	 * This function is a platform and environment independent solution but can be expensive on large queries.
	 *
	 * @param table The table to look at in the database. If this is the only parameter it will count all rows in the table.
	 * @param par   This string is used as the SQL's WHERE clause. Format is: "color = '"+colorAnswer+"' AND age = '"+userAge+"'"
	 * @return
	 */
	public int numRows( String table, String par ) {
		if ( isConnected == true ) {
			try {
				if ( par.length() <= 0 ) {
					preparedStatement = connection.prepareStatement( "SELECT COUNT(*) FROM " + table );
				} else {
					preparedStatement = connection.prepareStatement( "SELECT COUNT(*) FROM " + table + " WHERE " + par );
				}
				resultSet = preparedStatement.executeQuery();
				resultSet.first();
				return resultSet.getInt( 1 );
			} catch ( SQLException ex ) {
				System.out.println( "MySQL Error: [Severe] " + ex );
			}
		} else {
			System.out.println( "MySQL Error: Can not count rows, no database connection." );
			return 0;
		}
		return 0;
	}

	public Connection getConnection() {
		return connection;
	}

	public Statement getStatement() {
		return statement;
	}

	public Statement getNewStatement(){
		try {
			statement = connection.createStatement();
		} catch ( SQLException e ) {
			return null;
		}
		return statement;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}

