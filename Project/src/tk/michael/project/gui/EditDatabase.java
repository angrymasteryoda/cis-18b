package tk.michael.project.gui;

import net.miginfocom.swing.MigLayout;
import tk.michael.project.db.ConnectionStatus;
import tk.michael.project.db.MysqlDatabase;
import tk.michael.project.util.Database;
import tk.michael.project.util.DatabaseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created By: Michael Risher
 * Date: 4/25/15
 * Time: 2:40 AM
 */
public class EditDatabase extends BasicFrameObject implements ActionListener {

	private HashMap< String, RegexTextField > textFields = new HashMap<>();
	private HashMap< String, MLabel > labels = new HashMap<>();
	private Database db;
	private ArrayList<Component> order = new ArrayList<>();
	private FocusPolicy focus;
	private JPasswordField password;

	public EditDatabase( Database db ) {
		super( "Editing "  + db.getName() );
		this.db = db;
		init();
	}

	@Override
	protected void init() {
		JPanel panel = new JPanel( new MigLayout(  ) );

		labels.put( "name", new MLabel( "Name:" ) );
		textFields.put( "name", new RegexTextField( db.getName(), 20, "notempty" ) );

		labels.put( "host", new MLabel( "Host:" ) );
		textFields.put( "host", new RegexTextField( db.getHost(), 11, "notempty" ) );

		labels.put( "port", new MLabel( "Port:" ) );
		textFields.put( "port", new RegexTextField( db.getPort(), 5, "^0*(110,?000|((10[0-9]|[1-9][0-9]|[1-9]),?[0-9]{3})|([1-9][0-9]{2}|[1-9][0-9]|[1-9]))$" ) );

		labels.put( "user", new MLabel( "Username:" ) );
		textFields.put( "user", new RegexTextField( db.getUsername(), 20, "notempty" ) );

		labels.put( "pass", new MLabel( "Password:" ) );
		password = new JPasswordField( db.getPassword(), 20 );

		labels.put( "database", new MLabel( "Database:" ) );
		textFields.put( "database", new RegexTextField( db.getDatabaseName(), 20, null ) );
		textFields.get( "database" ).setToolTipText( "Default database to use" );

		JButton confirm = new JButton( "OK" );
		confirm.setActionCommand( "create" );
		confirm.addActionListener( this );

		JButton cancel = new JButton( "Cancel" );
		cancel.setActionCommand( "cancel" );
		cancel.addActionListener( this );

		JButton testConnection = new JButton( "Test Connection" );
		testConnection.setActionCommand( "test" );
		testConnection.addActionListener( this );

		panel.add( labels.get( "name" ) );
		panel.add( textFields.get( "name" ), "wrap" );
		panel.add( labels.get( "host" ) );
		panel.add( textFields.get( "host" ), "split 3" );
		panel.add( labels.get( "port" ) );
		panel.add( textFields.get( "port" ), "wrap" );
		panel.add( labels.get( "user" ) );
		panel.add( textFields.get( "user" ), "wrap" );
		panel.add( labels.get( "pass" ) );
		panel.add( password, "wrap" );
		panel.add( labels.get( "database" ), "gaptop 20px" );
		panel.add( textFields.get( "database" ), "wrap" );

		panel.add( confirm, "skip, split 3" );
		panel.add( cancel, "" );
		panel.add( testConnection, "wrap" );

		//focus order
		order.add( textFields.get( "name" ) );
		order.add( textFields.get( "host" ) );
		order.add( textFields.get( "user" ) );
		order.add( password );
		order.add( textFields.get( "database" ) );
		order.add( confirm );
		order.add( cancel );
		order.add( testConnection );

		frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
		frame.setFocusTraversalPolicy( new FocusPolicy( order ) );
		frame.add( panel );
		frame.pack();
		frame.setLocationRelativeTo( null );
	}

	@Override
	protected void initMenu() {
		//no menus
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		String action = e.getActionCommand();

		if ( action.equals( "cancel" ) ) {
			frame.dispose();
		}

		if ( action.equals( "create" ) ) {
			String msg = "<html><p>Fix the following errors</p><ul>";
			boolean ok = true;
			String[] fields = { "name", "host", "user" };

			for ( int i = 0; i < fields.length; i++ ){
				if ( !textFields.get( fields[i] ).matches() ) {
					ok = false;
					msg += "<li>Fill in the " + fields[i] + " field</li>";
				}
			}

			if ( !textFields.get( "port" ).matches() ) {
				ok = false;
				msg += "<li>The port entered was invalid</li>";
			}

			if ( ok ) {
				db.setName( textFields.get( "name" ).getText() );
				db.setHost( textFields.get( "host" ).getText() );
				db.setPort( textFields.get( "port" ).getText() );
				db.setUsername( textFields.get( "user" ).getText() );
				db.setPassword( password.getText() );
				db.setDatabaseName( textFields.get( "database" ).getText() );

				DatabaseHandler.editDatabase( db );
				frame.dispose();
			}
			else{
				JOptionPane.showMessageDialog( frame, "The port you entered is invalid" );
			}
		}

		if ( action.equals( "test" ) ) {
			//todo figure out the code for this
			Database db = new Database(
				textFields.get( "name" ).getText(),
				textFields.get( "host" ).getText(),
				textFields.get( "port" ).getText(),
				textFields.get( "user" ).getText(),
				password.getText(),
				textFields.get(  "database" ).getText()
			);

			ConnectionStatus status = MysqlDatabase.testConnection( db.getUrl(), db.getUsername(), db.getPassword() );
			String message = "Connected successfully!";
			if ( !status.isConnected() ) {
				message = "<html><p>Unable to connect.</p><p>";
				message += status.getReason() + "</p></html>";
			}

			JOptionPane.showMessageDialog( frame, message );
		}
	}
}
