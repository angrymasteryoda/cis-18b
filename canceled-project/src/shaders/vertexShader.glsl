#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 passTextureCoords;

void main( void ) {
	// position.xyz same as position.x, position.y, position.z
	gl_Position = vec4( position.xyz, 1.0 );
	passTextureCoords = textureCoords;
}