package com.example.chart_0405;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {
    String TableName;

    public SQLiteDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, String TableName){
        super(context, name, factory, version);
        this.TableName = TableName;
    }

    @Override //資料庫的參數名叫db
    public void onCreate(SQLiteDatabase db) {
        String SQLTable = "CREATE TABLE IF NOT EXISTS " + TableName + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Date TEXT, " +
                "Name TEXT, " +
                "Type TEXT, " +
                "Fee TEXT " +
                ");";
        db.execSQL(SQLTable);//執行SQL指令
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        final  String SQL ="DEOP TABLE" + TableName;
        db.execSQL(SQL);
    }

    public void chickTable(){
        Cursor cursor = getWritableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name ='" + TableName + "'",null);
        if(cursor !=null){
            if(cursor.getCount() ==0 )
                getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TableName + "(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Date TEXT, " +
                        "Name TEXT, " +
                        "Type TEXT, " +
                        "Fee TEXT " +
                        ");");
            cursor.close();
        }
    }

    //取得指定日期明細
    public ArrayList<HashMap<String,String>> searchByDate(String getDate){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE Date =" + "'" + getDate + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String id = c.getString(0);
            String date = c.getString(1);
            String name = c.getString(2);
            String type = c.getString(3);
            String fee = c.getString(4);

            hashMap.put("id", id);
            hashMap.put("date", date);
            hashMap.put("name", name);
            hashMap.put("type", type);
            hashMap.put("fee", fee);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    //取得指定日期總花費金額
    public  String getTotalFee(String getDate){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT SUM(Fee) FROM " + TableName
                + " WHERE Date =" + "'" + getDate + "'", null);
        String totalFee = "0";
        while (c.moveToNext()) {
            totalFee = c.getString(0);
        }
        if(totalFee!=null){
            return totalFee;
        }
        else {return "0";}
    }

    //取得有多少資料表,並以陣列回傳
    public ArrayList<String> getTables(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master", null);
        ArrayList<String> tables = new ArrayList<>();
        while (cursor.moveToNext()){
            String getTab = new String (cursor.getBlob(0));
            if (getTab.contains("android_metadata")){}
            else if (getTab.contains("sqlite_sequence")){}
            else tables.add(getTab.substring(0,getTab.length()-1));

        }
        return tables;
    }

    //顯示所有資料
    public ArrayList<HashMap<String, String>> showAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName, null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String id = c.getString(0);
            String date = c.getString(1);
            String name = c.getString(2);
            String type  = c.getString(3);
            String fee= c.getString(4);

            hashMap.put("id", id);
            hashMap.put("date", date);
            hashMap.put("name", name);
            hashMap.put("type",type);
            hashMap.put("fee", fee);
            arrayList.add(hashMap);
        }
        return arrayList;

    }

    //刪除全部資料
    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM"+TableName);
    }

    //新增資料
    public void addData(ArrayList arrayList) {
        SQLiteDatabase db = getWritableDatabase();
        String Add = "INSERT INTO " + TableName + " (Date, Name, Type, Fee) VALUES ("
                + "'" + arrayList.get(0) + "', "
                + "'" + arrayList.get(1) + "', "
                + "'" + arrayList.get(2) + "', "
                + "'" + arrayList.get(3) + "');";
        db.execSQL(Add);
    }

    //修改資料
    public void modify(String id, String date, String name, String type, String fee) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" UPDATE " + TableName
                + " SET Date = " + "'" + date + "',"
                + " Name = " + "'" + name + "',"
                + " Type = " + "'" + type + "',"
                + " Fee = " + "'" + fee + "'"
                + " WHERE _id= " + "'" + id + "'");
    }

    //刪除資料
    public void deleteByIdEZ(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"_id = " + id,null);
    }

    public ArrayList<HashMap<String,String>> searchById(String getId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE _id =" + "'" + getId + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String date = c.getString(1);
            String name = c.getString(2);
            String type = c.getString(3);
            String fee = c.getString(4);

            hashMap.put("id", id);
            hashMap.put("date", date);
            hashMap.put("name", name);
            hashMap.put("type", type);
            hashMap.put("fee", fee);
            arrayList.add(hashMap);
        }
        return arrayList;
    }
}
