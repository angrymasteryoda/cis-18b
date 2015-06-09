package tk.michael.project.gui;

import com.michael.api.IO.IO;
import net.miginfocom.swing.MigLayout;
import tk.michael.project.Main;
import tk.michael.project.util.Database;
import tk.michael.project.util.DatabaseHandler;

import javax.activation.DataHandler;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 4:02 PM
 */
public class MainWindow extends BasicFrameObject implements MouseListener, ComponentListener{

	private static MainWindow instance = null;
	private JPanel dbPanel;
	private JLabel addLabel;
	private JLabel loginLabel;
	private JLabel loginLabelText;
	private ImageIcon loginIcon = new ImageIcon( getClass().getResource( "/login.png" ) );
	private ImageIcon logoutIcon = new ImageIcon( getClass().getResource( "/logout.png" ) );
	private ArrayList<DatabaseBox> databaseBoxes = new ArrayList<>();

	private int dbWidth = 738; // starting db width

	protected MainWindow(){
		super( "Database Workbench" );
		init();
		instance = this;
	}

	public static MainWindow GetInstance(){
		if( instance == null) {
			instance = new MainWindow();
		}
		return instance;
	}

	public void display(){
		frame.setVisible( true );
	}

	public JFrame getFrame(){
		return this.frame;
	}

	public void init(){
		String width = "w " + dbWidth + "px!";
		initMenu();
		frame.setLayout( new BorderLayout(  ) );
		frame.addComponentListener( this );

		JPanel topPanel = new JPanel( new MigLayout( width + ", h 35px!" + ( Main.isDebugView() ? ",debug" : "" ) ) );
		topPanel.setBackground( new Color( 0x222222 ) );
		JLabel info = new JLabel( "Your Mysql Servers" );
		info.setForeground( Color.WHITE );
		addLabel = new JLabel( new ImageIcon( getClass().getResource( "/addDatabase.png" ) ) );
		addLabel.setToolTipText( "Add Database" );
		addLabel.addMouseListener( this );
		addLabel.setCursor( new Cursor( Cursor.HAND_CURSOR ) );

		topPanel.add( addLabel, "dock west, w 40!, pad 5 0 5 0" );
		topPanel.add( info, "dock west, pad 5 15 5 0, w 150! " );

		frame.add( topPanel, BorderLayout.NORTH );

		//db panel
		dbPanel = new JPanel( new MigLayout( width + ", h 600px" + ( Main.isDebugView() ? ",debug" : "" ) ) );
		dbPanel.setBackground( new Color( 0x777777 ) );
		updateDatabase();
		frame.add( dbPanel, BorderLayout.CENTER );

		//
		JPanel loginPanel = new JPanel( new MigLayout( width + ", h 35px!" + ( Main.isDebugView() ? ",debug" : "" ) ) );
		loginPanel.setBackground( new Color( 0x222222 ) );
		loginLabelText = new JLabel( "Log into the cloud" );
		loginLabelText.setForeground( Color.WHITE );
		loginLabel = new JLabel( loginIcon );
		loginLabel.setToolTipText( "Log into the cloud" );
		loginLabel.addMouseListener( this );
		loginLabel.setCursor( new Cursor( Cursor.HAND_CURSOR ) );

		loginPanel.add( loginLabel, "dock west, w 40!, pad 5 0 5 0" );
		loginPanel.add( loginLabelText, "dock west, pad 5 15 5 0, w 350! " );

		frame.add( loginPanel, BorderLayout.SOUTH );

		frame.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
		frame.addWindowListener( new WindowCloseEvent( frame, true ) );
		frame.pack();
		frame.setLocationRelativeTo( null );
	}


	public void initMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu( "File" );
		file.setMnemonic( 'F' );

		MenuHelper.createMenuItem( file, MenuHelper.PLAIN, "Load", "loadDbs", 'L', "Load from custom location" );
		MenuHelper.createMenuItem( file, MenuHelper.PLAIN, "Set Save Location", "saveLoc", 0, "Sets the default save location" );
		MenuHelper.createMenuItem( file, MenuHelper.PLAIN, "Exit", "exitApp", 'X', "Close the application" );

		JMenu database = new JMenu( "Database" );
		file.setMnemonic( 'd' );
		MenuHelper.createMenuItem( database, MenuHelper.PLAIN, "Add Database", "addDatabase", 0,"Add a database" );
		MenuHelper.createMenuItem( database, MenuHelper.PLAIN, "Open Database", "openDatabase", 0,"Open a database" );
		MenuHelper.createMenuItem( database, MenuHelper.PLAIN, "Remove Database", "rmDatabase", 0,"Remove a database" );
		MenuHelper.createMenuItem( database, MenuHelper.PLAIN, "Remove All Databases", "rmAllDbs", 0,"Remove all databases" );

		menuBar.add( file );
		menuBar.add( database );
		frame.setJMenuBar( menuBar );
	}

	public void updateDatabase(){
		ArrayList<Database> dbs = DatabaseHandler.getDatabases();
		int width = dbPanel.getWidth() == 0 ? dbWidth : dbPanel.getWidth();
		int limit = (int) Math.floor( width / 184.5f );
		dbPanel.removeAll();
		for ( int i = 0; i < dbs.size(); i++ ){
			if ( ( i % limit ) + 1 == limit ) {
				dbPanel.add( new DatabaseBox( dbs.get( i ) ).getPanel(), "wrap" );
			}
			else {
				dbPanel.add( new DatabaseBox( dbs.get( i ) ).getPanel() );
			}
		}
		dbPanel.repaint();
		frame.validate();
	}
//184.5

	public void login( boolean state ){
		if ( state ) {
			//we logged in
			loginLabelText.setText( "Logged in as, " + Main.getLoginSession().getUsername() );
			loginLabel.validate();
			loginLabel.setIcon( logoutIcon );
			loginLabel.setToolTipText( "Log out" );
		} else{
			loginLabelText.setText( "Log into the cloud"  );
			loginLabel.validate();
			loginLabel.setIcon( loginIcon );
			loginLabel.setToolTipText( "Log into the cloud" );
		}
	}

	@Override
	public void mouseClicked( MouseEvent e ) {
			if ( e.getSource().equals( addLabel ) ) {
			new AddDatabase().display();
		}

		if ( e.getSource().equals( loginLabel ) ) {
			if ( Main.isLoggedIn() ) {
				int choice = JOptionPane.showConfirmDialog( null, "Are you sure you want to sign out?", "Logout?", JOptionPane.YES_NO_OPTION );

				if ( choice == JOptionPane.YES_OPTION ) {
					Main.logout();
				} else {
					return;
				}
			} else{
				new LoginDialog( frame, "Login" ).display();
			}
		}
	}

	@Override
	public void mousePressed( MouseEvent e ) {}

	@Override
	public void mouseReleased( MouseEvent e ) {}

	@Override
	public void mouseEntered( MouseEvent e ) {}

	@Override
	public void mouseExited( MouseEvent e ) {}

	@Override
	public void componentResized( ComponentEvent e ) {
		updateDatabase();
	}

	@Override
	public void componentMoved( ComponentEvent e ) {}

	@Override
	public void componentShown( ComponentEvent e ) {}

	@Override
	public void componentHidden( ComponentEvent e ) {}
}

