package tk.michael.project.gui;

import tk.michael.project.db.MysqlDatabase;
import tk.michael.project.util.Database;

import javax.swing.tree.DefaultMutableTreeNode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created By: Michael Risher
 * Date: 5/6/15
 * Time: 6:16 PM
 */
public class DatabaseTreeNode extends DefaultMutableTreeNode {
	private Database database;
	private DefaultMutableTreeNode tableNode;
	private ArrayList<DefaultMutableTreeNode> tables;
	private final String dbName;

	public DatabaseTreeNode( Object userObject, Database db ) {
		super( userObject );
		this.dbName = (String) userObject;
		this.database = db;

		tables = new ArrayList<>();
		tableNode = new DefaultMutableTreeNode( "Tables" );
		getTables();

		add( tableNode );
	}

	private void getTables(){
		//todo fix this its broke
		ArrayList<String> tableNames = new ArrayList<>();
		MysqlDatabase db = new MysqlDatabase( this.database );
		db.open();
		Statement statement = null;
		try {
			db.getConnection().setCatalog( dbName );
			statement = db.getConnection().createStatement();
			ResultSet res = statement.executeQuery( "SHOW TABLES" );
			while ( res.next() ){
				tableNames.add( res.getString( 1 ) );
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		db.close();

		for ( int i = 0; i < tableNames.size(); i++ ){
		    tables.add( new DefaultMutableTreeNode( tableNames.get( i ) ) );
		}

		for ( int i = 0; i < tables.size(); i++ ){
		    tableNode.add( tables.get( i ) );
		}

	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase( Database database ) {
		this.database = database;
	}

	public DefaultMutableTreeNode getTableNode() {
		return tableNode;
	}

	public void setTableNode( DefaultMutableTreeNode tableNode ) {
		this.tableNode = tableNode;
	}

	public String getDbName() {
		return dbName;
	}
}
