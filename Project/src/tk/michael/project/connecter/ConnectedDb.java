package tk.michael.project.connecter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created By: Michael Risher
 * Date: 4/23/15
 * Time: 3:50 AM
 */
public class ConnectedDb {

	public ConnectedDb() {}

	public static boolean testConnection( String url, String username, String password ){
		try {
			Connection connection = DriverManager.getConnection( url, username, password );
			return connection.isValid( 10 );
		} catch ( SQLException e ) {
			return false;
		}
	}
}
