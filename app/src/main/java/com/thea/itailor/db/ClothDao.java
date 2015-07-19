package com.thea.itailor.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hunter on 2015/6/16.
 */
public class ClothDao {
    private ClothSQLiteOpenHelper helper;

    public ClothDao(ClothSQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void add(String group, String remark, String fileName, String description, Date date) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into Cloth (groupname,remark,filename,description,generateTime) values (?,?,?,?,?)",
                new Object[]{group,remark,fileName,description,date.getTime()});
        db.close();
    }

    public void delete(int columnIndex, String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (columnIndex == 1)
            db.execSQL("delete from Cloth where groupname = ?", new Object[]{name});
        else if (columnIndex == 2)
            db.execSQL("delete from Cloth where remark = ?", new Object[]{name});
        else if (columnIndex == 3)
            db.execSQL("delete from Cloth where filename = ?", new Object[]{name});
        db.close();
    }

    public void updateGroup(String newName, String oldName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update Cloth set groupname = ? where groupname = ?", new Object[]{newName, oldName});
        db.close();
    }

    public void updateRemark(String newName, String filename) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update Cloth set remark = ?, generateTime = ? where filename = ?",
                new Object[]{newName, new Date().getTime(), filename});
        db.close();
    }

    public void moveTo(String groupName, String filename) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update Cloth set groupname = ?, generateTime = ? where filename = ?",
                new Object[]{groupName, new Date().getTime(), filename});
        db.close();
    }

    public List<String> findGroups() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> groups = new ArrayList<>();
        Cursor cursor = db.rawQuery("select distinct groupname from Cloth", null); // order by id asc
        while (cursor.moveToNext())
            groups.add(cursor.getString(0));

        cursor.close();
        db.close();
        return groups;
    }

    public List<String> findChildren(String groupName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> children = new ArrayList<>();
        Cursor cursor = db.rawQuery("select remark from Cloth where groupname = ? " +
                "order by generateTime desc", new String[]{groupName});
        while (cursor.moveToNext()) {
            if (cursor.isNull(0))
                continue;
            children.add(cursor.getString(0));
        }

        cursor.close();
        db.close();
        return children;
    }

    public List<List<String>> findChildrenAndFilename(String groupName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<List<String>> childFile = new ArrayList<>();
        List<String> children = new ArrayList<>();
        List<String> filenames = new ArrayList<>();
        Cursor cursor = db.rawQuery("select remark, filename from Cloth where groupname = ? " +
                "order by generateTime desc", new String[]{groupName});
        while (cursor.moveToNext()) {
            if (cursor.isNull(1))
                continue;
            children.add(cursor.getString(0));
            filenames.add(cursor.getString(1));
        }
        childFile.add(children);
        childFile.add(filenames);

        cursor.close();
        db.close();
        return childFile;
    }
}
