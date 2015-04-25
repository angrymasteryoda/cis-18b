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
	private ArrayList<DatabaseBox> databaseBoxes = new ArrayList<>();

	private int dbWidth = 738; // starting db width

	protected MainWindow(){
		super( "Database Workbench" );
		init();
		instance = this;
		IO.println( dbPanel.getWidth() );
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
		JLabel addLabel = new JLabel( new ImageIcon( getClass().getResource( "/addDatabase.png" ) ) );
		addLabel.setToolTipText( "Add Database" );
		addLabel.addMouseListener( this );
		addLabel.setCursor( new Cursor( Cursor.HAND_CURSOR ) );

		topPanel.add( addLabel, "dock west, w 40!, pad 5 0 5 0" );
		topPanel.add( info, "dock west, pad 5 15 5 0, w 150! " );

		frame.add( topPanel, BorderLayout.NORTH );

		dbPanel = new JPanel( new MigLayout( width + ", h 600px" + ( Main.isDebugView() ? ",debug" : "" ) ) );
		dbPanel.setBackground( new Color( 0x777777 ) );

//		JPanel databox = new DatabaseBox( UUID.randomUUID(), "local mysql", "localhost", "root"  ).getPanel();

//		dbPanel.add( databox, BorderLayout.CENTER );

		updateDatabase();

		frame.add( dbPanel, BorderLayout.CENTER );

		frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
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
		MenuHelper.createMenuItem( database, MenuHelper.PLAIN, "Remove All Databases", "rmAllDbs", 0,"Remove all databases" );

		menuBar.add( file );
		menuBar.add( database );
		frame.setJMenuBar( menuBar );
	}

	public void updateDatabase(){
		ArrayList<Database> dbs = DatabaseHandler.getDatabases();
		int width = dbPanel.getWidth() == 0 ? dbWidth : dbPanel.getWidth();
		float limit = width / 184.5f;
		dbPanel.removeAll();
		for ( int i = 0; i < dbs.size(); i++ ){
			if ( i != 0 && i % ( (int) Math.floor( limit ) - 1 )== 0 ) {
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

	@Override
	public void mouseClicked( MouseEvent e ) {
		new AddDatabase().display();
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
		IO.println( dbPanel.getWidth() );
		updateDatabase();
	}

	@Override
	public void componentMoved( ComponentEvent e ) {}

	@Override
	public void componentShown( ComponentEvent e ) {}

	@Override
	public void componentHidden( ComponentEvent e ) {}
}

