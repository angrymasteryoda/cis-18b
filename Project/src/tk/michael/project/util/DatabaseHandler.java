package tk.michael.project.util;

import com.michael.api.IO.IO;
import com.michael.api.json.JSONArray;
import com.michael.api.json.JSONObject;
import com.michael.api.security.AES;
import com.michael.api.swing.MSplashScreen;
import tk.michael.project.Main;
import tk.michael.project.db.DbUtils;
import tk.michael.project.db.MysqlDatabase;
import tk.michael.project.gui.ConnectedWindow;
import tk.michael.project.gui.MainWindow;

import java.awt.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 5:06 PM
 */
public class DatabaseHandler {

	private static ArrayList<Database> databases = new ArrayList<>();

	private static String outFileDir = "data";
	private static String outFileName = outFileDir + "/server.dat";

	public DatabaseHandler() {}

	public static void addDatabase( String name, String host, String port, String username, String password, String database ){
		Database db = new Database( name, host, port, username, password, database );

		databases.add( db );
		MainWindow.GetInstance().updateDatabase();
		save();
	}

	public static void editDatabase( Database db ) {
		getDatabase( db.getId() ).setName( db.getName() );
		getDatabase( db.getId() ).setHost( db.getHost() );
		getDatabase( db.getId() ).setPort( db.getPort() );
		getDatabase( db.getId() ).setUsername( db.getUsername() );
		getDatabase( db.getId() ).setPassword( db.getPassword() );
		getDatabase( db.getId() ).setDatabaseName( db.getDatabaseName() );

		IO.println( getDatabase( db.getId() ) );
		MainWindow.GetInstance().updateDatabase();
		save();
	}

	public static boolean deleteDatabase( UUID id ){
		for ( int i = 0; i < databases.size(); i++ ){
		    Database db = databases.get( i );
			if ( db.getId().equals( id ) ) {
				databases.remove( i );
				MainWindow.GetInstance().updateDatabase();
				save();
				return true;
			}
		}
		return false;
	}

