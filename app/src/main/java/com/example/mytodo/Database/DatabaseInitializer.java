package com.example.mytodo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseInitializer extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.sqlite";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE"+
            " todos (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, "
            + " todo_date DATETIME, completed INTEGER);";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF "+
            "EXISTS todos";
    public DatabaseInitializer(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int
            i1) {
        sqLiteDatabase.execSQL(SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
    public SQLiteDatabase generateDB(){
        return this.getWritableDatabase();
    }
}
