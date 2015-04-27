package tk.michael.project;

import com.michael.api.IO.IO;
import tk.michael.project.gui.AddDatabase;
import tk.michael.project.gui.DirectoryChooser;
import tk.michael.project.gui.MainWindow;
import tk.michael.project.util.DatabaseHandler;

import javax.swing.*;
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

//		new Test();

		DatabaseHandler.load();
		MainWindow.GetInstance().display();
	}

	public static boolean isDebugView() {
		return debugView;
	}
}