	private static void serialize(){
		try {
			File testFile = new File( outFileName );
			if ( !testFile.exists() ) {
				testFile.getParentFile().mkdir();
				testFile.createNewFile();
			}
			FileOutputStream file = new FileOutputStream( outFileName );
			ObjectOutputStream out = new ObjectOutputStream( file );
			out.writeObject( databases );
			out.close();
			file.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public static void save(){
		Thread thread = new Thread(){
			public void run(){
				serialize();
				saveDatabasesToDb();
			}
		};
		thread.start();

	}

	@SuppressWarnings( "unchecked" )
	public static void load(){
		try {
			FileInputStream file = new FileInputStream( outFileName );
			ObjectInputStream in = new ObjectInputStream( file );
			databases = (ArrayList<Database>) in.readObject();
			in.close();
			file.close();
		} catch ( IOException e ){
			databases = new ArrayList<Database>();
		} catch ( ClassNotFoundException e ){
			e.printStackTrace();
			return;
		}

		if ( databases.size() == 0 ) {
			databases.add( new Database( "Localhost", "localhost", "3306", "root", "", "" ) );
		}
	}

	public static String getDatabasesAsString(){
		String result = "";
		for( Database db : databases ){
			result += db.toDbString() + ";";
		}
		return result;
	}

	public static String getDatabasesAsJSON(){
		JSONArray array = new JSONArray();
		for ( Database db : databases ) {
			array.put( db.toJSON() );
		}
		return array.toString();
	}

	public static void saveDatabasesToDb(){
		if ( Main.isLoggedIn() ) {
			MysqlDatabase db = new MysqlDatabase();
			Session session = Main.getLoginSession();
			if ( db.open() ) {
//				int find = db.numRows( DbUtils.prependTable( "user_database" ), "user='" + session.getUserId().toString() + "'" );
				int find = db.numRows( DbUtils.prependTable( "databases" ), "user='" + session.getUserId().toString() + "'" );
				if ( find == 0 ) {
					//create it
					PreparedStatement state = db.query( "INSERT INTO " + DbUtils.prependTable( "databases" ) + " (id,user,dbs) VALUES (?,?,?)" );

					try {
						state.setString( 1, UUID.randomUUID().toString() );
						state.setString( 2, Main.getLoginSession().getUserId().toString() );
						state.setString( 3, getDatabasesAsJSON() );

						find = state.executeUpdate();

						if ( find == 0 ) {
							IO.println( "Save dbs didn't save" );
						}
					} catch ( SQLException e ){
						e.printStackTrace();
					}
				}
				else if ( find == 1 ){
					PreparedStatement state = db.query( "UPDATE " + DbUtils.prependTable( "databases" ) + " SET dbs= ? WHERE user='" + session.getUserId().toString() + "'" );

					try {
						state.setString( 1, getDatabasesAsJSON() );

						find = state.executeUpdate();

						if ( find == 0 ) {
							IO.println( "Save dbs didn't save" );
						}
					} catch ( SQLException e ){
						e.printStackTrace();
					}
				}
			}
		}

	}

	public static void loadDatabasesFromDb(){
		if ( Main.isLoggedIn() ) {
			MysqlDatabase db = new MysqlDatabase();
			Session session = Main.getLoginSession();
			if ( db.open() ) {
				try {
					PreparedStatement statement = db.query( "SELECT dbs FROM " + DbUtils.prependTable( "databases" ) + " WHERE user=? LIMIT 1" );
					statement.setString( 1, session.getUserId().toString() );

					ResultSet resultSet = statement.executeQuery();
					if ( resultSet.next() ) {
						final String allDbs = resultSet.getString( 1 );
						JSONArray json = new JSONArray( allDbs );
						//clear databases
						databases = new ArrayList<>();
						for ( int i = 0; i < json.length(); i++ ) {
							JSONObject dbJson = (JSONObject) json.get( i );
							String password = dbJson.getString( "password" );
							try {
								password = AES.decrypt( password );
							} catch ( Exception ignore ){}
							Database database =  new Database(
								dbJson.getString( "name" ),
								dbJson.getString( "host" ),
								dbJson.getString( "port" ),
								dbJson.getString( "username" ),
								password,
								dbJson.getString( "dbName" )
							);

							databases.add( database );
						}

						MainWindow.GetInstance().updateDatabase();
						save();
					}
				} catch ( SQLException e ) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void openDatabase( final UUID id ){
		final MSplashScreen splash = new MSplashScreen( Main.class.getResource( "/loading.png" ), true, true, false, "", null, new Color( 0x00618A ), new Color( 0x002A3D ) );
		splash.setIndeterminate( true );
		splash.on();
		Thread thread = new Thread( ){
			public void run(){
				splash.setProgress( "Connecting to " + getDatabase( id ).getName() );
				ConnectedWindow connectedWindow = new ConnectedWindow( id );
				connectedWindow.display();
				splash.off();
			}
		};

		thread.start();
	}

	public static void reset(){
		databases = new ArrayList<>();
		databases.add( new Database( "Localhost", "localhost", "3306", "root", "", "" ) );
		MainWindow.GetInstance().updateDatabase();
		save();
	}

	public static Database getDatabase( UUID id ){
		for ( int i = 0; i < databases.size(); i++ ){
			if ( databases.get( i ).getId().equals( id ) ) {
				return databases.get( i );
			}
		}
		return null;
	}

	public static ArrayList<Database> getDatabases() {
		return databases;
	}

	public static void deleteAllDatabases(){
		databases.clear();
		MainWindow.GetInstance().updateDatabase();
		save();
	}

	public static void setOutFileDir( String outFileDir ) {
		DatabaseHandler.outFileDir = outFileDir;
		outFileName = outFileDir + "/server.dat";
	}
}
