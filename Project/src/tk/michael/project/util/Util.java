package tk.michael.project.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 4/27/15
 * Time: 3:51 PM
 */
public class Util {

	public static int[] getDuplicatesFromStrings( final String[] input ){
		Set set = new HashSet();
		int[] tempDupId = new int[ input.length ];
		int j = 0;
		for ( int i = 0; i < input.length; i++ ) {
			if ( !set.add( input[i] ) ) {
				tempDupId[j++] = i;
			}
		}
		int[] result = new int[ j ];
		for ( int i = 0; i < result.length; i++ ) {
			result[i] = tempDupId[i];
		}
		return result;
	}

	/**
	 * get the current UNIX timestamp
	 * @return timestamp
	 */
	public static long getUnix(){
		return System.currentTimeMillis() / 1000L;
	}

	/**
	 * Converts Unix time to Calendar instance.
	 * @return calendar object
	 */
	public static Calendar unixToCalendar(long unixTime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(unixTime);
		return calendar;
	}
}
