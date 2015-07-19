package com.thea.itailor.utils;

import android.util.Log;

import static android.opengl.GLES30.GL_COMPILE_STATUS;
import static android.opengl.GLES30.GL_FRAGMENT_SHADER;
import static android.opengl.GLES30.GL_LINK_STATUS;
import static android.opengl.GLES30.GL_VALIDATE_STATUS;
import static android.opengl.GLES30.GL_VERTEX_SHADER;
import static android.opengl.GLES30.glAttachShader;
import static android.opengl.GLES30.glCompileShader;
import static android.opengl.GLES30.glCreateProgram;
import static android.opengl.GLES30.glCreateShader;
import static android.opengl.GLES30.glDeleteProgram;
import static android.opengl.GLES30.glDeleteShader;
import static android.opengl.GLES30.glGetProgramInfoLog;
import static android.opengl.GLES30.glGetProgramiv;
import static android.opengl.GLES30.glGetShaderInfoLog;
import static android.opengl.GLES30.glGetShaderiv;
import static android.opengl.GLES30.glLinkProgram;
import static android.opengl.GLES30.glShaderSource;
import static android.opengl.GLES30.glValidateProgram;

/**
 * Created by Thea on 2015/6/30.
 */
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";
//    private static final int GL_VERTEX_SHADER = 1;
//    private static final int GL_FRAGMENT_SHADER = 2;

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            Log.w(TAG, "Could not create new shader.");
            return 0;
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0);
        Log.v(TAG, "Results of compiling source:\n" + shaderCode + "\n:"
                + glGetShaderInfoLog(shaderId));
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderId);
            Log.w(TAG, "Compilation of shader failed.");
            return 0;
        }

        return shaderId;
    }

    public static int linkProgram(int vertexShader, int fragmentShader) {
        final int programId = glCreateProgram();
        if (programId == 0) {
            Log.w(TAG, "Could not create new program.");
            return 0;
        }

        glAttachShader(programId, vertexShader);
        glAttachShader(programId, fragmentShader);
        glLinkProgram(programId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
        Log.v(TAG, "Results of linking program:\n" + glGetProgramInfoLog(programId));
        if (linkStatus[0] == 0) {
            glDeleteProgram(programId);
            Log.w(TAG, "Linking of program failed.");
            return 0;
        }

        return programId;
    }

    public static boolean validateProgram(int programId) {
        glValidateProgram(programId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0]
                + "\n: " + glGetProgramInfoLog(programId));

        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;

        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        program = linkProgram(vertexShader, fragmentShader);

        return program;
    }
}
