package tk.michael.project.gui;

import com.michael.api.IO.IO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 5:41 PM
 */
public class MenuActions implements ActionListener {

	private static MenuActions instance;

	@Override
	public void actionPerformed( ActionEvent e ) {
		String action = e.getActionCommand();
		if ( action.equals( "exitApp" ) ) {
			System.exit( 0 );
		}
		if ( action.equals( "addDatabase" ) ) {
			AddDatabase addDB = new AddDatabase();
			addDB.display();
		}
		IO.println( "menu action"  + ( action.isEmpty() ? "" : action ) );
	}
}
