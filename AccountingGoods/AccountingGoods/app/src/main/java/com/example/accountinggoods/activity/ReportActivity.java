package com.example.accountinggoods.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.accountinggoods.R;
import com.example.accountinggoods.model.DatabaseModel;

public class ReportActivity extends AppCompatActivity {
    TextView sumBuyTV, sumSellTV, incomeTV;
    double sumBuy = 0;
    double sumSell = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        sumBuyTV = findViewById(R.id.sumBuyReportTextView);
        sumSellTV = findViewById(R.id.sumSellReportET);
        incomeTV = findViewById(R.id.incomeReportTV);
        UpdateData();
        sumBuyTV.setText(String.valueOf(sumBuy));
        sumSellTV.setText(String.valueOf(sumSell));
        incomeTV.setText(String.valueOf(sumSell - sumBuy));


    }
    public void UpdateData() {
        DatabaseModel databaseModel;
        databaseModel = new DatabaseModel(getApplicationContext());
        SQLiteDatabase db = databaseModel.getReadableDatabase();
        Cursor transactionCursor = db.query(DatabaseModel.TABLE_TRANSACTIONS, null, null, null, null, null, null);
        while(transactionCursor.moveToNext())
        {
            int typeIndex = transactionCursor.getColumnIndex(DatabaseModel.COLUMN_TRANSACTIONS_TYPE);
            int transactionIdIndex = transactionCursor.getColumnIndex(DatabaseModel.COLUMN_TRANSACTIONS_ID);
            switch (transactionCursor.getString(typeIndex))
            {
                case "Order":
                    Cursor orderCursor = db.query(DatabaseModel.TABLE_ORDERS, null, null, null, null, null, null);
                    while(orderCursor.moveToNext())
                    {
                        int orderTransactionIdIndex = orderCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_TRANSACTION_ID);
                        int transactionId = orderCursor.getInt(orderTransactionIdIndex);
                        int returnFlagIndex = orderCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_RETURN_FLAG);
                        int returnFlag = orderCursor.getInt(returnFlagIndex);
                        if (transactionId == transactionCursor.getInt(transactionIdIndex) && returnFlag == 0)
                        {
                            int sumBuyIndex = orderCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_SUM_BUY);
                            int sumSellIndex = orderCursor.getColumnIndex(DatabaseModel.COLUMN_ORDERS_SUM_SELL);
                            sumBuy += orderCursor.getDouble(sumBuyIndex);
                            sumSell += orderCursor.getDouble(sumSellIndex);
                            break;
                        }
                    }
                    orderCursor.close();
            }
        }
        transactionCursor.close();
        db.close();
    }
}