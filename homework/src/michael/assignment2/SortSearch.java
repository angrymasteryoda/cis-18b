package michael.assignment2;

/**
 * Created By: Michael Risher
 * Date: 3/4/15
 * Time: 4:22 PM
 */
public class SortSearch extends BaseSort {

	public SortSearch( int length ){
		super( length );
	}

	public int search( int value ){
		return binarySearch( this.sortArr, value, 0, this.sortArr.length -1 );
	}
	
	private int binarySearch( int[] array, int value, int start, int end ){
		if ( ( end - start ) <= 1 ) { // 1 or 2 elems
			if( array[start] == value ) {
				return start;
			}
			if ( array[end] == value ) {
				return end;
			}
			return -1;
		}
		int mid = ( start + end ) / 2;
		if ( array[mid] > value ) {
			return binarySearch( array, value, 0, mid );
		} else {
			return binarySearch( array, value, mid, end );
		}
	}

}
