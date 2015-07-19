//uniform mat4 u_Matrix;
/*uniform mat4 u_MVMatrix;
uniform mat4 u_IT_MVMatrix;
uniform mat4 u_MVPMatrix;

uniform vec3 u_Filtering;
uniform vec3 u_Ambient;
uniform vec3 u_Diffuse;
uniform vec3 u_Specular;
uniform float u_Dissolve;

attribute vec3 a_Position;

vec3 getAmbient();
vec3 getDiffuse();
vec3 getSpecular();

void main() {
//    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_Matrix * vec4(a_Position, 1.0);
}

vec3 getAmbient() {
    return u_Ambient;
}*/

attribute vec4 aVertex;
attribute vec3 aNormal;
attribute vec2 aTexture;

// View Matrices
uniform mat4 uProjectionMatrix;
uniform mat4 uModelViewMatrix;
uniform mat3 uNormalMatrix;

// Output to Fragment Shader
varying vec3 vNormal;
varying vec2 vTexture;

void main(void)
{
    vNormal     = uNormalMatrix * aNormal;
    vTexture    = aTexture;

    gl_Position = uProjectionMatrix * uModelViewMatrix * aVertex;
}
