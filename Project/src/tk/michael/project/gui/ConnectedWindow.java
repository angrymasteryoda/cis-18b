package tk.michael.project.gui;

import com.michael.api.IO.IO;
import net.miginfocom.swing.MigLayout;
import tk.michael.project.Main;
import tk.michael.project.db.Commands;
import tk.michael.project.db.ConnectionStatus;
import tk.michael.project.db.MysqlDatabase;
import tk.michael.project.util.Database;
import tk.michael.project.util.DatabaseHandler;
import tk.michael.project.util.SyntaxRegex;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 4/27/15
 * Time: 3:56 AM
 */
public class ConnectedWindow extends BasicFrameObject implements ActionListener {
	private final UUID dbId;
	private Database database;

	private JTree navTree;
	private DefaultMutableTreeNode selectedNode;
	private JPopupMenu navPopupMenu;

	private JTextPane textPane;

	private JTable table;
	private DefaultTableModel dataModel;


	public ConnectedWindow( UUID dbId ) {
		super();
		this.dbId = dbId;
		this.database = DatabaseHandler.getDatabase( dbId );
		frame.setTitle( "Connected to " + this.database.getName() + " as " + this.database.getUsername() );
		init();
	}

	@Override
	public void display(){
		//test we can connect first
		ConnectionStatus status = MysqlDatabase.testConnection( this.database );
		if ( status.isConnected() ) {
			frame.setVisible( true );
		} else {
			JOptionPane.showOptionDialog( null, "Unable to open database.\nMake sure the information you provided is correct and you are connected to the internet.\n" + status.getReason(),
				"Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null );

		}
	}

	@Override
	protected void init() {
		initMenu();
		initNavTree();
		initSyntaxEditor();

		setEmptyTable();
		table = new JTable( dataModel ){
			public boolean isCellEditable(int rowIndex, int vColIndex) {
				return false;
			}
		};

		JPanel wrapper = new JPanel( new MigLayout( "w 738px!, h 600px, fill, inset 0" + ( Main.isDebugView() ? ",debug" : "" ) ) );

		JPanel treePane = new JPanel( new MigLayout( "fill" ) );

		JPanel scriptPane = new JPanel( new MigLayout( "fill, inset 0" ) );
		JPanel tablePane = new JPanel( new MigLayout( "fill, inset 0" ) );
//		JPanel rightPane = new JPanel( new MigLayout( "fill" ) );

		treePane.add( new JScrollPane( navTree ), "dock west, w 100%, h 100%, grow" );
		scriptPane.add( new JScrollPane( textPane ), "dock center, w 538px, h 50%, grow");
		tablePane.add( new JScrollPane( table ), "dock center, w 538px, h 50%, grow" );
		JSplitPane rightPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, true, scriptPane, tablePane );
		rightPane.setBorder( null );
		JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, true, treePane, rightPane );
		splitPane.setBorder( null );

		wrapper.add( splitPane, "grow, push" );
		frame.add( wrapper );

		frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
//		frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		frame.pack();
		frame.setLocationRelativeTo( null );
		splitPane.setDividerLocation( 200 ); //set it so the tree isnt tiny
		rightPane.setDividerLocation( frame.getHeight() / 2); //set it so the tree isnt tiny
	}

	@Override
	protected void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu( "File" );
		file.setMnemonic( 'F' );

		MenuHelper.createMenuItem( file, MenuHelper.PLAIN, "Load", "loadDbs", 'L', "Load from custom location" );
		MenuHelper.createMenuItem( file, MenuHelper.PLAIN, "Set Save Location", "saveLoc", 0, "Sets the default save location" );
		MenuHelper.createMenuItem( file, MenuHelper.PLAIN, "Exit", "exitApp", 'X', "Close the application" );
		if ( Main.isArg( "dev" ) ) {
			MenuHelper.createMenuItem( file, MenuHelper.PLAIN, "Open MainFrame", "openMainWindow", 0 );
		}

		JMenu database = new JMenu( "Database" );
		database.setMnemonic( 'd' );
		MenuHelper.createMenuItem( database, MenuHelper.PLAIN, "Open Database", "openDatabase", 0,"Open a database" );
		MenuHelper.createMenuItemCustomAction( database, MenuHelper.PLAIN, "Close Database", "closeDatabase", 0, "Close current connection", this );

		JMenu script = new JMenu( "Script" );
		script.setMnemonic( 's' );
		MenuHelper.createMenuItemCustomAction( script, MenuHelper.PLAIN, "Run", "runScript", 0, "Run the Script", this );

		menuBar.add( file );
		menuBar.add( database );
		menuBar.add( script );
		frame.setJMenuBar( menuBar );
	}

	private void initNavTree(){
		initTreePopUpMenu();
		ArrayList<String> databases = new ArrayList<>();
		MysqlDatabase db = new MysqlDatabase( this.database );
		if( db.open() ) {
			Statement statement = null;
			try {
				statement = db.getConnection().createStatement();
				ResultSet res = statement.executeQuery( "SHOW DATABASES" );
				while ( res.next() ){
					databases.add( res.getString( 1 ) );
				}
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
			db.close();
		}



		//create the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( "Root" );
		//create the child nodes
		ArrayList<DatabaseTreeNode> databaseNodes = new ArrayList<>();
		for ( int i = 0; i < databases.size(); i++ ){
		    databaseNodes.add( new DatabaseTreeNode( databases.get( i ), this.database ) );
		}

		//add the child nodes to the root node
		for ( int i = 0; i < databaseNodes.size(); i++ ){
			root.add( databaseNodes.get( i ) );
		}

		//create the tree by passing in the root node
		navTree = new JTree( root );

		ImageIcon imageIcon = new ImageIcon( this.getClass().getResource( "/database.png" ) );
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon( imageIcon );

		navTree.setCellRenderer( renderer );

		//set right click menu
		navTree.setComponentPopupMenu( navPopupMenu );

		//set selection action
		navTree.getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {
			@Override
			public void valueChanged( TreeSelectionEvent e ) {
				selectedNode = (DefaultMutableTreeNode) navTree.getLastSelectedPathComponent();
				IO.println( selectedNode.getUserObject().toString() );
			}
		} );

		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)){
					int selRow = navTree.getRowForLocation(e.getX(), e.getY());
					TreePath selPath = navTree.getPathForLocation(e.getX(), e.getY());
					navTree.setSelectionPath(selPath);
					if (selRow>-1){
						navTree.setSelectionRow(selRow);
					}
					selectedNode = (DefaultMutableTreeNode) navTree.getLastSelectedPathComponent();
				}
			}
		};
		navTree.addMouseListener(ml);

		navTree.setRootVisible(false);
	}

	private void initTreePopUpMenu(){
		navPopupMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem( "Select * From" );
		item.setActionCommand( "treeSelect" );
		item.addActionListener( this );
		navPopupMenu.add( item );
	}

	private void initSyntaxEditor() {
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		final AttributeSet attrCommand = cont.addAttribute( cont.getEmptySet(), StyleConstants.Foreground, Color.RED );
		final AttributeSet attrDatatype = cont.addAttribute( cont.getEmptySet(), StyleConstants.Foreground, new Color( 0x01C40F ) );
		final AttributeSet attrWhere = cont.addAttribute( cont.getEmptySet(), StyleConstants.Foreground, new Color( 0xBC5C00 ) );
		final AttributeSet attrUnknown1 = cont.addAttribute( cont.getEmptySet(), StyleConstants.Foreground, new Color( 0 ) );
		final AttributeSet attrUnknown2 = cont.addAttribute( cont.getEmptySet(), StyleConstants.Foreground, new Color( 0 ) );
		final AttributeSet attrBlack = cont.addAttribute( cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK );
		DefaultStyledDocument doc = new DefaultStyledDocument() {
			public void insertString( int offset, String str, AttributeSet a ) throws BadLocationException {
				if ( str.matches( "\\t" ) ) { //todo put a real tab fix in here
					str = "    ";
				}
				super.insertString( offset, str, a );

				String text = getText( 0, getLength() );
				int before = findLastNonWordChar( text, offset );
				if ( before < 0 ) before = 0;
				int after = findFirstNonWordChar( text, offset + str.length() );
				int wordL = before;
				int wordR = before;

				while ( wordR <= after ) {
					if ( wordR == after || String.valueOf( text.charAt( wordR ) ).matches( "\\W" ) ) {
//						String subStr = text.substring( wordL, wordR );
						if ( text.substring( wordL, wordR ).matches( SyntaxRegex.mysqlCommand() ) )
							setCharacterAttributes( wordL, wordR - wordL, attrCommand, false );
						else if ( text.substring( wordL, wordR ).matches( SyntaxRegex.mysqlDataType() ) )
							setCharacterAttributes( wordL, wordR - wordL, attrDatatype, false );
						else if ( text.substring( wordL, wordR ).matches( SyntaxRegex.mysqlWhereSearch() ) )
							setCharacterAttributes( wordL, wordR - wordL, attrWhere, false );
						else if ( text.substring( wordL, wordR ).matches( SyntaxRegex.mysqlUnknown1() ) )
							setCharacterAttributes( wordL, wordR - wordL, attrUnknown1, false );
						else if ( text.substring( wordL, wordR ).matches( SyntaxRegex.mysqlUnknown2() ) )
							setCharacterAttributes( wordL, wordR - wordL, attrUnknown2, false );
						else
							setCharacterAttributes( wordL, wordR - wordL, attrBlack, false );
						wordL = wordR;
					}
					wordR++;
				}
			}

			public void remove( int offs, int len ) throws BadLocationException {
				super.remove( offs, len );

				String text = getText( 0, getLength() );
				int before = findLastNonWordChar( text, offs );
				if ( before < 0 ) before = 0;
				int after = findFirstNonWordChar( text, offs );

				if ( text.substring( before, after ).matches( "(\\W)*(private|public|protected)" ) ) {
					setCharacterAttributes( before, after - before, attrCommand, false );
				} else {
					setCharacterAttributes( before, after - before, attrBlack, false );
				}
			}
		};
		textPane = new JTextPane( doc );
		textPane.setText(
			"DROP TABLE IF EXISTS `test`;\n" +
			"CREATE TABLE test (\n" +
			"\tid varchar(20) NOT NULL,\n" +
			"\tusername varchar(50) NOT NULL,\n" +
			"\temail VARCHAR(255) NOT NULL,\n" +
			"\tpassword VARCHAR(35) NOT NULL,\n" +
			"\tcreated int(11) NOT NULL, \n" +
			"\tPRIMARY KEY (id),\n" +
			"\tINDEX (email)\n" +
			");\n" +
			"SELECT * FROM `cis18b`.`mr2358174_entity_databases` LIMIT 1000;"
		);
	}

	private int findLastNonWordChar( String text, int index ) {
		while ( --index >= 0 ) {
			if ( String.valueOf( text.charAt( index ) ).matches( "\\W" ) ) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar( String text, int index ) {
		while ( index < text.length() ) {
			if ( String.valueOf( text.charAt( index ) ).matches( "\\W" ) ) {
				break;
			}
			index++;
		}
		return index;
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		String action = e.getActionCommand();
		if ( action.equals( "closeDatabase" ) ) {
			this.frame.dispose();
		}

		if ( action.equals( "treeSelect" ) ) {
			DatabaseTreeNode db = (DatabaseTreeNode) selectedNode.getPath()[1];
			textPane.setText( "SELECT * FROM `" + db.getDbName() + "`.`" + selectedNode.getUserObject().toString() + "` LIMIT 1000;" );
		}

		if ( action.equals( "runScript" ) ) {
			runCommand( textPane.getText() );
		}
	}

	private void runCommand( String cmd ){
		Commands commands = new Commands( cmd );
		String dbName = database.getDatabaseName();
		try {
			dbName = ( (DatabaseTreeNode) selectedNode.getPath()[1] ).getDbName();
		} catch ( Exception e ) {
			IO.println( "No database selected in tree using default" );
		}

		if ( dbName.isEmpty() ) {
			JOptionPane.showOptionDialog( null, "You must select a default database by clicking on the database to the left or set in the edit database",
				"Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null );
			return;
		}


		MysqlDatabase db = new MysqlDatabase( database );

		if ( db.open() ) {
			try {
				db.getConnection().setCatalog( dbName );
				while( commands.next() ){
					if ( commands.getCommandType() == Commands.SELECT ) {
						Statement statement = db.getNewStatement();
						ResultSet rs = statement.executeQuery( commands.getCommand() );
						ResultSetMetaData rsmd = rs.getMetaData();
						IO.println( rsmd.getColumnName( 1 ) );
						rs.last();
						int rows = rs.getRow();
						int cols = rsmd.getColumnCount();
						rs.beforeFirst();
						Object columnValues[][] = new Object[rows][cols];
						Object columnNames[] = new Object[rsmd.getColumnCount()];
						for ( int i = 1; i <= rsmd.getColumnCount(); i++ ){
							columnNames[i-1] = rsmd.getColumnName( i );
						}

						int counter = 0;
						while ( rs.next() ) {
							for ( int i = 1; i <= rsmd.getColumnCount(); i++ ){
								columnValues[counter][i-1] = rs.getObject( i );
							}
							counter++;
						}

						setTableModel( columnValues, columnNames );
					}
					else if( commands.getCommandType() == Commands.USE ){
						//todo test this
						db.getConnection().setCatalog( commands.getCommand().replaceAll( "(?i:use\\s+[\\`|\\'|\\\"]?(.+?)[\\`|\\'|\\\"]?;?$)", "$1" ) );
					}
					else {
						//going to run edits
						Statement statement = db.getNewStatement();
						statement.executeUpdate( commands.getCommand() );
					}
				}


			} catch ( SQLException e ) {
				e.printStackTrace();
			}
			db.close();
		}
	}

	private void setEmptyTable(){
		String[] colHeadings = {"No Data"};
		dataModel = new DefaultTableModel( 0, colHeadings.length );
		dataModel.setColumnIdentifiers( colHeadings );
	}

	private void setTableModel( Object[][] data, Object[] columns ) {
		dataModel = new DefaultTableModel(data, columns);
		table.setModel(dataModel);
	}
}
