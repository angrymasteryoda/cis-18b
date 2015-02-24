package michael.assignment1;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 2/23/15
 * Time: 1:29 PM
 */
public class Rectangle {

	protected float length;
	protected float width;

	public Rectangle( float width, float length  ) {
		if ( !setLength( length ) || !setWidth( width )) {
			Main.Pl( "Rectangle sides must be larger than 0.0 and less than 20.0" );
		}
	}

	public Rectangle() {
		this( 1f, 1f );
	}

	public float getPerimeter(){
		return ( 2 * length ) + ( 2 * width );
	}

	public float getArea(){
		return width * length;
	}

	public float getLength() {
		return length;
	}

	public boolean setLength( float length ) {
		if( length > 0f && length < 20f){
			this.length = length;
			return true;
		}
		return false;
	}

	public float getWidth() {
		return width;
	}

	public boolean setWidth( float width ) {
		if( width > 0f && width < 20f){
			this.width = width;
			return true;
		}
		return false;
	}
}
