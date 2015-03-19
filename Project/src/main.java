import java.io.File;
import java.io.InputStream;

/**
 * Created By: Michael Risher
 * Date: 3/11/15
 * Time: 5:36 PM
 */
public class main {

	public main(){

	}

	public static void main( String[] args ) {
		File dir1 = new File("res/plus.png");
		System.out.println("current directory: " + dir1.getAbsolutePath());
		//*
	    // Set the Nimbus look and feel
		try {
			for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() ) {
				if ( "Nimbus".equals( info.getName() ) ) {
					javax.swing.UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		} catch ( ClassNotFoundException  | InstantiationException | IllegalAccessException |
			javax.swing.UnsupportedLookAndFeelException ex ) {
			java.util.logging.Logger.getLogger( MainWindow.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		}

		java.awt.EventQueue.invokeLater( new Runnable() {
			public void run() {
				new MainWindow();
			}
		} );
		//*/
	}
}
