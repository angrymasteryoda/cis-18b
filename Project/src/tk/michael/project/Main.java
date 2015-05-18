package tk.michael.project;

import com.michael.api.IO.IO;
import com.michael.api.security.AES;
import tk.michael.project.gui.AddDatabase;
import tk.michael.project.gui.ConnectedWindow;
import tk.michael.project.gui.DirectoryChooser;
import tk.michael.project.gui.MainWindow;
import tk.michael.project.util.DatabaseHandler;
import tk.michael.project.util.Session;

import javax.swing.*;
import java.util.Arrays;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 4:02 PM
 */
public class Main {

	private static Session loginSession;
	private static boolean isLoggedIn = false;

	private static boolean debugView  = false;
	private static String[] arugments;

	public static void main( String[] args ) throws Exception{
		arugments = args;
		if ( Arrays.asList( args ).contains( "debug".toLowerCase() ) ) {
			debugView = true;
		}

		try {
			for ( UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() ){
				if ( "Nimbus".equals( info.getName() ) ) {
					UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		}
		catch ( Exception e) { }

		DatabaseHandler.load();
		Session.load();
//		ConnectedWindow cw = new ConnectedWindow( DatabaseHandler.getDatabases().get( 0 ).getId() );
//		cw.display();
		MainWindow.GetInstance().display();

	}

	public static boolean isDebugView() {
		return debugView;
	}

	public static void login( Session session ){
		loginSession = session;
		isLoggedIn = true;
		MainWindow.GetInstance().login( isLoggedIn );
		DatabaseHandler.loadDatabasesFromDb();
	}

	public static void logout(){
		loginSession.expire();
		isLoggedIn = false;
		MainWindow.GetInstance().login( isLoggedIn );
		loginSession = null;
		DatabaseHandler.reset();
	}

	public static Session getLoginSession() {
		return loginSession;
	}

	public static void setLoginSession( Session loginSession ) {
		Main.loginSession = loginSession;
	}

	public static boolean isLoggedIn() {
		return isLoggedIn;
	}

	public static boolean isArg( String str ){
		return Arrays.asList( arugments ).contains( str );
	}
}
