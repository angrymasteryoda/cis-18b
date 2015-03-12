package engine;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by: Michael Risher
 * Date: 3/10/15
 * Time: 6:04 PM
 */
public class Renderer {

	/**
	 * once every frame will prepare opengl to render
	 */
	public void prepare(){
		GL11.glClear( GL11.GL_COLOR_BUFFER_BIT ); //clears the color from prevoius frame
		GL11.glClearColor( 1, 0, 0, 1 ); //background
	}

	/**
	 * render a raw model
	 * @param texturedModel model we want to render
	 */
	public void render( TexturedModel texturedModel ){
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray( model.getVaoId() );//bind the vao so we can do stuff to it
		GL20.glEnableVertexAttribArray( 0 );
		GL20.glEnableVertexAttribArray( 1 );
		GL13.glActiveTexture( GL13.GL_TEXTURE0 );
		GL11.glBindTexture( GL11.GL_TEXTURE_2D, texturedModel.getTexture().getTextureId() );
		// type of rendering tris vs quads, offset, vertex count
		GL11.glDrawElements( GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0 );
		GL20.glDisableVertexAttribArray( 0 );
		GL20.glDisableVertexAttribArray( 1 );
		GL30.glBindVertexArray( 0 );//unbind it
	}
}
