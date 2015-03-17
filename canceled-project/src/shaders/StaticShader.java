package shaders;

/**
 * Created by: Michael Risher
 * Date: 3/10/15
 * Time: 7:21 PM
 */
public class StaticShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "src/shaders/vertexShader.glsl";
	private static final String FRAGMENT_SHADER = "src/shaders/fragmentShader.glsl";

	public StaticShader() {
		super( VERTEX_SHADER, FRAGMENT_SHADER );
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute( 0, "position" );
		super.bindAttribute( 1, "textureCoords" );

	}
}
