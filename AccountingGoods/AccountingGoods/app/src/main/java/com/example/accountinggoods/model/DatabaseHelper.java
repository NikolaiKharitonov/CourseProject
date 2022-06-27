package com.example.accountinggoods.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper {

    // Добавление поставщика
    public void addSupplier (String title, String phone, String address, Context context)
    {
        DatabaseModel databaseModel;
        databaseModel = new DatabaseModel(context);
        SQLiteDatabase db = databaseModel.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseModel.COLUMN_SUPPLIERS_TITLE, title);
        values.put(DatabaseModel.COLUMN_SUPPLIERS_PHONE, phone);
        values.put(DatabaseModel.COLUMN_SUPPLIERS_ADDRESS, address);
        db.insert(DatabaseModel.TABLE_SUPPLIERS, null, values);
    }

    // Создание заказа
    public void createOrder (String supplier, double sumBuy, double sumSell, Calendar date, Context context)
    {

    }
}
