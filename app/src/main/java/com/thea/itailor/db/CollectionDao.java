package com.thea.itailor.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.thea.itailor.entities.Collection;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hunter on 2015/6/16.
 */
public class CollectionDao {
    private CollectionSQLiteOpenHelper helper;

    public CollectionDao(CollectionSQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void add(Bitmap bitmap, String description, Date date) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into Collection (image,description,collectTime) values (?,?,?)",
                new Object[]{bmToBlob(bitmap),description,date.getTime()});
        db.close();
    }

    public byte[] bmToBlob(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    public void delete(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from Collection where id = ?", new Object[]{id});
        db.close();
    }

    public List<Collection> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Collection> collections = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Collection order by collectTime desc", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            byte[] image  = cursor.getBlob(1);
            String description = cursor.getString(2);
            long date  = cursor.getLong(3);
            collections.add(new Collection(id, image, description, date));
        }

        cursor.close();
        db.close();
        return collections;
    }

    public List<Collection> search(String text) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Collection> collections = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Collection where description like \'" +
                strToPattern(text) + "\' order by collectTime desc", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            byte[] image  = cursor.getBlob(1);
            String description = cursor.getString(2);
            long date  = cursor.getLong(3);
            collections.add(new Collection(id, image, description, date));
        }

        cursor.close();
        db.close();
        return collections;
    }

    public String strToPattern(String text) {
        String[] strs = text.split(" ");
        String pattern = "%";
        for (int i = 0; i < strs.length; i++)
            pattern += strs[i] + "%";
        return pattern;
    }
}
