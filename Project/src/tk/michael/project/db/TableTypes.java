package tk.michael.project.db;

/**
 * Created By: Michael Risher
 * Date: 5/13/15
 * Time: 3:39 PM
 */
public enum TableTypes {
	E( "entity_" ),
	X( "xref_" ),
	En( "enum_" );


	private String text;

	TableTypes( String text ) {
		this.text = text;
	}

	public String toString() {
		return text;
	}
}

