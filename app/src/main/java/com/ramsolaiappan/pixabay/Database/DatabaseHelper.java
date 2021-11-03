package com.ramsolaiappan.pixabay.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ramsolaiappan.pixabay.GlobalVars;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "LikedImages";
    public static final String LIKED_COLUMN = "LikedImagesId";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "likedImagesDB.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + "(" + LIKED_COLUMN + " INTEGER)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addId(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LIKED_COLUMN,id);

        long i = db.insert(TABLE_NAME,null,cv);
        if(i == -1)
            return false;
        else
            return true;
    }

    public boolean deleteId(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long i = db.delete(TABLE_NAME,LIKED_COLUMN+" = ?",new String[]{String.valueOf(id)});
        if(i > 0)
            return true;
        else
            return false;
    }

    public ArrayList<Integer> getLikedImages()
    {
        ArrayList<Integer> ids = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
            if(cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    int id = cursor.getInt(0);
                    ids.add(id);
                }
            }
            cursor.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        db.close();

        return ids;
    }
}
