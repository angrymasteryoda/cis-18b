package models;

import textures.ModelTexture;

/**
 * Created by: Michael Risher
 * Date: 3/10/15
 * Time: 7:33 PM
 */
public class TexturedModel {
	private RawModel rawModel;
	private ModelTexture texture;

	public TexturedModel( RawModel rawModel, ModelTexture texture ) {
		this.rawModel = rawModel;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
