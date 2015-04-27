package tk.michael.project.gui;

import net.miginfocom.swing.MigLayout;
import tk.michael.project.Main;
import tk.michael.project.util.Database;
import tk.michael.project.util.DatabaseHandler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 4/27/15
 * Time: 3:56 AM
 */
public class ConnectedWindow extends BasicFrameObject {
	private final UUID dbId;
	private Database db;
	private JTree navTree;

	public ConnectedWindow( UUID dbId ) {
		super();
		this.dbId = dbId;
		this.db = DatabaseHandler.getDabase( dbId );
		frame.setTitle( "Connected to " + this.db.getName() + " as " + this.db.getUsername() );
		init();
	}

	@Override
	void init() {
		initMenu();
		initNavTree();
		JPanel panel = new JPanel( new MigLayout( "w 738px!, h 600px" + ( Main.isDebugView() ? ",debug" : "" ) ) );

		panel.add( new JScrollPane( navTree ) );

		frame.add( panel );

		frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		frame.pack();
		frame.setLocationRelativeTo( null );
	}

	@Override
	void initMenu() {

	}

	void initNavTree(){
		//create the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( "Root" );
		//create the child nodes
		DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode( "Vegetables" );
		DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode( "Fruits" );

		//add the child nodes to the root node
		root.add( vegetableNode );
		root.add( fruitNode );

		//create the tree by passing in the root node
		navTree = new JTree( root );

		navTree.setRootVisible(false);
	}
}
