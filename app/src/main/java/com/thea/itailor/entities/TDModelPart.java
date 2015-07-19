package com.thea.itailor.entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.thea.itailor.Constant.BYTES_PER_FLOAT;
import static com.thea.itailor.Constant.BYTES_PER_SHORT;

/**
 * Created by Thea on 2015/7/10.
 */
public class TDModelPart {
    private final ShortBuffer vertexIndex;
    private final ShortBuffer textureIndex;
    private final ShortBuffer normalIndex;
    private final FloatBuffer normalBuffer;
    private final int size;
    private final Material material;

    public TDModelPart(ArrayList<Short> vertexIndex, ArrayList<Short> textureIndex,
                       ArrayList<Short> normalIndex, Material material, ArrayList<Float> normalData) {
        this.vertexIndex = listToBuffer(vertexIndex);
        this.textureIndex = listToBuffer(textureIndex);
        this.normalIndex = listToBuffer(normalIndex);
        this.normalBuffer = listToNormalBuffer(normalIndex, normalData);
        this.size = vertexIndex.size();
        this.material = material;
    }

    private ShortBuffer listToBuffer(ArrayList<Short> list) {
        int size = list.size();
        short[] array = new short[size];
        for (int i = 0; i < size; i++)
            array[i] = list.get(i);
        ShortBuffer shortBuffer = ByteBuffer.allocateDirect(array.length * BYTES_PER_SHORT)
            .order(ByteOrder.nativeOrder()).asShortBuffer();
        shortBuffer.put(array);
        shortBuffer.position(0);
        return shortBuffer;
    }

    private FloatBuffer listToNormalBuffer(ArrayList<Short> normals, ArrayList<Float> normalData) {
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(normals.size() * BYTES_PER_FLOAT * 3)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (short s : normals){
            float x = normalData.get(s * 3);
            float y = normalData.get(s * 3 + 1);
            float z = normalData.get(s * 3 + 2);
            floatBuffer.put(x);
            floatBuffer.put(y);
            floatBuffer.put(z);
        }
        floatBuffer.position(0);
        return floatBuffer;
    }

    public int genVertexIndexBuffers() {
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);
        if (buffers[0] == 0)
            throw new RuntimeException("Could not create a new index buffer object.");

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[0]);

        vertexIndex.position(0);

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, vertexIndex.capacity() * BYTES_PER_SHORT,
                vertexIndex, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        return buffers[0];
    }

    public int setNormalPointer(int aNormal) {
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);
        if (buffers[0] == 0)
            throw new RuntimeException("Could not create a new vertex buffer object.");

        glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);

        normalBuffer.position(0);

        glBufferData(GL_ARRAY_BUFFER, normalBuffer.capacity() * BYTES_PER_FLOAT,
                normalBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(aNormal, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(aNormal);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return buffers[0];
    }

    public void draw(LocationParams params) {
        glUniform3f(params.uFiltering, material.getFiltering()[0],
                material.getFiltering()[1], material.getFiltering()[2]);
        glUniform3f(params.uAmbient, material.getAmbient()[0],
                material.getAmbient()[1], material.getAmbient()[2]);
        glUniform3f(params.uDiffuse, material.getDiffuse()[0],
                material.getDiffuse()[1], material.getDiffuse()[2]);
        glUniform3f(params.uSpecular, material.getSpecular()[0],
                material.getSpecular()[1], material.getSpecular()[2]);
        glUniform1f(params.uDissolve, material.getDissolve());
        glUniform1f(params.uExponent, material.getExponent());

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, genVertexIndexBuffers());
        glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_SHORT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
