uniform mat4 u_Matrix;

attribute vec3 a_Position;
attribute vec3 a_Normal;

varying vec3 v_Normal;
//attribute vec3 a_TextureCoordinates;

//varying vec3 v_TextureCoordinates;
void main() {
//    v_TextureCoordinates = a_TextureCoordinates;
    v_Normal = a_Normal;
    gl_Position = u_Matrix * vec4(a_Position, 1.0);
}
