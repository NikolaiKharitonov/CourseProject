package com.example.accountinggoods.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.accountinggoods.R;
import com.example.accountinggoods.dialog.AddSupplierDialog;
import com.example.accountinggoods.model.DatabaseHelper;
import com.example.accountinggoods.model.DatabaseModel;

import java.util.ArrayList;

public class SuppliersActivity extends AppCompatActivity implements AddSupplierDialog.AddSupplierDialogListener {

    private ListView supList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);
        supList = findViewById(R.id.listSup);
        updateList();
    }

    public void addSupClick(View view) {
        openDialog();
    }

    public void openDialog() {
        AddSupplierDialog addSupplierDialog = new AddSupplierDialog();
        addSupplierDialog.show(getSupportFragmentManager(), "exampleDialog");
    }

    @Override
    public void applyText(String title, String phone, String address) {
        DatabaseModel databaseModel;
        databaseModel = new DatabaseModel(getApplicationContext());
        SQLiteDatabase db = databaseModel.getWritableDatabase();

        //Todo: Вот тут нужно валидацию сделать. Чтобы нельзя было двух поставщиков с одинаковым именем создать.
        ContentValues values = new ContentValues();
        values.put(DatabaseModel.COLUMN_SUPPLIERS_TITLE, title);
        values.put(DatabaseModel.COLUMN_SUPPLIERS_PHONE, phone);
        values.put(DatabaseModel.COLUMN_SUPPLIERS_ADDRESS, address);
        db.insert(DatabaseModel.TABLE_SUPPLIERS, null, values);
        db.close();
        updateList();
    }

    public void updateList() {
        DatabaseModel databaseModel;
        databaseModel = new DatabaseModel(getApplicationContext());
        SQLiteDatabase db = databaseModel.getReadableDatabase();
        ArrayList<String> nameSups = new ArrayList<String>();
        Cursor cursor = db.query(DatabaseModel.TABLE_SUPPLIERS, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int titleIndex = cursor.getColumnIndex(DatabaseModel.COLUMN_SUPPLIERS_TITLE);
            String title = cursor.getString(titleIndex);
            nameSups.add(title);
        }
        cursor.close();
        db.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameSups);
        supList.setAdapter(adapter);
    }

}