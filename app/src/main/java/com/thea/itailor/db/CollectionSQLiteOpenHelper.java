package com.thea.itailor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hunter on 2015/6/16.
 */
public class CollectionSQLiteOpenHelper extends SQLiteOpenHelper {

    public CollectionSQLiteOpenHelper(Context context) {
        super(context, "collection.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Collection(" +
                "id integer primary key autoincrement, " +
                "image blob, description text, collectTime long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
