package com.thea.itailor.utils;

import android.util.Log;

import com.thea.itailor.entities.Material;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Thea on 2015/7/10.
 */
public class MTLParser {
    private static final String TAG = "MTLParser";

    private final InputStream inputStream;
    private final HashMap<String, Material> materials;

    private Material material = null;

    public MTLParser(InputStream inputStream, HashMap<String, Material> materials) {
        this.inputStream = inputStream;
        this.materials = materials;
    }

    public HashMap<String, Material> parser() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                Log.i(TAG, line);
                parserLine(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (material != null)
            materials.put(material.getMaterialName(), material);
        return materials;
    }

    public void parserLine(String line) {
        if (line.startsWith("newmtl"))
            processNewMtl(line.substring(6).trim());
        else if (line.startsWith("illum"))
            material.setIllum(Integer.valueOf(line.substring(5).trim()));
        else if (line.startsWith("Ns"))
            material.setExponent(Float.valueOf(line.substring(2).trim()));
        else if (line.startsWith("Ni"))
            material.setNi(Float.valueOf(line.substring(2).trim()));
        else if (line.startsWith("Sharpness"))
            material.setSharpness(Integer.valueOf(line.substring(9).trim()));
        else if (line.startsWith("Tf"))
            processTf(line.substring(2).trim());
        else if (line.startsWith("Ka"))
            processKa(line.substring(2).trim());
        else if (line.startsWith("Kd"))
            processKd(line.substring(2).trim());
        else if (line.startsWith("Ks"))
            processKs(line.substring(2).trim());
        else if (line.startsWith("map_Kd"))
            material.setDiffuseMap(line.substring(6).trim());
        else if (line.startsWith("d"))
            material.setDissolve(Float.valueOf(line.substring(1).trim()));
    }

    private void processNewMtl(String newmtl) {
        if (material != null)
            materials.put(material.getMaterialName(), material);
        material = new Material(newmtl);
    }

    private void processTf(String filteringStr) {
        String[] tf = filteringStr.split(" ");
        float[] filtering = new float[tf.length];
        for (int i = 0; i < tf.length; i++)
            filtering[i] = Float.valueOf(tf[i]);
        material.setAmbient(filtering);
    }

    private void processKa(String ambientStr) {
        String[] ka = ambientStr.split(" ");
        float[] ambient = new float[ka.length];
        for (int i = 0; i < ka.length; i++)
            ambient[i] = Float.valueOf(ka[i]);
        material.setAmbient(ambient);
    }

    private void processKd(String diffuseStr) {
        String[] kd = diffuseStr.split(" ");
        float[] diffuse = new float[kd.length];
        for (int i = 0; i < kd.length; i++)
            diffuse[i] = Float.valueOf(kd[i]);
        material.setDiffuse(diffuse);
    }

    private void processKs(String specularStr) {
        String[] ks = specularStr.split(" ");
        float[] specular = new float[ks.length];
        for (int i = 0; i < ks.length; i++)
            specular[i] = Float.valueOf(ks[i]);
        material.setSpecular(specular);
    }

}
