package tk.michael.project.gui;

import net.miginfocom.swing.MigLayout;
import tk.michael.project.Main;
import tk.michael.project.util.Database;
import tk.michael.project.util.DatabaseHandler;

import javax.swing.*;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 4/27/15
 * Time: 3:56 AM
 */
public class ConnectedWindow extends BasicFrameObject {
	private final UUID dbId;
	private Database db;

	public ConnectedWindow( UUID dbId ) {
		super();
		this.dbId = dbId;
		this.db = DatabaseHandler.getDabase( dbId );
		frame.setTitle( "Connected to " + this.db.getName() );
	}

	@Override
	void init() {
		initMenu();
		JPanel panel = new JPanel( new MigLayout( "w 738px!, h 35px!" + ( Main.isDebugView() ? ",debug" : "" ) ) );

		frame.add( panel );

		frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		frame.pack();
		frame.setLocationRelativeTo( null );
	}

	@Override
	void initMenu() {

	}
}
