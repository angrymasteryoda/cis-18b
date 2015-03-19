/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.michael.api.IO.ResourceLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Michael
 */
public class MainWindow extends javax.swing.JFrame {

	/**
	 * Creates new form MainWindow
	 */
	public MainWindow() {
		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		menuPane = new JPanel( new BorderLayout( 25, 25 ) );
		addLabel = new JLabel( new ImageIcon( "res/plus.png" ) );
		menuPane.add( addLabel, BorderLayout.WEST );
		menuPane.setBackground( new Color( 151, 151, 151 ) );

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder( BorderFactory.createMatteBorder( 3, 0, 0, 0, Color.GRAY ) );
		DatabaseBox d1 = new DatabaseBox();
		DatabaseBox d2 = new DatabaseBox();
		DatabaseBox d3 = new DatabaseBox();
		scrollPane.add( d1 );
		scrollPane.add( d2 );
		scrollPane.add( d3 );

		JPanel bothPanel = new JPanel( new BorderLayout() );
		bothPanel.add( menuPane, BorderLayout.NORTH );
		bothPanel.setBorder( BorderFactory.createMatteBorder( 3, 8, 3, 8, Color.GRAY ) );
		bothPanel.add( scrollPane );

		JLabel copyLabel = new JLabel( "<html>&copy;2014 peeskillet</html>" );
		copyLabel.setBackground( Color.LIGHT_GRAY );
		copyLabel.setHorizontalAlignment( JLabel.CENTER );
		bothPanel.add( copyLabel, BorderLayout.PAGE_END );

		this.add( bothPanel );
		this.pack();
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		this.setLocationByPlatform( true );
		this.setVisible( true );
	}

	private JLabel addLabel;

	private JPanel container;
	private JPanel menuPane;
	private JPanel mainPane;
}
