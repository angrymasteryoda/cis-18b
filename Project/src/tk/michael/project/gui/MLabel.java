package tk.michael.project.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 4:49 PM
 */
public class MLabel extends JLabel {
	private Font regularFont = new Font( "Courier New", Font.PLAIN, 12 );

	public MLabel( String text, Icon icon, int horizontalAlignment ) {
		super( text, icon, horizontalAlignment );
	}

	public MLabel( String text, int horizontalAlignment ) {
		super( text, horizontalAlignment );
	}

	public MLabel( String text ) {
		super( text );
	}

	public void setBasicStyle(){
		this.setFont( regularFont );
	}
	public void setFont( int type, int size ){
		this.setFont( new Font( "Courier New", type, size ) );
	}

	public void setFont( int size ){
		this.setFont( new Font( "Courier New", Font.PLAIN, size ) );
	}
}
