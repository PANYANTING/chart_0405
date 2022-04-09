package com.example.chart_0405;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {
    String TableName;

    public SQLiteDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, String TableName){
        super(context, name, factory, version);
        this.TableName = TableName;
    }

    @Override //資料庫的參數名叫db
    public void onCreate(SQLiteDatabase db) {
        String SQLTable = "CREATE TABLE IF NOT EXISTS" + TableName + "(" +
                "_id INTEGER PRIMARY KEY, "+
                "Name TEXT, "+
                "Fee TEXT, " +
                "Type TEXT" +
                ");";
        db.execSQL(SQLTable);//執行SQL指令
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {


    }
}
