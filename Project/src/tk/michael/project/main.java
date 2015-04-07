package tk.michael.project;

/**
 * Created By: Michael Risher
 * Date: 3/11/15
 * Time: 5:36 PM
 */
public class main {

	public static void main( String[] args ) {
		setNimbus();

		new Tester().setVisible( true );

        /* Create and display the form */
//		java.awt.EventQueue.invokeLater( new Runnable() {
//			public void run() {
//				new MainWindow().setVisible( true );
//			}
//		} );
	}

	public static void setNimbus() {
		try {
			for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() ) {
				if ( "Nimbus".equals( info.getName() ) ) {
					javax.swing.UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		} catch ( Exception ex ) {
			java.util.logging.Logger.getLogger( MainWindow.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		}
	}
}
