package tk.michael.project;

import tk.michael.project.gui.AddDatabase;
import tk.michael.project.gui.MainWindow;
import tk.michael.project.util.DatabaseHandler;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 4:02 PM
 */
public class Main {

	public static void main( String[] args ){
		MainWindow.GetInstance().display();
		DatabaseHandler.load();
//		new AddDatabase().display();
	}
}
