package com.thea.itailor.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by hunter on 2015/6/25.
 */
public class Collection {
    private int id;
    private byte[] image;
    private String description;
    private long date;

    public Collection(int id, byte[] image, String description) {
        this.id = id;
        this.image = image;
        this.description = description;
    }

    public Collection(int id, byte[] image, String description, long date) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Bitmap getImageBitmap() {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
