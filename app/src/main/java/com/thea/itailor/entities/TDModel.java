package com.thea.itailor.entities;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glGenBuffers;
import static com.thea.itailor.Constant.BYTES_PER_FLOAT;

/**
 * Created by Thea on 2015/7/10.
 */
public class TDModel {
    private static final String TAG = "TDModel";

    private final FloatBuffer vertexData;
    private final FloatBuffer textureData;
    private final FloatBuffer normalData;

    private final ArrayList<TDModelPart> parts;

    public TDModel(ArrayList<Float> vertexData, ArrayList<Float> textureData,
                   ArrayList<Float> normalData, ArrayList<TDModelPart> parts) {
        this.vertexData = listToBuffer(vertexData);
        this.textureData = listToBuffer(textureData);
        this.normalData = listToBuffer(normalData);
        this.parts = parts;
    }

    private FloatBuffer listToBuffer(ArrayList<Float> list) {
        int size = list.size();
        float[] array = new float[size];
        for (int i = 0; i < size; i++)
            array[i] = list.get(i);
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(array.length * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public int genVertexDataBuffers() {
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);
        if (buffers[0] == 0)
            throw new RuntimeException("Could not create a new vertex buffer object.");

        glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);

        vertexData.position(0);

        glBufferData(GL_ARRAY_BUFFER, vertexData.capacity() * BYTES_PER_FLOAT,
                vertexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return buffers[0];
    }

    public int genNormalBuffers() {
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);
        if (buffers[0] == 0)
            throw new RuntimeException("Could not create a new vertex buffer object.");

        glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);

        normalData.position(0);

        glBufferData(GL_ARRAY_BUFFER, normalData.capacity() * BYTES_PER_FLOAT,
                normalData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return buffers[0];
    }

    public void setNormalPointer(int aNormal) {
        for (TDModelPart part : parts)
            part.setNormalPointer(aNormal);
    }

    public void draw(LocationParams params) {
        Log.i(TAG, "draw");
        for (TDModelPart part : parts)
            part.draw(params);
    }
}
