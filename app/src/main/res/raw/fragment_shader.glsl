precision mediump float;

uniform vec3 u_Filtering;
uniform vec3 u_Ambient;
uniform vec3 u_Diffuse;
uniform vec3 u_Specular;
uniform float u_Dissolve;
uniform float u_Exponent;

//uniform sampler2D u_TextureUnit;
//
//varying vec3 v_TextureCoordinates;
varying vec3 v_Normal;

highp float df;
highp float sf;

vec3 getAmbientLighting();
vec3 getDiffuseLighting();
vec3 getSpecularLighting();

void main() {
//    gl_FragColor = texture3D(u_TextureUnit, v_TextureCoordinates);
//    vec3 material = (0.2 * u_Ambient + 0.4 * u_Diffuse + 0.4 * u_Specular) * u_Dissolve;
    vec3 material = u_Ambient;
    highp vec3 N = normalize(v_Normal);
    highp vec3 L = vec3(0.4, 0.3, 0.3);
//    highp vec3 E = vec3(0.2, 0.0, 0.2);
//    highp vec3 H = normalize(L + E);
//    df = max(0.0, dot(N, L));
    df = max(0.0, 0.4);
//    sf = pow(max(0.0, dot(N, H)), u_Exponent);
    sf = pow(0.9, u_Exponent);
    material += getDiffuseLighting();
    material += getSpecularLighting();
    material *= u_Dissolve;
    gl_FragColor = vec4(material, 1.0);
}

vec3 getAmbientLighting() {
    return u_Ambient * 0.1;
}

vec3 getDiffuseLighting() {
    return u_Diffuse * df;
}

vec3 getSpecularLighting() {
    return u_Specular * sf;
}
