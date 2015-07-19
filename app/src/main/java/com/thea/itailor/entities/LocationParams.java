package com.thea.itailor.entities;

/**
 * Created by Thea on 2015/7/14.
 */
public class LocationParams {
    public final int aNormal;
    public final int uFiltering;
    public final int uAmbient;
    public final int uDiffuse;
    public final int uSpecular;
    public final int uDissolve;
    public final int uExponent;

    public LocationParams(int aNormal, int uFiltering, int uAmbient, int uDiffuse,
                          int uSpecular, int uDissolve, int uExponent) {
        this.aNormal = aNormal;
        this.uFiltering = uFiltering;
        this.uAmbient = uAmbient;
        this.uDiffuse = uDiffuse;
        this.uSpecular = uSpecular;
        this.uDissolve = uDissolve;
        this.uExponent = uExponent;
    }
}
