package tk.michael.project.gui;

import com.michael.api.IO.IO;
import tk.michael.project.util.DatabaseHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

		if ( action.equals( "saveLoc" ) ) {
			DirectoryChooser dc = new DirectoryChooser( "Choose Save Location" );
			File file;
			if ( ( file = dc.display() ) != null ) {
				if ( file.isDirectory() ) {
					DatabaseHandler.setOutFileDir( file.toString() );
					DatabaseHandler.serialize();
				}
				else {
					JOptionPane.showMessageDialog( null, "Not a valid directory to choose from!" );
				}
			}
		}

		if ( action.equals( "loadDbs" ) ) {
			DirectoryChooser dc = new DirectoryChooser( "Choose Save Location" );
			File file;
			if ( ( file = dc.display() ) != null ) {
				if ( file.isDirectory() ) {
					DatabaseHandler.setOutFileDir( file.toString() );
					DatabaseHandler.load();
					MainWindow.GetInstance().updateDatabase();
				}
				else {
					JOptionPane.showMessageDialog( null, "Not a valid directory to choose from!" );
				}
			}
		}

		if ( action.equals( "rmAllDbs" ) ) {
			int conf = JOptionPane.showConfirmDialog( null, "Delete all database, you can not undo this?", "Delete all databases", JOptionPane.YES_NO_OPTION );
			if ( conf == JOptionPane.YES_OPTION ) {
				DatabaseHandler.deleteAllDatabases();
			}
		}
		IO.println( "menu action: "  + ( action.isEmpty() ? "" : action ) );
	}
}
