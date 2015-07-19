package com.thea.itailor.entities;

/**
 * Created by Thea on 2015/7/10.
 */
public class Material {
    private String materialName;
    private String diffuseMap;
    private float exponent = 0f;
    private float ni = 0f;
    private float dissolve = 0f;
    private int illum = 0;
    private int sharpness = 0;
    private float[] filtering = {0f, 0f, 0f};
    private float[] ambient = {0f, 0f, 0f};
    private float[] diffuse = {0f, 0f, 0f};
    private float[] specular = {0f, 0f, 0f};

    public Material(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getDiffuseMap() {
        return diffuseMap;
    }

    public void setDiffuseMap(String diffuseMap) {
        this.diffuseMap = diffuseMap;
    }

    public float getExponent() {
        return exponent;
    }

    public void setExponent(float exponent) {
        this.exponent = exponent;
    }

    public float getNi() {
        return ni;
    }

    public void setNi(float ni) {
        this.ni = ni;
    }

    public float getDissolve() {
        return dissolve;
    }

    public void setDissolve(float dissolve) {
        this.dissolve = dissolve;
    }

    public int getIllum() {
        return illum;
    }

    public void setIllum(int illum) {
        this.illum = illum;
    }

    public int getSharpness() {
        return sharpness;
    }

    public void setSharpness(int sharpness) {
        this.sharpness = sharpness;
    }

    public float[] getFiltering() {
        return filtering;
    }

    public void setFiltering(float[] filtering) {
        this.filtering = filtering;
    }

    public float[] getAmbient() {
        return ambient;
    }

    public void setAmbient(float[] ambient) {
        this.ambient = ambient;
    }

    public float[] getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(float[] diffuse) {
        this.diffuse = diffuse;
    }

    public float[] getSpecular() {
        return specular;
    }

    public void setSpecular(float[] specular) {
        this.specular = specular;
    }
}
