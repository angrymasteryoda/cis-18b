package tk.michael.project.db;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 5/22/15
 * Time: 6:04 PM
 */
public class Commands {

	public static final int EDIT = 0;
	public static final int SELECT = 1;
	public static final int USE = 2;

	private final String raw;
	private ArrayList<String> commands;
	private int index = -1;

	/**
	 * Mysql commands made into a iterator
	 * @param raw all the commands entered by user
	 */
	public Commands( String raw ) {
		this.commands = new ArrayList<>();
		this.raw = raw;
		read();
	}

	/**
	 * Read the raw commands and split them up based on semicolons
	 */
	private void read(){
		boolean dQuote = false;
		boolean sQuote = false;
		boolean aQuote = false;
		boolean comment = false;
		boolean inlineComment = false;
		int rightIndex = 0;
		int leftIndex = 0;
		int arrayOrder = 0; //to verify inserted in order
		final int length = raw.length();

		for ( int i = 0; i < length; i++ ) {
			char current = raw.charAt( i );
			String lookahead = "";
			if ( length <= ( i + 3 ) ) {
				lookahead = raw.substring( i, length );
			} else{
				lookahead = raw.substring( i, i + 3 );
			}

			if( lookahead.startsWith( "/*" ) ) {
				inlineComment = true;
			} else if ( inlineComment ) {
				if ( lookahead.startsWith( "*/" ) ) {
					inlineComment = false;
					rightIndex = i + 2;
				}
			} else if ( current == '#' || lookahead.startsWith( "-- " ) ) {
				comment = true;
			} else if( comment ){ //look for newline
				if ( current == '\n' ) {
					comment = false;
					rightIndex = i + 1;
				}
			} else if ( current == '\'' ) {
				if ( sQuote ) { //we found the ending quote
					sQuote = false;
				} else {
					sQuote = true;
				}
			} else if( current == '"' ){
				if ( dQuote ) { //we found the ending quote
					dQuote = false;
				} else {
					dQuote = true;
				}
			} else if ( current == '`' ) {
				if ( aQuote ) { //we found the ending quote
					aQuote = false;
				} else {
					aQuote = true;
				}
			} else if ( current == ';' && ( !dQuote && !sQuote && !aQuote ) ){
				leftIndex = i + 1;
				String temp = raw.substring( rightIndex, leftIndex );
				commands.add( arrayOrder++, temp.trim() );
				rightIndex = leftIndex + 1;
			}
		}

		//check of invalid stuff as best i can
		removeInvalid();
	}

	/**
	 * remove invalid commands that happen
	 */
	private void removeInvalid() {
		for ( int i = 0; i < commands.size(); i++ ) {
			if ( commands.get( i ).matches( "^;$" ) ) {
				commands.remove( i );
				i--;
			}
		}
	}

	/**
	 * move iterator to next position
	 * @return true if has a next, false if there isnt a next
	 */
	public boolean next(){
		try {
			commands.get( ++index );
			return true;
		} catch ( Exception e ){
			return false;
		}
	}

	/**
	 * move the iterator to the first index
	 */
	public void first(){
		index = 0;
	}

	/**
	 * move the iterator to before the first index
	 */
	public void beforeFirst(){
		index = -1;
	}

	/**
	 * move the iterator to the last index
	 */
	public void last(){
		index = commands.size();
	}

	/**
	 * get the command at the iterator
	 * @return string of the command
	 */
	public String getCommand(){
		return commands.get( index );
	}

	/**
	 * get the type of command select, use, other
	 * @return type of command see constants
	 */
	public int getCommandType(){
		String s = commands.get( index );
		if ( s.matches( "(?i:select.+?$)" ) ) {
			return SELECT;
		}
		else if( s.matches( "(?i:use.+?$)" ) ) {
			return USE;
		}
		else{
			return EDIT;
		}
	}

	/**
	 * get all commands in arraylist
	 * @return commands
	 */
	public ArrayList<String> getCommands() {
		return commands;
	}

	/**
	 * get the raw command string inputted
	 * @return raw command string
	 */
	public String getRaw() {
		return raw;
	}
}
