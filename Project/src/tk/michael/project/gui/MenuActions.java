package tk.michael.project.gui;

import com.michael.api.IO.IO;
import tk.michael.project.util.Database;
import tk.michael.project.util.DatabaseHandler;
import tk.michael.project.util.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

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
					DatabaseHandler.save();
				} else {
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
				} else {
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

		if ( action.equals( "openDatabase" ) ) {
			ArrayList<Database> dbs = DatabaseHandler.getDatabases();
			UUID[] uuids = new UUID[ dbs.size() ];
			String[] names = new String[ dbs.size() ];

			for ( int i = 0; i < dbs.size(); i++ ) {
				uuids[ i ] = dbs.get( i ).getId();
				names[ i ] = dbs.get( i ).getName();
			}

			int[] duplicates = Util.getDuplicatesFromStrings( names );

			for ( int i = 0; i < duplicates.length; i++ ) {
				names[ duplicates[ i ] ] += appendSpaces( duplicates[ i ] );
			}

			String input = (String) JOptionPane.showInputDialog( null, "Select a database to open.",
				"Delete a Database", JOptionPane.PLAIN_MESSAGE, null, names, names[ 0 ] );

			//If a string was returned, say so.
			if ( ( input != null ) && ( input.length() > 0 ) ) {
				int id = -1;
				for ( int i = 0; i < names.length; i++ ) {
					if ( input.equals( names[ i ] ) ) {
						id = i;
						break;
					}
				}
				if ( id != -1 ) {
					ConnectedWindow cw = new ConnectedWindow( uuids[id] );
					cw.display();
				}
			}
		}

		if ( action.equals( "rmDatabase" ) ) {
			ArrayList<Database> dbs = DatabaseHandler.getDatabases();
			UUID[] uuids = new UUID[ dbs.size() ];
			String[] names = new String[ dbs.size() ];

			for ( int i = 0; i < dbs.size(); i++ ) {
				uuids[ i ] = dbs.get( i ).getId();
				names[ i ] = dbs.get( i ).getName();
			}

			int[] duplicates = Util.getDuplicatesFromStrings( names );

			for ( int i = 0; i < duplicates.length; i++ ) {
				names[ duplicates[ i ] ] += appendSpaces( duplicates[ i ] );
			}

			String input = (String) JOptionPane.showInputDialog( null, "Select a database to delete.\nThis can not be undone.",
				"Delete a Database", JOptionPane.PLAIN_MESSAGE, null, names, names[ 0 ] );

			//If a string was returned, say so.
			if ( ( input != null ) && ( input.length() > 0 ) ) {
				int id = -1;
				for ( int i = 0; i < names.length; i++ ) {
					if ( input.equals( names[ i ] ) ) {
						id = i;
						break;
					}
				}
				if ( id != -1 ) {
					DatabaseHandler.deleteDatabase( uuids[id] );
				}
			}
		}

		if ( action.equals( "openMainWindow" ) ) {
			MainWindow.GetInstance().display();
		}

		IO.println( "menu action: " + ( action.isEmpty() ? "" : action ) );
	}

	/**
	 * bug fix for duplicates
	 *
	 * @param amt int
	 * @return string to append
	 */
	private String appendSpaces( int amt ) {
		String s = "";
		for ( int i = 0; i < amt; i++ ) {
			s += " ";
		}
		return s;
	}
}
