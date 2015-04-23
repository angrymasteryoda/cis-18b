package tk.michael.project.gui;

import net.miginfocom.swing.MigLayout;
import tk.michael.project.util.Database;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 4:02 PM
 */
public class MainWindow extends BasicFrameObject{

	private static MainWindow instance = null;
	private JPanel panel;
	private ArrayList<DatabaseBox> databaseBoxes = new ArrayList<>();

	protected MainWindow(){
		super( "title" );
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
		initMenu();

		panel = new JPanel( new MigLayout( "w 800px, h 600px" ) );
		panel.setBackground( new Color( 0x777777 ) );

		JPanel databox = new DatabaseBox( "local mysql", "localhost", "root"  ).getPanel();

		panel.add( databox );

		frame.add( panel );

		frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		frame.pack();
	}

	public void initMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu( "File" );
		file.setMnemonic( 'F' );
		JMenuItem exit = MenuHelper.createMenuItem( file, MenuHelper.PLAIN, "Exit", "exitApp", 'X', "Close the application" );

		JMenu database = new JMenu( "Database" );
		file.setMnemonic( 'd' );
		JMenuItem addDatabase = MenuHelper.createMenuItem( database, MenuHelper.PLAIN, "Add Database", "addDatabase", 0,"Add a database" );

		menuBar.add( file );
		menuBar.add( database );
		frame.setJMenuBar( menuBar );
	}

	public void addDatabase( Database db ){
		String host = db.getHost() + ":" + db.getPort();
		DatabaseBox dbb = new DatabaseBox( db.getName(), host, db.getUsername() );
		databaseBoxes.add( dbb );

		panel.add( dbb.getPanel() );
		frame.validate();
		frame.pack();

	}
}

