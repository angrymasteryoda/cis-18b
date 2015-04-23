package tk.michael.project.util;

import tk.michael.project.gui.MainWindow;

import java.io.*;
import java.util.ArrayList;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 5:06 PM
 */
public class DatabaseHandler {

	private static ArrayList<Database> databases = new ArrayList<>();

	private static final String OUT_FILE_NAME = "data/server.dat";

	public DatabaseHandler() {}

	public static void addDatabase( String name, String host, String port, String username, String password ){
		Database db = new Database( name, host, port, username, password );

		databases.add( db );
		MainWindow.GetInstance().addDatabase( db );
		serialize();
	}

	private static void serialize(){
		try {
			File testFile = new File( OUT_FILE_NAME );
			if ( !testFile.exists() ) {
				testFile.getParentFile().mkdir();
				testFile.createNewFile();
			}
			FileOutputStream file = new FileOutputStream( OUT_FILE_NAME );
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
			FileInputStream file = new FileInputStream( OUT_FILE_NAME );
			ObjectInputStream in = new ObjectInputStream( file );
			databases = (ArrayList<Database>) in.readObject();
			in.close();
			file.close();
		} catch ( IOException e ){
			databases = new ArrayList<Database>();
			return;
		} catch ( ClassNotFoundException e ){
			e.printStackTrace();
			return;
		}

		MainWindow mainWindow = MainWindow.GetInstance();
		for( Database db : databases ){
			mainWindow.addDatabase( db );
		}
	}

	/*

      {
         FileInputStream fileIn = new FileInputStream("/tmp/employee.ser");
         ObjectInputStream in = new ObjectInputStream(fileIn);
         e = (Employee) in.readObject();
         in.close();
         fileIn.close();
      }catch(IOException i)
      {
         i.printStackTrace();
         return;
      }catch(ClassNotFoundException c)
      {
         System.out.println("Employee class not found");
         c.printStackTrace();
         return;
      }
	 */
}
