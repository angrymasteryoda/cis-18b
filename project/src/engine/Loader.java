package engine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Michael Risher
 * Date: 3/10/15
 * Time: 5:41 PM
 */
public class Loader {
	/*
	vba attrib info
	0 : position
	1 : texture coords (UV's)
	 */

	//for memory management so we can delete vaos and vbos
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	/**
	 * take in positions
	 * @param positions
	 * @return
	 */
	public RawModel loadToVAO( float[] positions,float[] textureCoords, int[] indices ){
		int vaoId = createVAO();
		bindIndicesBuffer( indices );
		storeDataInAttrList( 0, 3, positions );
		storeDataInAttrList( 1, 2, textureCoords );
		unbindVao();
		return new RawModel( vaoId, indices.length ); //divide by 3 because each vertex is  (x,y,z)
	}

	/**
	 * load a texture into opengl
	 * @param file file of texture
	 * @return return texture id
	 */
	public int loadTexture( String file ){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture( "PNG", new FileInputStream( "res/" + file + ".png" ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		int textId = texture.getTextureID();
		textures.add( textId );
		return textId;
	}

	/**
	 * for when we close the game this will remove the vbos, vbas
	 */
	public void cleanUp(){
		for ( int vao : vaos ){
			GL30.glDeleteVertexArrays( vao );
		}
		for ( int vbo : vbos ){
			GL15.glDeleteBuffers( vbo );
		}

		for( int texture : textures ){
			GL11.glDeleteTextures( texture );
		}

	}

	/**
	 * create an empty vao
	 * @return id of vao
	 */
	private int createVAO(){
		int vaoId = GL30.glGenVertexArrays(); //create the empty vao
		vaos.add( vaoId );
		GL30.glBindVertexArray( vaoId ); //activate it so we can use it
		return vaoId; //return id
	}

	/**
	 * store the data into the attrib list of the vao
	 * @param attrNumber
	 * @param coordSize
	 * @param data
	 */
	private void storeDataInAttrList( int attrNumber, int coordSize, float[] data ){
		int vboId = GL15.glGenBuffers(); //create empty vbo
		vbos.add( vboId );
		GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, vboId ); //bind it so we can use it
		FloatBuffer buffer = storeDataInFloatBuffer( data ); //convert our data
		// GL15.GL_STATIC_DRAW mean we will never edit it again
		GL15.glBufferData( GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW );// store the data into the vbo
		/* number of the attrib list in which you want to store the data,
		* length of each vertex (x,y,z),
		* type of data,
		* if normalized,
		* if you have data bewteen our data,
		* offset where it should start
		*/
		GL20.glVertexAttribPointer( attrNumber, 3, GL11.GL_FLOAT, false, 0, 0 );

		GL20.glVertexAttribPointer( attrNumber, coordSize, GL11.GL_FLOAT, false, 0, 0 );
		GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, 0 ); //unbind the current vbo
	}

	/**
	 * unbinds the vao when done using it
	 */
	private void unbindVao(){
		GL30.glBindVertexArray( 0 );//will unbound the currently bound vao
	}

	/**
	 * create a indices buffer
	 * @param indices
	 */
	private void bindIndicesBuffer( int[] indices ){
		int vboId = GL15.glGenBuffers(); //create vbo
		vbos.add( vboId );
		GL15.glBindBuffer( GL15.GL_ELEMENT_ARRAY_BUFFER, vboId ); //bind the vbo
		IntBuffer buffer = storeDataInIntBuffer( indices );
		GL15.glBufferData( GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW );
	}

	/**
	 * convert int array into a int buffer
	 * @param data
	 * @return
	 */
	private IntBuffer storeDataInIntBuffer( int[] data ){
		IntBuffer buffer = BufferUtils.createIntBuffer( data.length );
		buffer.put( data ); //put the data into the buffer
		buffer.flip(); //prepare it to be read from we finished writing to it
		return buffer;
	}

	/**
	 * convert float array into a float buffer
	 * @param data
	 * @return
	 */
	private FloatBuffer storeDataInFloatBuffer( float[] data ){
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer( data.length );
		floatBuffer.put( data ); //put the data into the buffer
		floatBuffer.flip(); //prepare it to be read from we finished writing to it
		return floatBuffer;
	}
}
