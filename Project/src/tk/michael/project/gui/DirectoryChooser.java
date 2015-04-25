package tk.michael.project.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created By: Michael Risher
 * Date: 4/25/15
 * Time: 4:53 AM
 */
public class DirectoryChooser extends JPanel implements ActionListener {

	JFileChooser chooser;
	public DirectoryChooser( String title) {
		int result;

		chooser = new JFileChooser(  );
		chooser.setCurrentDirectory( new File( "." ) );
		chooser.setDialogTitle( title );
		chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		chooser.setAcceptAllFileFilterUsed( false );
	}

	public File display(){
		if ( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ) {
			return chooser.getSelectedFile();
		}
		return null;
	}

	@Override
	public void actionPerformed( ActionEvent e ) {

	}
}
