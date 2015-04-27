package tk.michael.project;


import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 4/27/15
 * Time: 4:34 PM
 */
public class TreeExample extends JFrame
{
	private JTree tree;
	public TreeExample()
	{
		//create the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		//create the child nodes
		DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
		DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");

		//add the child nodes to the root node
		root.add(vegetableNode);
		root.add(fruitNode);

		//create the tree by passing in the root node
		tree = new JTree(root);
		add(tree);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("JTree Example");
		this.pack();
		this.setVisible(true);
	}
}