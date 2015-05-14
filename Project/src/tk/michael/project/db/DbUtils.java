package tk.michael.project.db;

/**
 * Created By: Michael Risher
 * Date: 5/13/15
 * Time: 3:37 PM
 */
public class DbUtils {

	/**
	 * prepend table prefix
	 * @param name
	 * @return
	 */
	public static String prependTable( String name ){
		return "mr2358174_" + getType( name ) + name;
	}

	public static String getType( String name ){
		switch ( name ) {
			case "users" :
			case "sessions" :
			case "databases" : return TableTypes.E.toString();
		}
		return "";
	}
}
