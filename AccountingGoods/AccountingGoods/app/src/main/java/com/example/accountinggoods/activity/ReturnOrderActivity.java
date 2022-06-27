package com.example.accountinggoods.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accountinggoods.R;
import com.example.accountinggoods.model.DatabaseModel;

import java.util.ArrayList;

public class ReturnOrderActivity extends AppCompatActivity {
    ListView ordersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order);
        ordersListView = findViewById(R.id.ordersList);
        UpdateOrders();
        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View itemClicked, int i, long l) {
                String ordersName = ((TextView)itemClicked).getText().toString();
                DatabaseModel databaseModel;
                databaseModel = new DatabaseModel(getApplicationContext());
                SQLiteDatabase db = databaseModel.getReadableDatabase();
                Cursor searchOrderCursor = db.query(DatabaseModel.TABLE_ORDERS, null, null, null, null, null, null, null);
                while(searchOrderCursor.moveToNext())
                {
                    int ordersTitleIndex = searchOrderCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_DESCRIPTION);
                    if (ordersName.equals(searchOrderCursor.getString(ordersTitleIndex)))
                    {
                        int indexId = searchOrderCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_ID);
                        int id = searchOrderCursor.getInt(indexId);
                        ContentValues values = new ContentValues();
                        values.put(DatabaseModel.COLUMN_ORDERS_RETURN_FLAG, 1);
                        try {
                            db.update(DatabaseModel.TABLE_ORDERS, values, DatabaseModel.COLUMN_ORDERS_ID + "=" + id, null);
                            Toast t = Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_LONG);
                            t.show();
                            UpdateOrders();
                        } catch (Exception e) {
                            Toast t = Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG);
                            t.show();
                        }


                    }
                }
                searchOrderCursor.close();
                db.close();
            }
        });
    }


    public void UpdateOrders() {
        DatabaseModel databaseModel;
        databaseModel = new DatabaseModel(getApplicationContext());
        SQLiteDatabase db = databaseModel.getReadableDatabase();
        ArrayList<String> nameOrders = new ArrayList<String>();

        Cursor ordersCursor = db.query(DatabaseModel.TABLE_ORDERS, null, null, null, null, null, null);
        while(ordersCursor.moveToNext())
        {
            int returnFlagIndex = ordersCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_RETURN_FLAG);
            if (ordersCursor.getInt(returnFlagIndex) == 0)
            {
                int descriptionIndex = ordersCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_DESCRIPTION);
                int sumBuyIndex = ordersCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_SUM_BUY);
                nameOrders.add(ordersCursor.getString(descriptionIndex));
            }
        }

        ordersCursor.close();
        db.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameOrders);
        ordersListView.setAdapter(adapter);
    }
}