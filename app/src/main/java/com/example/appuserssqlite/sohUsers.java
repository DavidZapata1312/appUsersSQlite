package com.example.appuserssqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class sohUsers extends SQLiteOpenHelper {
    String tblUser = "Create Table user(user text, fullname text, email text, password  text, reservedword text)";
    String tblProfile = "Create Table profile(idProfile text, descProfile text)";
    public sohUsers(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblUser);
        db.execSQL(tblProfile);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table user");
        db.execSQL("Drop Table profile");
        db.execSQL(tblUser);
        db.execSQL(tblProfile);
    }
}
