package tk.michael.project.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 5/28/15
 * Time: 4:17 AM
 */
public class WindowManager {
	private static ArrayList<UUID> windowIds = new ArrayList<>();
	private static HashMap<UUID, JFrame> frames = new HashMap<>();

	/**
	 * open a new window
	 *
	 * @param id id of window
	 * @return true if opened, false if cant open
	 */
	public static boolean open( UUID id , JFrame frame ) {
		if ( windowIds.contains( id ) ) {
			return false;
		}

		windowIds.add( id );
		frames.put( id, frame );
		return true;
	}

	/**
	 * close a window
	 *
	 * @param id id of window
	 * @return true if closed, false if not found
	 */
	public static boolean close( UUID id ) {
		if ( windowIds.contains( id ) ) {
			int index = windowIds.indexOf( id );
			windowIds.remove( index );
			frames.remove( id );
			return true;
		} else {
			return false;
		}
	}

	/**
	 * close all open windows
	 */
	public static void closeAll(){
		for ( int i = 0; i < windowIds.size(); i++ ){
		    UUID id = windowIds.get( i );
			frames.get( id ).dispose();
			frames.remove( id );
		}
		windowIds.clear();
	}

	/**
	 * get count of opened windows
	 * @return number of opened windows
	 */
	public static int windowsOpened(){
		return windowIds.size();
	}
}
