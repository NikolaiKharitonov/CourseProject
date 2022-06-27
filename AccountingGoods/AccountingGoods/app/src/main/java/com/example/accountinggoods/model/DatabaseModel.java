package com.example.accountinggoods.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseModel extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "accounting_goods.db";
    private static final int SCHEMA = 3;

    // Transactions Общая таблица с транзацакиями.
    public static final String TABLE_TRANSACTIONS = "Transactions";
    public static final String COLUMN_TRANSACTIONS_ID = "TransactionID";
    public static final String COLUMN_TRANSACTIONS_DATE = "Date";
    public static final String COLUMN_TRANSACTIONS_TYPE = "Type";

    //Overheads Накладные: з/п налоги и тд
    public static final String TABLE_OVERHEADS = "Overheads";
    public static final String COLUMN_OVERHEADS_ID = "OverheadID";
    public static final String COLUMN_OVERHEADS_OVERHEAD_TYPE_ID = "OverheadTypeID";
    public static final String COLUMN_OVERHEADS_TRANSACTION_ID = "TransactionID";
    public static final String COLUMN_OVERHEADS_SUM = "Sum";

    //OverheadTypes: Виды накладных
    public static final String TABLE_OVERHEAD_TYPES = "OverheadTypes";
    public static final String COLUMN_OVERHEAD_TYPES_ID = "OverheadTypeID";
    public static final String COLUMN_OVERHEAD_TYPES_TITLE = "Title";

    //Orders: Заказы поставщикам
    public static final String TABLE_ORDERS = "Orders";
    public static final String COLUMN_ORDERS_ID = "OrderID";
    public static final String COLUMN_ORDERS_SUPPLIER_ID = "SupplierID";
    public static final String COLUMN_ORDERS_SUM_BUY = "SumBuy";
    public static final String COLUMN_ORDERS_SUM_SELL = "SumSell";
    public static final String COLUMN_ORDERS_DESCRIPTION = "Description";
    public static final String COLUMN_ORDERS_TRANSACTION_ID = "TransactionID";
    public static final String COLUMN_ORDERS_RETURN_FLAG = "ReturnFlag";

    //Suppliers: Поставщики
    public static final String TABLE_SUPPLIERS = "Suppliers";
    public static final String COLUMN_SUPPLIERS_ID = "SupplierID";
    public static final String COLUMN_SUPPLIERS_TITLE = "Title";
    public static final String COLUMN_SUPPLIERS_PHONE = "Phone";
    public static final String COLUMN_SUPPLIERS_ADDRESS = "Address";

    //DebtSuppliers: Долги поставщикам
    public static final String TABLE_DEBT_SUPPLIERS = "DebtSuppliers";
    public static final String COLUMN_DEBT_SUPPLIERS_ID = "DebtSuppliersID";
    public static final String COLUMN_DEBT_SUPPLIERS_ORDER_ID = "OrderID";
    public static final String COLUMN_DEBT_SUPPLIERS_DEBT = "Debt";

    //PaymentSuppliers: Выплаты поставщикам
    public static final String TABLE_PAYMENT_SUPPLIERS = "PaymentSuppliers";
    public static final String COLUMN_PAYMENT_SUPPLIERS_ID = "PaymentSuppliersID";
    public static final String COLUMN_PAYMENT_SUPPLIERS_DEBT_SUPPLIERS_ID = "DebtSuppliersID";
    public static final String COLUMN_PAYMENT_SUPPLIERS_SUM = "Sum";

    public DatabaseModel(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    //Создание бд если её не существует
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Transactions (" +
                    COLUMN_TRANSACTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TRANSACTIONS_DATE + " INTEGER NOT NULL," +
                    COLUMN_TRANSACTIONS_TYPE + " TEXT);"
        );

        db.execSQL("CREATE TABLE OverheadTypes (" +
                COLUMN_OVERHEAD_TYPES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_OVERHEAD_TYPES_TITLE + " TEXT NOT NULL);"
        );

        db.execSQL("CREATE TABLE Overheads (" +
                    COLUMN_OVERHEADS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_OVERHEADS_OVERHEAD_TYPE_ID + " INTEGER NOT NULL," +
                    COLUMN_OVERHEADS_TRANSACTION_ID + " INTEGER NOT NULL," +
                    COLUMN_OVERHEADS_SUM + " REAL NOT NULL," +
                    "FOREIGN KEY (TransactionID) REFERENCES Transactions (TransactionID)" +
                    "ON UPDATE CASCADE ON DELETE CASCADE," +
                    "FOREIGN KEY (OverheadTypeID) REFERENCES OverheadTypes (OverheadTypeID)" +
                    "ON UPDATE CASCADE ON DELETE CASCADE);"
        );

        db.execSQL("CREATE TABLE Suppliers (" +
                    COLUMN_SUPPLIERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_SUPPLIERS_TITLE + " TEXT NOT NULL," +
                    COLUMN_SUPPLIERS_PHONE + " TEXT," +
                    COLUMN_SUPPLIERS_ADDRESS + " TEXT);"

        );

        db.execSQL("CREATE TABLE Orders (" +
                COLUMN_ORDERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ORDERS_SUPPLIER_ID + " INTEGER NOT NULL," +
                COLUMN_ORDERS_SUM_BUY + " REAL NOT NULL," +
                COLUMN_ORDERS_SUM_SELL + " REAL NOT NULL," +
                COLUMN_ORDERS_DESCRIPTION + " TEXT," +
                COLUMN_ORDERS_TRANSACTION_ID + " INTEGER NOT NULL," +
                COLUMN_ORDERS_RETURN_FLAG + " INTEGER NOT NULL," +
                "FOREIGN KEY (SupplierID) REFERENCES Suppliers (SupplierID)" +
                "ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY (TransactionID) REFERENCES Transactions (TransactionID)" +
                "ON UPDATE CASCADE ON DELETE CASCADE);"
        );

        db.execSQL("CREATE TABLE DebtSuppliers (" +
                COLUMN_DEBT_SUPPLIERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_DEBT_SUPPLIERS_ORDER_ID + " INTEGER NOT NULL," +
                COLUMN_DEBT_SUPPLIERS_DEBT + " REAL NOT NULL," +
                "FOREIGN KEY (OrderID) REFERENCES Orders (OrderID)" +
                "ON UPDATE CASCADE ON DELETE CASCADE);"
        );

        db.execSQL("CREATE TABLE PaymentSuppliers (" +
                COLUMN_PAYMENT_SUPPLIERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PAYMENT_SUPPLIERS_DEBT_SUPPLIERS_ID + " INTEGER NOT NULL," +
                COLUMN_PAYMENT_SUPPLIERS_SUM + " REAL NOT NULL," +
                "FOREIGN KEY (DebtSuppliersID) REFERENCES DebtSuppliers (DebtSuppliersID)" +
                "ON UPDATE CASCADE ON DELETE CASCADE);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OVERHEAD_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OVERHEADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEBT_SUPPLIERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_SUPPLIERS);
        onCreate(db);

    }








}
