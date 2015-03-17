package michael.assignment2;

import michael.Main;

import java.util.Arrays;
import java.util.Random;

/**
 * Created By: Michael Risher
 * Date: 2/25/15
 * Time: 6:50 PM
 */
public class BaseSort extends AbstractSort {
	protected int[] sortArr;

	public BaseSort( int length ) {
		this.array = new int[length];
		this.sortArr = new int[length];
		fillArray();
		System.arraycopy( array, 0, sortArr, 0, array.length );
	}

	@Override
	public void fillArray() {
		Random rand = new Random();
		for ( int i = 0; i < array.length; i++ ) {
			array[i] = rand.nextInt( ( 99 - 10 ) + 1 ) + 10;
		}
	}

	public void sort() {
		quickSort( sortArr, 0, array.length - 1 );
	}

	protected void quickSort( int[] array, int start, int end ){
		int i = start; //left-right scan index
		int k = end; //right-left scan index

		if ( end - start >= 1 ) { //make sure atleast 2
			int pivot = array[start];

			while ( k > i ){ //while the scanning indexs have not meet
				while ( array[i] <= pivot && i <+ end && k > i ) { // form the left look for first element greater than pivot
					i++;
				}
				while ( array[k] > pivot && k >= start && k >= i ){ //from the right look for the first elem not greater than pivot
					k--;
				}
				if ( k > i ) { // if the left scan index is still smaller than right index, swap the elems
					swap( array, i, k );
				}
			}
			swap( array, start, k ); //after indices have crossed swap the last elem in the left partition with pivot
			quickSort( array, start, k - 1 ); // quicksort the left part
			quickSort( array, k + 1, end ); //quicksort the right part
		} else {
			return; //its sorted
		}
	}

	private void swap( int array[], int index1, int index2 ) {
		int temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}

	public void print(){
		Main.println( "unsorted" );
		Main.println( Arrays.toString( array ) );
		Main.println( "sorted" );
		Main.println( Arrays.toString( sortArr ) );
	}
}


