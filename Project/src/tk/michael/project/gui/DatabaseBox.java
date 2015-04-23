package tk.michael.project.gui;

import com.michael.api.IO.IO;
import com.sun.corba.se.impl.protocol.NotLocalLocalCRDImpl;
import com.sun.deploy.net.proxy.RemoveCommentReader;
import net.miginfocom.swing.MigLayout;
import tk.michael.project.Main;
import tk.michael.project.util.DatabaseHandler;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.RequestingUserName;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 4:25 PM
 */
public class DatabaseBox implements MouseListener{
	private final UUID id;
	private JPanel panel;
	private MLabel name;
	private MLabel host;
	private MLabel username;

	private JLabel edit;
	private JLabel remove;

	public DatabaseBox( UUID id, String name, String host, String username ) {
		this.id = id;
		init( name, host, username );
	}

	public void init( String nameStr, String hostStr, String usernameStr ) {
		panel = new JPanel( new MigLayout( "w 175px!, h 75px!" + ( Main.isDebugView() ? ",debug" : "" ) ) );

		name = new MLabel( nameStr );
		host = new MLabel( hostStr );
		username = new MLabel( usernameStr );
		edit = new JLabel( new ImageIcon( getClass().getResource( "/editDatabase.png" ) ) );
		edit.addMouseListener( this );
		remove = new JLabel( new ImageIcon( getClass().getResource( "/removeDatabase.png" ) ) );
		remove.addMouseListener( this );

		name.setBasicStyle();
		host.setBasicStyle();
		username.setBasicStyle();

		name.setForeground( new Color( 0xD2D2D2 ) );
		host.setForeground( new Color( 0xD2D2D2 ) );
		username.setForeground( new Color( 0xD2D2D2 ) );

		name.setFont( Font.BOLD, 13 );
		panel.setBackground( new Color( 0x2F2F2F ) );

		panel.add( this.name, "wmax 100px");
		panel.add( edit, "x 130, y 5, wmin 16px, hmin 16px, pad 3px 5px 3px 3px, split 2" );
		panel.add( remove, "x 150, y 5, wmin 16px, hmin 16px, pad 3px 5px 3px 3px, wrap" );
		panel.add( this.host );
		panel.add( this.username );

	}

	@Override
	public void mouseClicked( MouseEvent e ) {
		if ( e.getSource() == edit ) {
			IO.println( "clicked edit" );
		}

		if ( e.getSource() == remove ) {
			int choice = JOptionPane.showConfirmDialog( null, "Are you sure you want to delete " + this.name.getText() + "?", "Delete", JOptionPane.YES_NO_OPTION );

			if ( choice == JOptionPane.YES_OPTION ) {
				if ( !DatabaseHandler.deleteDatabase( this.id ) ) {
					JOptionPane.showOptionDialog( null, "Failed to delete database", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null );
				}
			} else {
				return;
			}
		}
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void setHost( String str ) {
		this.host.setText( str );
	}

	public void setUsername( String str ) {
		this.username.setText( str );
	}

	public void setName( String str ) {
		this.name.setText( str );
	}

	public UUID getId() {
		return id;
	}

	@Override
	public void mousePressed( MouseEvent e ) {}

	@Override
	public void mouseReleased( MouseEvent e ) {}

	@Override
	public void mouseEntered( MouseEvent e ) {}

	@Override
	public void mouseExited( MouseEvent e ) {}
}
