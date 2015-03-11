package game;

import engine.DisplayManger;
import org.lwjgl.opengl.Display;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 2/27/15
 * Time: 9:07 PM
 */
public class MainGameLoop {

	public static void main( String[] args ){
		DisplayManger.createDisplay();

		while( !Display.isCloseRequested() ){
			DisplayManger.updateDisplay();
		}

		DisplayManger.closeDisplay();
	}
}
