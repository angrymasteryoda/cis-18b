package michael;

import michael.assignment2.BaseSort;
import michael.assignment2.SortSearch;

import javax.swing.*;

/**
 * Created By: Michael Risher
 * Date: 2/25/15
 * Time: 6:52 PM
 */
public class Main {

	public static void main( String[] args ){
		SortSearch bs = new SortSearch( 100 );
		bs.sort();
		bs.print();
		Main.println( bs.search( 30 ) );

	}

	public static <T> void println( T out ){
		System.out.println( out );
	}

	public static <T> void print( T out ){
		System.out.print( out );
	}

	public static <T> void printlnErr( T out ){
		System.err.println( out );
	}

	public static <T> void printErr( T out ){
		System.err.print( out );
	}

	public static <T> void arrayPrinter( T[] t ){
		for (int i = 0; i < t.length; i++) {
			System.out.println(t[i]);
		}
	}
}
