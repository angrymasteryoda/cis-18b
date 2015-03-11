package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 2/27/15
 * Time: 6:23 PM
 */
public class DisplayManger {
	private static int width = 1280;
	private static int height = 720;
	private static int fpsCap = 120;

	public static void createDisplay(){
		ContextAttribs attribs = new ContextAttribs( 3,2 );
		attribs.withForwardCompatible( true );
		attribs.withProfileCore( true );
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			Display.create( new PixelFormat(  ), attribs );
		} catch ( LWJGLException e ) {
			e.printStackTrace();
		}

		GL11.glViewport( 0, 0, width, height );
	}

	public static void updateDisplay(){
		Display.sync( fpsCap );
		Display.update();
	}

	public static void closeDisplay(){
		Display.destroy();
	}

}
