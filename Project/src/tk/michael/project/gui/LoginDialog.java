package tk.michael.project.gui;

import com.michael.api.IO.IO;
import com.michael.api.security.Hashing;
import net.miginfocom.swing.MigLayout;
import tk.michael.project.Main;
import tk.michael.project.db.DbUtils;
import tk.michael.project.db.MysqlDatabase;
import tk.michael.project.util.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 5/13/15
 * Time: 2:11 PM
 */
public class LoginDialog extends ModalDialog implements ActionListener{
	private RegexTextField user;
	private JPasswordField pass;

	public LoginDialog( Frame parent, String title ) {
		super( parent, title, true );
		init();
	}

	@Override
	protected void init() {
		JPanel panel = new JPanel( new MigLayout() );
//		panel.setBackground( new Color( 0x222222 ) );

		JLabel userL = new JLabel( "Username or Email" );
//		userL.setForeground( Color.WHITE );
		user = new RegexTextField( 20, "notempty" );
//		user.setBackground( new Color( 0x777777 ) );
		JLabel passL = new JLabel( "Password:" );
//		passL.setForeground( Color.WHITE );
		pass = new JPasswordField( 20 );
//		pass.setBackground( new Color( 0x777777 ) );


		JButton confirm = new JButton( "Login" );
		confirm.setActionCommand( "confirm" );
		confirm.addActionListener( this );
		JButton signUp = new JButton( "Sign Up" );
		signUp.setActionCommand( "signUp" );
		signUp.addActionListener( this );
		JButton cancel = new JButton( "Cancel" );
		cancel.setActionCommand( "cancel" );
		cancel.addActionListener( this );


		panel.add( userL );
		panel.add( user, "wrap" );
		panel.add( passL );
		panel.add( pass, "wrap" );
		panel.add( confirm, "skip, split 3");
		panel.add( signUp );
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
			if ( user.matches() ) {
				MysqlDatabase db = new MysqlDatabase(  );
				String passHash = pass.getText();
				try {
					passHash = Hashing.sha1( passHash );
				} catch ( Exception ex ) {}
				if ( db.open() ) {
					int findUser = db.numRows( DbUtils.prependTable( "users" ), "user='" + user.getText() + "' AND password='" +  passHash + "'" );
					int findEmail = db.numRows( DbUtils.prependTable( "users" ), "email='" + user.getText() + "' AND password='" +  passHash + "'" );
					if ( findEmail == 1 || findUser == 1 ) {
						try {
							PreparedStatement statement = db.query( "SELECT id, user FROM " + DbUtils.prependTable( "users" ) + " WHERE ( " +
								"user='" + user.getText() + "' OR " +
								"email='" + user.getText() + "'" +
								") AND password='" + passHash + "'" );
							ResultSet res = statement.executeQuery();
							if ( res.next() ) {
								UUID userId = UUID.fromString( res.getString( 1 ) );
								String user = res.getString( 2 );
								Session session = new Session( userId, user );
								session.createSession();
								Main.login( session );
								dialog.setVisible( false );
								dialog.dispose();
							} else {
								IO.printlnErr( "found user in login but no user id" );
							}

						} catch ( SQLException ex ) {
							ex.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog( null, "Username or password is wrong" );
					}
				}
				db.close();
			} else {
				JOptionPane.showMessageDialog( null, "Username can not be empty" );
			}
		} else {
			dialog.setVisible( false );
			dialog.dispose();
			if ( action.equals( "signUp" ) ) {
				new SignUpDialog( parent, "Sign Up" ).display();
			}
		}
	}
}
