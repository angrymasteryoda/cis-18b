package tk.michael.project.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created By: Michael Risher
 * Date: 5/13/15
 * Time: 2:07 PM
 */
public abstract class ModalDialog {
	protected JDialog dialog;
	protected Frame parent = null;
	protected String title = null;

	public ModalDialog() {
		this.dialog = new JDialog();
	}

	public ModalDialog( Frame parent, String title ) {
		this.dialog = new JDialog( parent, title );
		this.parent = parent;
		this.title = title;
	}

	public ModalDialog( Frame parent, String title, boolean modality ) {
		this.dialog = new JDialog( parent, title, modality );
		this.parent = parent;
		this.title = title;
	}

	protected abstract void init();

	public void display(){
		dialog.setVisible( true );
	}
}
