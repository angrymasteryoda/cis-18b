/**
 * Created By: Michael Risher
 * Date: 3/11/15
 * Time: 5:36 PM
 */
public class main {

	public static void main( String[] args ) {
	    /* Set the Nimbus look and feel */
		try {
			for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() ) {
				if ( "Nimbus".equals( info.getName() ) ) {
					javax.swing.UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		} catch ( ClassNotFoundException ex ) {
			java.util.logging.Logger.getLogger( MainWindow.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		} catch ( InstantiationException ex ) {
			java.util.logging.Logger.getLogger( MainWindow.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		} catch ( IllegalAccessException ex ) {
			java.util.logging.Logger.getLogger( MainWindow.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		} catch ( javax.swing.UnsupportedLookAndFeelException ex ) {
			java.util.logging.Logger.getLogger( MainWindow.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		}
		//</editor-fold>

        /* Create and display the form */
		java.awt.EventQueue.invokeLater( new Runnable() {
			public void run() {
				new MainWindow().setVisible( true );
			}
		} );
	}
}
