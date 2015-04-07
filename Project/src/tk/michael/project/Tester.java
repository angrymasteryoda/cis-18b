package tk.michael.project;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 4/6/15
 * Time: 4:18 PM
 */
public class Tester extends JFrame {

	public Tester() {
		super( "test" );
		setLocationRelativeTo( null );
		setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );


		JPanel panel = new JPanel( new MigLayout() );

		JLabel firstNameLabel = new JLabel( "firstname:" );
		JLabel lastNameLabel = new JLabel( "lastname:" );
		JLabel addressLabel = new JLabel( "Address:" );

		JTextField firstNameTextField = new JTextField( 15 );
		JTextField lastNameTextField = new JTextField( 15 );
		JTextField addressTextField = new JTextField( 15 );


		panel.add( firstNameLabel );
		panel.add( firstNameTextField );
		panel.add( lastNameLabel, "gap unrelated" );
		panel.add( lastNameTextField, "wrap" );
		panel.add( addressLabel );
		panel.add( addressTextField, "span, grow" );

		add( panel );
	}


}
