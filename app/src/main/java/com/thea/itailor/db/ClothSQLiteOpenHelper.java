package com.thea.itailor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hunter on 2015/6/16.
 */
public class ClothSQLiteOpenHelper extends SQLiteOpenHelper {

    public ClothSQLiteOpenHelper(Context context) {
        super(context, "Cloth.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Cloth(" +
                "id integer primary key autoincrement, " +
                "groupname varchar(40), remark varchar(40)," +
                "filename text, description text," +
                "generateTime long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
