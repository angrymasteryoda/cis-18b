package tk.michael.project.gui;

import javax.swing.*;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 6:36 PM
 */
public abstract class BasicFrameObject {

	protected JFrame frame;

	public BasicFrameObject() {
		frame = new JFrame();
	}

	public BasicFrameObject( String title ) {
		frame = new JFrame( title );
	}

	protected abstract void init();
	protected abstract void initMenu();

	public void display(){
		frame.setVisible( true );
	}
}
