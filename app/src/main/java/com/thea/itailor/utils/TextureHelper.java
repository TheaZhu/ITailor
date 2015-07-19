package com.thea.itailor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;

import static android.opengl.GLES30.GL_LINEAR;
import static android.opengl.GLES30.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES30.GL_TEXTURE_3D;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import static android.opengl.GLES30.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES30.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glDeleteTextures;
import static android.opengl.GLES30.glGenTextures;
import static android.opengl.GLES30.glGenerateMipmap;
import static android.opengl.GLES30.glTexParameteri;

/**
 * Created by Thea on 2015/7/2.
 */
public class TextureHelper {
    private static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, String fileName) {
        final int[] textureIds = new int[1];
        glGenTextures(1, textureIds, 0);

        if (textureIds[0] == 0) {
            Log.w(TAG, "Could not generate a new OpenGL texture object.");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(TAG, "Resource ID " + fileName + " could not be decoded.");

            glDeleteTextures(1, textureIds, 0);
            return 0;
        }

        glBindTexture(GL_TEXTURE_3D, textureIds[0]);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GLUtils.texImage2D(GL_TEXTURE_3D, 0, bitmap, 0);
        glGenerateMipmap(GL_TEXTURE_3D);

        glBindTexture(GL_TEXTURE_3D, 0);
        bitmap.recycle();
        return textureIds[0];
    }

    public static int loadCubeMap(Context context, int[] cubeResources) {
        final int[] textureIds = new int[1];
        glGenTextures(1, textureIds, 0);

        if (textureIds[0] == 0) {
            Log.w(TAG, "Could not generate a new OpenGL texture object.");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap[] cubeBitmaps = new Bitmap[6];
        for (int i = 0; i < 6; i++) {
            cubeBitmaps[i] = BitmapFactory.decodeResource(
                context.getResources(), cubeResources[i], options);

            if (cubeBitmaps[i] == null) {
                Log.w(TAG, "Resource ID " + cubeResources[i] + " could not be decoded.");
                glDeleteTextures(1, textureIds, 0);
                return 0;
            }
        }

        glBindTexture(GL_TEXTURE_CUBE_MAP, textureIds[0]);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0);

        glBindTexture(GL_TEXTURE_3D, 0);

        for (Bitmap bm : cubeBitmaps)
            bm.recycle();

        return textureIds[0];
    }
}
