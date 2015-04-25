package tk.michael.project.util;

import com.michael.api.IO.IO;
import tk.michael.project.gui.MainWindow;

import java.io.*;
import java.util.ArrayList;
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
		serialize();
	}

	public static void editDatabase( Database db ) {
		getDabase( db.getId() ).setName( db.getName() );
		getDabase( db.getId() ).setHost( db.getHost() );
		getDabase( db.getId() ).setPort( db.getPort() );
		getDabase( db.getId() ).setUsername( db.getUsername() );
		getDabase( db.getId() ).setPassword( db.getPassword() );
		getDabase( db.getId() ).setDatabaseName( db.getDatabaseName() );

		IO.println( getDabase( db.getId() ) );
		MainWindow.GetInstance().updateDatabase();
		serialize();
	}

	public static boolean deleteDatabase( UUID id ){
		for ( int i = 0; i < databases.size(); i++ ){
		    Database db = databases.get( i );
			if ( db.getId().equals( id ) ) {
				databases.remove( i );
				MainWindow.GetInstance().updateDatabase();
				serialize();
				return true;
			}
		}
		return false;
	}

	public static void serialize(){
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

	public static Database getDabase( UUID id ){
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
		serialize();
	}

	public static void setOutFileDir( String outFileDir ) {
		DatabaseHandler.outFileDir = outFileDir;
		outFileName = outFileDir + "/server.dat";
	}
}
