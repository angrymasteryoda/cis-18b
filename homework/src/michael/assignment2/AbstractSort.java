package michael.assignment2;

/**
 * Created By: Michael Risher
 * Date: 2/25/15
 * Time: 6:50 PM
 */
public abstract class AbstractSort {
	protected int[] array;

	abstract void fillArray();

	public void plainFill(){
		for ( int i = 0; i < array.length; i++ ){
		    array[i] = i;
		}
	}
}
