package models;

/**
 * Created by: Michael Risher
 * Date: 3/10/15
 * Time: 5:40 PM
 */
public class RawModel {
	private int vaoId;
	private int vertexCount;

	public RawModel( int id, int vertexCount ) {
		this.vaoId = id;
		this.vertexCount = vertexCount;
	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}

}
