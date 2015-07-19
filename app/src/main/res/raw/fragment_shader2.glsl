precision mediump float;

uniform vec3 u_Filtering;
uniform vec3 u_Ambient;
uniform vec3 u_Diffuse;
uniform vec3 u_Specular;
uniform float u_Dissolve;

vec4 u_Color;
//uniform sampler3D u_TextureUnit;
//varying vec3 v_TextureCoordinates;
vec3 getAmbientLighting();
vec3 getDiffuseLighting();
vec3 getSpecularLighting();

varying mediump vec3 vNormal;
varying mediump vec2 vTexture;

// MTL Data
uniform lowp vec3 uAmbient;
uniform lowp vec3 uDiffuse;
uniform lowp vec3 uSpecular;
uniform highp float uExponent;

uniform lowp int uMode;
uniform lowp vec3 uColor;
uniform sampler2D uTexture;

void main() {
//    gl_FragColor = texture3D(u_TextureUnit, v_TextureCoordinates);

    lowp vec3 material = modelColor();
    gl_FragColor = vec4(material, 1.0);
}

lowp vec3 materialDefault(highp float df, highp float sf)
{
    lowp vec3 ambient = vec3(0.5);
    lowp vec3 diffuse = vec3(0.5);
    lowp vec3 specular = vec3(0.0);
    highp float exponent = 1.0;

    sf = pow(sf, exponent);

    return (ambient + (df * diffuse) + (sf * specular));
}

lowp vec3 materialMTL(highp float df, highp float sf)
{
    sf = pow(sf, uExponent);

    return (uAmbient + (df * uDiffuse) + (sf * uSpecular));
}

lowp vec3 modelColor(void)
{
    highp vec3 N = normalize(vNormal);
    highp vec3 L = vec3(1.0, 1.0, 0.5);
    highp vec3 E = vec3(0.0, 0.0, 1.0);
    highp vec3 H = normalize(L + E);

    highp float df = max(0.0, dot(N, L));
    highp float sf = max(0.0, dot(N, H));

    // Default
    if(uMode == 0)
        return materialDefault(df, sf);

    // Texture
    else if(uMode == 1)
        return (materialDefault(df, sf) * vec3(texture2D(uTexture, vTexture)));

    // Material
    else if(uMode == 2)
        return materialMTL(df, sf);

    return vec3(0);
}
