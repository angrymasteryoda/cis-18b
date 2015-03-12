import engine.DisplayManger;
import engine.Loader;
import models.RawModel;
import engine.Renderer;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import shaders.StaticShader;
import textures.ModelTexture;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 2/27/15
 * Time: 9:07 PM
 */
public class MainGameLoop {

	public static void main( String[] args ){
		DisplayManger.createDisplay();

		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		//opengl expects vertices to be defined counter clockwidse by default
		float[] vertices = {
				-0.5f, 0.5f, 0f,  //V0
				-0.5f, -0.5f, 0f, //V1
				0.5f, -0.5f, 0f,  //V2
				0.5f, 0.5f, 0f,   //V3
		};

		int[] indices = {
				0, 1, 3,
				3, 1, 2
		};

		float[] textCoords = {
				0,0, //v0
				0,1, //v1
				1,1, //v2
				1,0, //v3
		};

		RawModel model = loader.loadToVAO( vertices, textCoords, indices );
		ModelTexture texture = new ModelTexture( loader.loadTexture( "sunset" ) );
		TexturedModel texturedModel = new TexturedModel( model, texture );


		while( !Display.isCloseRequested() ){
			renderer.prepare(); //prepare the screen
			shader.start(); //must run before render
			renderer.render( texturedModel );
			shader.stop(); //must run after the rendering

			DisplayManger.updateDisplay();
		}

		shader.cleanup();
		loader.cleanUp(); //clean up the mess
		DisplayManger.closeDisplay();
	}
}
