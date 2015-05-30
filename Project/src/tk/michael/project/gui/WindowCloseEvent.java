package tk.michael.project.gui;

import tk.michael.project.Main;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 5/28/15
 * Time: 4:09 AM
 */
public class WindowCloseEvent extends WindowAdapter {
	private JFrame frame;
	private final UUID id;
	private boolean willCloseAll;

	public WindowCloseEvent( JFrame frame ) {
		this( frame, null, false );
	}

	public WindowCloseEvent( JFrame frame, UUID id ) {
		this( frame, id, false );
	}

	public WindowCloseEvent( JFrame frame, boolean willCloseAll ) {
		this( frame, null, willCloseAll );
	}

	public WindowCloseEvent( JFrame frame, UUID id, boolean willCloseAll ) {
		this.frame = frame;
		this.id = id;
		this.willCloseAll = willCloseAll;
	}

	public void windowClosing( WindowEvent e ){
		int choice = -1;
		if ( !Main.isArg( "dev" ) ) {
			if ( willCloseAll && WindowManager.windowsOpened() > 0 ) {
				choice = JOptionPane.showConfirmDialog( null, "Are you sure you want to close all open windows?", "Close Program", JOptionPane.YES_NO_OPTION );
			}
			else if ( !willCloseAll ) {
				choice = JOptionPane.showConfirmDialog( null, "Are you sure you want to close " + this.frame.getTitle() + "?", "Close Window", JOptionPane.YES_NO_OPTION );
			}
			else if ( willCloseAll ){
				choice = JOptionPane.YES_OPTION;
			}
		}

		if ( choice == JOptionPane.YES_OPTION || Main.isArg( "dev" ) ) {
			if ( id != null ) {
				WindowManager.close( id );
			}

			if ( willCloseAll ) {
				WindowManager.closeAll();
			}

			frame.dispose();
		}
		else {
			return;
		}

		//verify we did indeed close
		if ( willCloseAll ) {
			System.exit( 0 );
		}
	}
}
