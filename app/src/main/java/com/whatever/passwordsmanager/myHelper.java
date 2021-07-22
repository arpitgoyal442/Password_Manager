package com.whatever.passwordsmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class myHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "PasswordsDB";
    private static final int DB_VERSION = 1;



    public myHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE AllPasswords (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "Title TEXT, "
                + "Password TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
