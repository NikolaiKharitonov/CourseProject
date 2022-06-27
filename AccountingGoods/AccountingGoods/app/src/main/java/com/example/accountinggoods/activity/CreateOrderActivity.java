package com.example.accountinggoods.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.accountinggoods.R;
import com.example.accountinggoods.dialog.ExampleDialog;
import com.example.accountinggoods.model.DatabaseModel;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateOrderActivity extends AppCompatActivity {
    private Button button;

    DatabaseModel databaseModel;
    private Button dateTimePickerButton;
    private DatePickerDialog datePickerDialog;
    private Spinner spinner;
    private EditText sumBuyET, sumSellET, descriptionET;
    private static final String TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        initDatePicker();
        dateTimePickerButton = findViewById(R.id.datapickerButton);
        dateTimePickerButton.setText(getTodayDate());
        spinner = findViewById(R.id.supSpinner);
        sumBuyET = findViewById(R.id.sumBuyET);
        sumSellET = findViewById(R.id.sumSellET);
        descriptionET = findViewById(R.id.descriptionET);
        loadSup();
        button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month += 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public void datapickerButtonClick(View view) {
        datePickerDialog.show();
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = makeDateString(day, month, year);
                dateTimePickerButton.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return month + " " + day + " " + year;
    }

    public void loadSup() {

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
        spinner.setAdapter(adapter);

    }


    public void addOrderButton(View view) {
        // Создаём транзацию
        databaseModel = new DatabaseModel(getApplicationContext());
        SQLiteDatabase db = databaseModel.getWritableDatabase();
        ContentValues transactionValues = new ContentValues();
        transactionValues.put(DatabaseModel.COLUMN_TRANSACTIONS_DATE, System.currentTimeMillis());
        transactionValues.put(DatabaseModel.COLUMN_TRANSACTIONS_TYPE, "Order");
        db.insert(DatabaseModel.TABLE_TRANSACTIONS,null, transactionValues);

        //Ищем её id
        int idTransaction = 1;
        Cursor cursor = db.query(DatabaseModel.TABLE_TRANSACTIONS, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(DatabaseModel.COLUMN_TRANSACTIONS_ID);
            idTransaction = cursor.getInt(index);
        }
        cursor.close();

        //Получаем данные с формы

        String sumBuyT = sumBuyET.getText().toString();
        String sumSellT = sumSellET.getText().toString();

        //Todo: Тут нужно добавить валидация данных с полученных EditText.
        //
        // Код писать тут. Данные для валидации sumBuyT, sumSellT. Нужно проверить возможность преобразования в double и чтобы они пустые не были.
        //

        // Ищем поставщика
        int idSupplier = 1;
        Cursor cursor1 = db.query(DatabaseModel.TABLE_SUPPLIERS, null ,null, null, null, null, null);
        while(cursor1.moveToNext()) {
            int indexSupTitle = cursor1.getColumnIndex(DatabaseModel.COLUMN_SUPPLIERS_TITLE);
            String titleSups = cursor1.getString(indexSupTitle);
            if (titleSups.equals(spinner.getSelectedItem().toString()))
            {
                int idIndex = cursor1.getColumnIndex(DatabaseModel.COLUMN_SUPPLIERS_ID);
                idSupplier = cursor1.getInt(idIndex);
            }

        }
        cursor1.close();

        //Создаём заказ товара
        try {
            ContentValues orderValues = new ContentValues();
            orderValues.put(DatabaseModel.COLUMN_ORDERS_SUPPLIER_ID, idSupplier);
            orderValues.put(DatabaseModel.COLUMN_ORDERS_SUM_BUY, Double.parseDouble(sumBuyT));
            orderValues.put(DatabaseModel.COLUMN_ORDERS_SUM_SELL, Double.parseDouble(sumSellT));
            orderValues.put(DatabaseModel.COLUMN_ORDERS_DESCRIPTION, descriptionET.getText().toString() + dateTimePickerButton.getText().toString()); //Todo: Это поле тоже обязательное
            orderValues.put(DatabaseModel.COLUMN_ORDERS_TRANSACTION_ID, idTransaction);
            orderValues.put(DatabaseModel.COLUMN_ORDERS_RETURN_FLAG, 0);
            db.insert(DatabaseModel.TABLE_ORDERS, null, orderValues);
            db.close();
            Toast t = Toast.makeText(this, "Успешно", Toast.LENGTH_LONG);
            t.show();
        }
        catch (Exception e)
        {
            Toast t = Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_LONG);
            t.show();
        }

    }
}