package tk.michael.project.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created By: Michael Risher
 * Date: 5/27/15
 * Time: 5:59 PM
 */
public class FileChooser extends JPanel implements ActionListener {
	private JFileChooser chooser;

	public FileChooser( String title ){
		this( title, null );
	}

	public FileChooser( String title, String[][] types ){
		chooser = new JFileChooser();
		chooser.setCurrentDirectory( new File( "." ) );
		chooser.setDialogTitle( title );
		chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
		if ( types != null ) {
			chooser.removeChoosableFileFilter( chooser.getFileFilter() );
			for ( int i = 0; i < types.length; i++ ){
				String[] type = types[i];
			    chooser.addChoosableFileFilter( new FileNameExtensionFilter( type[0], type[1] ) );
			}
		}
		else{
			chooser.setAcceptAllFileFilterUsed( true );
		}

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
