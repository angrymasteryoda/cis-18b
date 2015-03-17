package shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by: Michael Risher
 * Date: 3/10/15
 * Time: 7:03 PM
 */
public abstract class ShaderProgram {
	private int programId;
	private int vertexShaderId;
	private int fragmentShaderId;

	protected ShaderProgram( String vertexFile, String fragmentFile ) {
		vertexShaderId = loadShader( vertexFile, GL20.GL_VERTEX_SHADER );
		fragmentShaderId = loadShader( fragmentFile, GL20.GL_FRAGMENT_SHADER );
		programId = GL20.glCreateProgram(); //create a new program
		GL20.glAttachShader( programId, vertexShaderId ); //attach the vertex shader to program
		GL20.glAttachShader( programId, fragmentShaderId ); //attach the fragment shader to program
		GL20.glLinkProgram( programId );
		GL20.glValidateProgram( programId ); //validate it
		bindAttributes();
	}

	/**
	 * stop the program
	 */
	public void stop(){
		GL20.glUseProgram( 0 );
	}

	/**
	 * start the program
	 */
	public void start(){
		GL20.glUseProgram( programId );
	}

	/**
	 * housekeeping
	 */
	public void cleanup(){
		stop();
		GL20.glDetachShader( programId, vertexShaderId ); //unlink the vert and frag shader
		GL20.glDetachShader( programId, fragmentShaderId );
		GL20.glDeleteShader( vertexShaderId ); //delete the vertex and frag shader
		GL20.glDeleteShader( fragmentShaderId );
		GL20.glDeleteProgram( programId );//delete the program
	}

	/**
	 * going to take an attributes from the model and use it as input for the shader file
	 */
	protected abstract void bindAttributes();

	protected void bindAttribute( int attrib, String variableName ){
		GL20.glBindAttribLocation( programId, attrib, variableName );
	}

	/**
	 * load up shader source code file
	 * @param file shader file
	 * @return
	 */
	private static int loadShader( String file, int type ) {
		StringBuilder shaderSrc = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader( new FileReader( file ) );
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				shaderSrc.append( line ).append( "\n" );
			}
			reader.close();
		} catch ( IOException e ) {
			System.err.println( "could not read: " + file );
			e.printStackTrace();
			System.exit( 255 );
		}

		int shaderId = GL20.glCreateShader( type );
		GL20.glShaderSource( shaderId, shaderSrc );

		GL20.glCompileShader( shaderId );
		if ( GL20.glGetShader( shaderId, GL20.GL_COMPILE_STATUS ) == GL11.GL_FALSE ) {
			System.out.println( GL20.glGetShaderInfoLog( shaderId, 500 ) );
			System.err.println( "could not compile shader: " + file );
			System.exit( 254 );
		}
		return shaderId;
	}
}
