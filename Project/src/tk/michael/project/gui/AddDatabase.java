package tk.michael.project.gui;

import net.miginfocom.swing.MigLayout;
import tk.michael.project.util.DatabaseHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 6:35 PM
 */
public class AddDatabase extends BasicFrameObject implements ActionListener{

	private HashMap< String, RegexTextField > textFields = new HashMap<>();
	private HashMap< String, MLabel > labels = new HashMap<>();

	private JButton confirm;
	private JButton cancel;
	private JButton testConnection;

	public AddDatabase() {
		super( "Add Database" );
		init();
	}

	@Override
	void init() {
		JPanel panel = new JPanel( new MigLayout(  ) );

		labels.put( "name", new MLabel( "Name:" ) );
		textFields.put( "name", new RegexTextField( 20, null ) );

		labels.put( "host", new MLabel( "Host:" ) );
		textFields.put( "host", new RegexTextField( 11, null ) );

		labels.put( "port", new MLabel( "Port:" ) );
		textFields.put( "port", new RegexTextField( "3306", 5, "^0*(110,?000|((10[0-9]|[1-9][0-9]|[1-9]),?[0-9]{3})|([1-9][0-9]{2}|[1-9][0-9]|[1-9]))$" ) );

		labels.put( "user", new MLabel( "Username:" ) );
		textFields.put( "user", new RegexTextField( 20, null ) );

		labels.put( "pass", new MLabel( "Password:" ) );
		textFields.put( "pass", new RegexTextField( 20, null ) );

		confirm = new JButton( "OK" );
		confirm.setActionCommand( "create" );
		confirm.addActionListener( this );

		cancel = new JButton( "Cancel" );
		cancel.setActionCommand( "cancel" );
		cancel.addActionListener( this );

		testConnection = new JButton( "Test Connection" );
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
		panel.add( textFields.get( "pass" ), "wrap" );

		panel.add( confirm, "skip, split 3" );
		panel.add( cancel, "" );
		panel.add( testConnection, "wrap" );

		frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
		frame.add( panel );
		frame.pack();
	}

	@Override
	void initMenu() {
		//no menus here
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		String action = e.getActionCommand();

		if ( action.equals( "cancel" ) ) {
			frame.dispose();
		}

		if ( action.equals( "create" ) ) {
			if ( textFields.get( "port" ).matches() ) {
				DatabaseHandler.addDatabase(
					textFields.get( "name" ).getText(),
					textFields.get( "host" ).getText(),
					textFields.get( "port" ).getText(),
					textFields.get( "user" ).getText(),
					textFields.get( "pass" ).getText()
				);
				frame.dispose();
			}
			else{
				JOptionPane.showMessageDialog( frame, "The port you entered is invalid" );
			}
		}

		if ( action.equals( "test" ) ) {
			//todo figure out the code for this
		}
	}
}
