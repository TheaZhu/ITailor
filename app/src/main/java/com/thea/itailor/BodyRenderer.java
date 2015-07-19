package com.thea.itailor;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.thea.itailor.entities.LocationParams;
import com.thea.itailor.entities.TDModel;
import com.thea.itailor.utils.MatrixHelper;
import com.thea.itailor.utils.OBJParser;
import com.thea.itailor.utils.ShaderHelper;
import com.thea.itailor.utils.TextResourceReader;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LEQUAL;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glClearDepthf;
import static android.opengl.GLES20.glDepthFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;


/**
 * Created by Thea on 2015/7/8.
 */
public class BodyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "BodyRenderer";

    private static final String U_MATRIX = "u_Matrix";
    private static final String U_COLOR = "u_Color";
    private static final String U_FILTERING = "u_Filtering";
    private static final String U_AMBIENT = "u_Ambient";
    private static final String U_DIFFUSE = "u_Diffuse";
    private static final String U_SPECULAR = "u_Specular";
    private static final String U_DISSOLVE = "u_Dissolve";
    private static final String U_EXPONENT = "u_Exponent";
    private static final String A_POSITION = "a_Position";
    private static final String A_NORMAL = "a_Normal";

    private static final float TOUCH_SCALE = 0.4f;

    private final float[] modelMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelProjectionMatrix = new float[16];

    private Context context;
    private OBJParser objParser;
    private TDModel model;

    private int program;
    private int uMatrixLocation;
    private int uColorLocation;
    private int uFilteringLocation;
    private int uAmbientLocation;
    private int uDiffuseLocation;
    private int uSpecularLocation;
    private int uDissolveLocation;
    private int uExponentLocation;
    private int aPositionLocation;
    private int aNormalLocation;

    private float z = 1200f;
    private float xRotation = 0f, yRotation = 0f;
    private float previousX, previousY;

    public BodyRenderer(Context context) {
        this.context = context;
        objParser = new OBJParser(context);
        model = objParser.parser("newgirl.obj");
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 1f);
        glClearDepthf(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.fragment_shader);

        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);
        glUseProgram(program);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        uFilteringLocation = glGetUniformLocation(program, U_FILTERING);
        uAmbientLocation = glGetUniformLocation(program, U_AMBIENT);
        uDiffuseLocation = glGetUniformLocation(program, U_DIFFUSE);
        uSpecularLocation = glGetUniformLocation(program, U_SPECULAR);
        uDissolveLocation = glGetUniformLocation(program, U_DISSOLVE);
        uExponentLocation = glGetUniformLocation(program, U_EXPONENT);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);

        glBindBuffer(GL_ARRAY_BUFFER, model.genVertexDataBuffers());
        glVertexAttribPointer(aPositionLocation, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(aPositionLocation);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

//        glBindBuffer(GL_ARRAY_BUFFER, model.genNormalBuffers());
//        glVertexAttribPointer(aNormalLocation, 3, GL_FLOAT, false, 0, 0);
//        glEnableVertexAttribArray(aNormalLocation);
//        glBindBuffer(GL_ARRAY_BUFFER, 0);

        model.setNormalPointer(aNormalLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 0.1f, 2000f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, -450f, -z);
//        rotateM(modelMatrix, 0, xRotation, 1f, 0f, 0f);
        rotateM(modelMatrix, 0, yRotation, 0f, 1f, 0f);

        multiplyMM(modelProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelProjectionMatrix, 0);

//        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);

        LocationParams params = new LocationParams(aNormalLocation, uFilteringLocation,
            uAmbientLocation, uDiffuseLocation, uSpecularLocation, uDissolveLocation, uExponentLocation);
        model.draw(params);

    }

    public void handleTouchDrag(float x, float y, int upperArea) {
        Log.i(TAG, "handleTouchDrag");
        float dx = x - previousX;
        float dy = y - previousY;

        if( y < upperArea)
            z -= dx * TOUCH_SCALE / 2;
        else {
            xRotation += dy * TOUCH_SCALE;
            yRotation += dx * TOUCH_SCALE;
        }

        previousX = x;
        previousY = y;

    }

    public void handleTouchPress(float x, float y) {
        previousX = x;
        previousY = y;
    }
}
