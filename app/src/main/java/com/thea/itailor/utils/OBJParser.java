package com.thea.itailor.utils;

import android.content.Context;
import android.util.Log;

import com.thea.itailor.entities.Material;
import com.thea.itailor.entities.TDModel;
import com.thea.itailor.entities.TDModelPart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Thea on 2015/7/10.
 */
public class OBJParser {
    private static final String TAG = "OBJParser";
    private Context context;

    private final HashMap<String, Material> materials = new HashMap<>();
    private final ArrayList<Float> vertexData = new ArrayList<>();
    private final ArrayList<Float> textureData = new ArrayList<>();
    private final ArrayList<Float> normalData = new ArrayList<>();

    private final ArrayList<Short> vertexIndex = new ArrayList<>();
    private final ArrayList<Short> textureIndex = new ArrayList<>();
    private final ArrayList<Short> normalIndex = new ArrayList<>();

    private final ArrayList<TDModelPart> parts = new ArrayList<>();

    private String materialName = null;

    public OBJParser(Context context) {
        this.context = context;
    }

    public TDModel parser(String fileName) {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            while ((line = reader.readLine()) != null) {
                Log.i(TAG, line);
                parserLine(line.trim());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, e.toString());
        }
        addTDModelPart();
        return new TDModel(vertexData, textureData, normalData, parts);
    }

    public void parserLine(String line) {
        if (line.startsWith("mtllib"))
            processMtlLib(line.substring(6).trim());
        else if (line.startsWith("vt"))
            processTexture(line.substring(2).trim());
        else if (line.startsWith("vn"))
            processNormal(line.substring(2).trim());
        else if (line.startsWith("v"))
            processVertex(line.substring(1).trim());
        else if (line.startsWith("f"))
            processFace(line.substring(1).trim());
        else if (line.startsWith("usemtl"))
            processUseMtl(line.substring(6).trim());
    }

    private void processMtlLib(String mtlName) {
//        File file = new File(mtlName);
        try {
            new MTLParser(context.getAssets().open(mtlName), materials).parser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processTexture(String textureStr) {
        String[] textures = textureStr.split("[ ]+");
        for (int i = 0; i < textures.length; i++)
            textureData.add(Float.valueOf(textures[i]));
    }

    private void processNormal(String normalStr) {
        String[] normals = normalStr.split("[ ]+");
        for (int i = 0; i < normals.length; i++)
            normalData.add(Float.valueOf(normals[i]));
    }

    private void processVertex(String vertexStr) {
        String[] vertices = vertexStr.split("[ ]+");
        for (int i = 0; i < vertices.length; i++)
            vertexData.add(Float.valueOf(vertices[i]));
    }

    private void processFace(String faceStr) {
        String[] face = faceStr.split("[ ]+");
        if (face.length == 3 && face[0].matches("[0-9]+/[0-9]+/[0-9]+")) {
            for (int i = 0; i < 3; i++) {
                String[] vertices = face[i].split("/");
                vertexIndex.add((short) (Short.valueOf(vertices[0]) - 1));
                textureIndex.add((short) (Short.valueOf(vertices[1]) - 1));
                normalIndex.add((short) (Short.valueOf(vertices[2]) - 1));
            }
        }
    }

    private void processUseMtl(String materialStr) {
        addTDModelPart();
        materialName = materialStr;
    }

    private void addTDModelPart() {
        if (vertexIndex.size() == 0)
            return;
        Material material = materials.get(materialName);
        parts.add(new TDModelPart(vertexIndex, textureIndex, normalIndex, material, normalData));
        vertexIndex.clear();
        textureIndex.clear();
        normalIndex.clear();
    }
}
