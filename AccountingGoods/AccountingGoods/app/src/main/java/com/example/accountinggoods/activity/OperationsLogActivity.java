package com.example.accountinggoods.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.accountinggoods.R;
import com.example.accountinggoods.dialog.ChoiseOperationTypeDialog;
import com.example.accountinggoods.model.DatabaseModel;

import java.util.ArrayList;

public class OperationsLogActivity extends AppCompatActivity implements ChoiseOperationTypeDialog.ChoiseOperationTypeListener {

    ListView transactionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations_log);
        transactionList = findViewById(R.id.transactionListView);
        updateList();
    }


    public void addOperationButtonClick(View view) {
        DialogFragment typeChoice = new ChoiseOperationTypeDialog();
        typeChoice.setCancelable(false);
        typeChoice.show(getSupportFragmentManager(), "TypeChoice");
    }

    @Override
    public void onPositiveButtonClicked(String[] list, int position) {

        switch (position)
        {
            case 0:
                Intent intent1 = new Intent(OperationsLogActivity.this, CreateOrderActivity.class);
                startActivity(intent1);
                break;
            case 1:
                Intent intent2 = new Intent(OperationsLogActivity.this, ReturnOrderActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    public void updateList()
    {
        DatabaseModel databaseModel;
        databaseModel = new DatabaseModel(getApplicationContext());
        SQLiteDatabase db = databaseModel.getReadableDatabase();
        ArrayList<String> nameTranscations = new ArrayList<String>();
        Cursor cursor = db.query(DatabaseModel.TABLE_TRANSACTIONS, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int typeIndex = cursor.getColumnIndex(DatabaseModel.COLUMN_TRANSACTIONS_TYPE);
            String type = cursor.getString(typeIndex);
            int transactionIdIndex = cursor.getColumnIndex(DatabaseModel.COLUMN_TRANSACTIONS_ID);
            int transactionId = cursor.getInt(transactionIdIndex);
            switch (type)
            {
                case "Order":
                    Cursor orderCursor = db.query(DatabaseModel.TABLE_ORDERS, null, null, null, null, null, null);
                    while (orderCursor.moveToNext()) {
                        int orderTransactionIdIndex = orderCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_TRANSACTION_ID);
                        int orderTransactionId = orderCursor.getInt(orderTransactionIdIndex);
                        if (orderTransactionId == transactionId)
                        {
                             int indexDesc = orderCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_DESCRIPTION);
                             nameTranscations.add(orderCursor.getString(indexDesc));
                             break;
                        }
                    } orderCursor.close();
                    break;
            }
        }
        cursor.close();
        db.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameTranscations);
        transactionList.setAdapter(adapter);
    }
}