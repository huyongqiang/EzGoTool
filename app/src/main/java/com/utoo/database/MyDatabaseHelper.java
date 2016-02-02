package com.utoo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangdi on 2016/1/31.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    final String CREATE_TABLE_SQL = "create table info (_id integer primary key autoincrement, " +
            "product, " +
            "cost, " +
            "sale, " +
            "cur_rate, " +
            "exc_rate, " +
            "express_fee, " +
            "final_price, " +
            "profit)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor findAll(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select * from info", null);
        return cursor;
    }

    public Cursor findById(SQLiteDatabase db, int id) {
        Cursor cursor = db.rawQuery("select * from info where _id = "+id, null);
        return cursor;
    }

    public void insert(SQLiteDatabase db, String product, String cost, String sale, String cur_rate, String exc_rate, String express_fee, String final_price, String profit) {
        db.execSQL("insert into info values(null, ?, ?, ?, ?, ?, ?, ?, ?)", new String[]{product, cost, sale, cur_rate, exc_rate, express_fee, final_price, profit});
    }

    public void delete(SQLiteDatabase db, int id) {
        db.execSQL("delete from info where _id = ?", new String[]{id + ""});
    }

}
