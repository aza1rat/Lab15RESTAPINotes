package com.example.lab15_restapinoteskashitsin;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sql = "CREATE TABLE NotesCred (id INT, username TEXT, passwd TEXT);";
        sqlDB.execSQL(sql);
    }

    public String getCredUser()
    {
        String sql = "SELECT username FROM  NotesCred WHERE id = " + 1 + ";";
        SQLiteDatabase sqlDB = getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery(sql,null);
        if (cursor.moveToFirst())
            return cursor.getString(0);
        return"";
    }

    public String getCredPass()
    {
        String sql = "SELECT passwd FROM  NotesCred WHERE id = " + 1 + ";";
        SQLiteDatabase sqlDB = getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery(sql,null);
        if (cursor.moveToFirst())
            return cursor.getString(0);
        return"";
    }

    public void deleteCred()
    {
        String sql = "DELETE FROM NotesCred;";
        SQLiteDatabase sqlDB = getWritableDatabase();
        sqlDB.execSQL(sql);
    }

    public void insertCred(String iusername, String ipasswd)
    {
        String sql = "SELECT * FROM NotesCred;";
        SQLiteDatabase sqlDB = getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery(sql,null);
        if (cursor.moveToFirst())
            sql = "UPDATE NotesCred SET username = '" + iusername + "', SET passwd = '" + ipasswd +"' WHERE id = 1;";
        else
            sql = "INSERT INTO NotesCred VALUES (" + 1 + ", '"+ iusername +"', '" + ipasswd +"');";
        SQLiteDatabase sqlEDB = getWritableDatabase();
        sqlEDB.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
