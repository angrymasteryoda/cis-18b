package tk.michael.project.gui;

import com.michael.api.security.Hashing;
import net.miginfocom.swing.MigLayout;
import tk.michael.project.Main;
import tk.michael.project.db.DbUtils;
import tk.michael.project.db.MysqlDatabase;
import tk.michael.project.util.DatabaseHandler;
import tk.michael.project.util.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created By: Michael Risher
 * Date: 5/13/15
 * Time: 2:49 PM
 */
public class SignUpDialog extends ModalDialog implements ActionListener{
	private HashMap< String, RegexTextField > textFields = new HashMap<>();
	private HashMap< String, MLabel > labels = new HashMap<>();
	private JPasswordField password;
	private JPasswordField passwordConf;

	public SignUpDialog( Frame parent, String title ) {
		super( parent, title, true );
		init();
	}

	@Override
	protected void init() {
		JPanel panel = new JPanel( new MigLayout(  ) );

		labels.put( "user", new MLabel( "Username:" ) );
		textFields.put( "user", new RegexTextField( "michael", 20, "notempty" ) );

		labels.put( "email", new MLabel( "Email:" ) );
		textFields.put( "email", new RegexTextField( "w@w.w", 20, "[a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?"  ) );

		labels.put( "emailConf", new MLabel( "Confirm Email:" ) );
		textFields.put( "emailConf", new RegexTextField( "w@w.w", 20, "[a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?" ) );

		labels.put( "pass", new MLabel( "Password:" ) );
		password = new JPasswordField( "123", 20 );

		labels.put( "passConf", new MLabel( "Confirm Password:" ) );
		passwordConf = new JPasswordField( "123", 20 );

		JButton confirm = new JButton( "Sign Up" );
		confirm.setActionCommand( "confirm" );
		confirm.addActionListener( this );
		JButton cancel = new JButton( "Cancel" );
		cancel.setActionCommand( "cancel" );
		cancel.addActionListener( this );

		panel.add( labels.get( "user" ) );
		panel.add( textFields.get( "user" ), "wrap" );
		panel.add( labels.get( "email" ) );
		panel.add( textFields.get( "email" ), "wrap" );
		panel.add( labels.get( "emailConf" ) );
		panel.add( textFields.get( "emailConf" ), "wrap" );
		panel.add( labels.get( "pass" ) );
		panel.add( password, "wrap" );
		panel.add( labels.get( "passConf" ) );
		panel.add( passwordConf, "wrap" );
		panel.add( confirm, "skip, split 2" );
		panel.add( cancel );

		dialog.setContentPane( panel );
		dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		dialog.pack();
		dialog.setLocationRelativeTo( parent );
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		String action = e.getActionCommand();
		if ( action.equals( "confirm" ) ) {
			String msg = "<html><p>Fix the following errors</p><ul>";
			boolean ok = true;
			String[] fields = { "user", "email", "emailConf" };
			for ( int i = 0; i < fields.length; i++ ){
				textFields.get( fields[i] ).setText( textFields.get( fields[i] ).getText().toLowerCase() );
				if ( !textFields.get( fields[i] ).matches() ) {
					ok = false;
					String fieldTitle = labels.get( fields[i] ).getText();
					fieldTitle = fieldTitle.split( ":" )[0];
					msg += "<li>Fill in the " + fieldTitle + " field</li>";
				}
			}

			if ( !textFields.get( "email" ).equals( textFields.get( "emailConf" ) ) ) {
				msg += "<li>The Email fields do not match</li>";
			}

			final String passText = password.getText();
			final String passConfText = passwordConf.getText();

			if ( passText.isEmpty() ) {
				msg += "<li>Fill in the Password field</li>";
			}

			if ( passConfText.isEmpty() ) {
				msg += "<li>Fill in the Confirm Password field</li>";
			}

			if ( !passText.equals( passConfText ) ) {
				msg += "<li>The Passwords fields do not match</li>";
			}

			if ( ok ) {
				MysqlDatabase db = new MysqlDatabase();
				if ( db.open() ) {
					int found = db.numRows( DbUtils.prependTable( "users" ), "user='" + textFields.get( "user" ).getText() +
						"' or email='" + textFields.get( "email" ).getText() + "'" );
					if ( found == 0 && db.tableExists( DbUtils.prependTable( "users" ) ) ) {
						PreparedStatement state = db.query( "INSERT INTO " + DbUtils.prependTable( "users" ) + " (id,user,email,password) VALUES (?,?,?,?)" );

						UUID userId = UUID.randomUUID();
						String pass = passText;
						try { pass = Hashing.sha1( pass ); } catch ( Exception ex ){}
						try {
							state.setString( 1, userId.toString() );
							state.setString( 2, textFields.get( "user" ).getText() );
							state.setString( 3, textFields.get( "email" ).getText() );
							state.setString( 4, pass );
							found = state.executeUpdate();
							if ( found > 0 ) {
								db.close();
								Session session = new Session( userId, textFields.get( "user" ).getText() );
								session.createSession();
								Main.login( session );
								DatabaseHandler.saveDatabasesToDb();
								dialog.setVisible( false );
								dialog.dispose();
							} else {
								JOptionPane.showMessageDialog( null, "We could not create your account, please try again.", "Error", 0 );
							}
						} catch ( SQLException ex ) {
							ex.printStackTrace();
						}
					} else{
						int userFind = db.numRows( DbUtils.prependTable( "users" ), "user='" + textFields.get( "user" ).getText() + "'" );
						int emailFind = db.numRows( DbUtils.prependTable( "users" ), "email='" + textFields.get( "email" ).getText() + "'" );
						String details = "";
						if ( userFind != 0 && emailFind != 0 ) {
							details = "Username and Email are already in use.";
						}
						else if ( userFind != 0 ) {
							details = "Username is already in use.";
						}
						else if ( emailFind != 0 ) {
							details = "Email is already in use.";
						}
						else{
							details = "You broke the matrix good job";
						}
						JOptionPane.showMessageDialog( null, details );
					}
				} else {
					JOptionPane.showMessageDialog( null, "Unable to connect to internet. Try again." );
				}
				db.close();
			} else{
				JOptionPane.showMessageDialog( null, msg + "</ul></html>" );
			}

		} else {
			dialog.setVisible( false );
			dialog.dispose();
		}
	}
}
