package tk.michael.project.gui;

import net.miginfocom.swing.MigLayout;

import javax.print.attribute.standard.RequestingUserName;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 4:25 PM
 */
public class DatabaseBox {
	private JPanel panel;
	private MLabel name;
	private MLabel host;
	private MLabel username;

	public DatabaseBox( String name, String host, String username ) {
		init( name, host, username );
	}

	public void init( String nameStr, String hostStr, String usernameStr ) {
		panel = new JPanel( new MigLayout( "w 175px!, h 75px!") );

		name = new MLabel( nameStr );
		host = new MLabel( hostStr );
		username = new MLabel( usernameStr );

		name.setBasicStyle();
		host.setBasicStyle();
		username.setBasicStyle();

		name.setForeground( new Color( 0xD2D2D2 ) );
		host.setForeground( new Color( 0xD2D2D2 ) );
		username.setForeground( new Color( 0xD2D2D2 ) );

		name.setFont( Font.BOLD, 13 );
		panel.setBackground( new Color( 0x2F2F2F ) );

		panel.add( this.name, "wrap" );
		panel.add( this.host );
		panel.add( this.username );

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
}
