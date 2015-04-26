package tk.michael.project;

import com.michael.api.IO.IO;

import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import javax.swing.border.*;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class TextPaneTest extends JFrame implements KeyListener {
	private JPanel topPanel;
	private JTextPane tPane;

	public TextPaneTest() {
		topPanel = new JPanel();

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setLocationRelativeTo( null );

		EmptyBorder eb = new EmptyBorder( new Insets( 10, 10, 10, 10 ) );

		tPane = new JTextPane();
		tPane.setBorder( eb );
		//tPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		tPane.setMargin( new Insets( 5, 5, 5, 5 ) );

		topPanel.add( new JScrollPane( tPane ) );

		appendToPane( tPane, "My Name is Too Good.\n", Color.RED );
		appendToPane( tPane, "I wish I could be ONE of THE BEST on ", Color.BLUE );
		appendToPane( tPane, "Stack", Color.DARK_GRAY );
		appendToPane( tPane, "Over", Color.MAGENTA );
		appendToPane( tPane, "flow", Color.ORANGE );

		tPane.addKeyListener( this );

		getContentPane().add( topPanel );

		pack();
		setVisible( true );
	}

	private void appendToPane( JTextPane tp, String msg, Color c ) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute( SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c );

		aset = sc.addAttribute( aset, StyleConstants.FontFamily, "Lucida Console" );
		aset = sc.addAttribute( aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED );

		int len = tp.getDocument().getLength();
		tp.setCaretPosition( len );
		tp.setCharacterAttributes( aset, false );
		tp.replaceSelection( msg );
	}

	@Override
	public void keyTyped( KeyEvent e ) {}

	@Override
	public void keyPressed( KeyEvent e ) {}

	@Override
	public void keyReleased( KeyEvent e ) {
		IO.println( tPane.getText() );
	}

//	public static void main(String... args)
//	{
//		SwingUtilities.invokeLater(new Runnable()
//		{
//			public void run()
//			{
//				new TextPaneTest();
//			}
//		});
//	}
}