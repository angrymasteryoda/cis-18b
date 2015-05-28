package tk.michael.project.gui;

import com.thehowtotutorial.splashscreen.JSplash;
import net.miginfocom.swing.MigLayout;
import tk.michael.project.Main;
import tk.michael.project.util.Database;
import tk.michael.project.util.DatabaseHandler;

import javax.swing.*;
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
	private JPopupMenu menu;

	public DatabaseBox( Database db ) {
		this.menu = new JPopupMenu();
		this.id = db.getId();
		init( db.getName(), db.getHost() + ":" + db.getPort(), db.getUsername() );
	}

	public DatabaseBox( UUID id, String name, String host, String username ) {
		this.menu = new JPopupMenu();
		this.id = id;
		init( name, host, username );
	}

	public void init( String nameStr, String hostStr, String usernameStr ) {
		initMenu();
		panel = new JPanel( new MigLayout( "w 175px!, h 75px!" + ( Main.isDebugView() ? ",debug" : "" ) ) );

		panel.addMouseListener( this );

		name = new MLabel( nameStr );
		host = new MLabel( hostStr );
		username = new MLabel( usernameStr );
		edit = new JLabel( new ImageIcon( getClass().getResource( "/editDatabase.png" ) ) );
		edit.addMouseListener( this );
		edit.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		edit.setToolTipText( "Edit database" );

		remove = new JLabel( new ImageIcon( getClass().getResource( "/removeDatabase.png" ) ) );
		remove.addMouseListener( this );
		remove.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		remove.setToolTipText( "Delete database" );

		name.setBasicStyle();
		host.setBasicStyle();
		username.setBasicStyle();

		name.setForeground( new Color( 0xD2D2D2 ) );
		host.setForeground( new Color( 0xAEAEAE ) );
		username.setForeground( new Color( 0xAEAEAE ) );

		name.setFont( Font.BOLD, 13 );
		panel.setBackground( new Color( 0x2F2F2F ) );

		panel.add( this.name, "wmax 100px");
		panel.add( edit, "x 130, y 5, wmin 16px, hmin 16px, pad 3px 5px 3px 3px, split 2" );
		panel.add( remove, "x 150, y 5, wmin 16px, hmin 16px, pad 3px 5px 3px 3px, wrap" );
		panel.add( this.username, "wrap" );
		panel.add( this.host );

	}

	public void initMenu(){
		JMenuItem item = new JMenuItem("Open");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectedWindow connectedWindow = new ConnectedWindow( id );
				connectedWindow.display();
			}
		});
		menu.add(item);

		item = new JMenuItem("Edit");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EditDatabase( DatabaseHandler.getDatabase( id ) ).display();
			}
		});
		menu.add(item);

		item = new JMenuItem("Delete");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		menu.add(item);
	}

	@Override
	public void mouseClicked( MouseEvent e ) { }

	@Override
	public void mousePressed( MouseEvent e ) {}

	@Override
	public void mouseReleased( MouseEvent e ) {
		if ( e.isPopupTrigger() ) {
			menu.show( e.getComponent(), e.getX(), e.getY() );
		}

		if ( e.getSource() == edit ) {
			new EditDatabase( DatabaseHandler.getDatabase( id ) ).display();
		}

		if ( e.getSource() == remove ) {
			delete();
		}

		if ( e.getSource() == panel ) {
			if ( e.getClickCount() == 2 ) {
				//open to connected window
				ConnectedWindow connectedWindow = new ConnectedWindow( id );
				connectedWindow.display();
			}
		}
	}

	@Override
	public void mouseEntered( MouseEvent e ) {}

	@Override
	public void mouseExited( MouseEvent e ) {}

	private void delete(){
		int choice = JOptionPane.showConfirmDialog( null, "Are you sure you want to delete " + this.name.getText() + "?", "Delete", JOptionPane.YES_NO_OPTION );

		if ( choice == JOptionPane.YES_OPTION ) {
			if ( !DatabaseHandler.deleteDatabase( this.id ) ) {
				JOptionPane.showOptionDialog( null, "Failed to delete database", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null );
			}
		} else {
			return;
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
}
