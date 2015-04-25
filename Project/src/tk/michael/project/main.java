package tk.michael.project;

import tk.michael.project.gui.AddDatabase;
import tk.michael.project.gui.DirectoryChooser;
import tk.michael.project.gui.MainWindow;
import tk.michael.project.util.DatabaseHandler;

import java.util.Arrays;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 4:02 PM
 */
public class Main {

	private static boolean debugView  = false;

	public static void main( String[] args ){
		if ( Arrays.asList( args ).contains( "debugview".toLowerCase() ) ) {
			debugView = true;
		}
		DatabaseHandler.load();
//		new DirectoryChooser( "title" );
		MainWindow.GetInstance().display();
//		new AddDatabase().display();
	}

	public static boolean isDebugView() {
		return debugView;
	}
}
