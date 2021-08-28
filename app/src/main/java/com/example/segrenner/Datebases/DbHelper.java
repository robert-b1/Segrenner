package com.example.segrenner.Datebases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    public static final String DATABASE_NAME = "DatebaseProducts.db";
    public static final String PRODUCTS_TABLE_NAME = "products";
    public static final String PRODUCTS_COLUMN_ID = "id";
    public static final String PRODUCTS_COLUMN_CODE = "code";
    public static final String PRODUCTS_COLUMN_NAME = "name";
    public static final String PRODUCTS_COLUMN_KIND_OF_PACKAGE = "kindPackage";
    public static final String PRODUCTS_COLUMN_PRODUCT = "product";
    private HashMap hashMap;

    public DbHelper(@Nullable Context context) {
        super(context, PRODUCTS_TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + PRODUCTS_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCTS_COLUMN_CODE + " TEXT, " +
                PRODUCTS_COLUMN_NAME + " TEXT, " +
                PRODUCTS_COLUMN_KIND_OF_PACKAGE + " TEXT, " +
                PRODUCTS_COLUMN_PRODUCT + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME);
        onCreate(db);
    }

    // method for adding a product to the table
    // metoda do dodawania produktu do tabeli
    public boolean addProduct(String code, String name, String kindPackage, String product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("kindPackage", kindPackage);
        contentValues.put("product", product);
        //db.insert("products", null, contentValues);

        Log.d(TAG, "addProduct: Adding Product");

        long result = db.insert(PRODUCTS_TABLE_NAME, null, contentValues);

        // if date as inserted incorrectly it will return -1
        // jeżeli dodawanie nie przebiegnie prawidłowo to zwróci -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // method to get data from table products
    // metoda do pobierania danych z tabeli products
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + PRODUCTS_TABLE_NAME;
        Cursor data = db.rawQuery(query, null);

        Log.d(TAG, "getData: Geting Data");

        return data;
    }

    // method to get product code from table
    // metoda do pobierania kodu produktu z tabeli
    public Cursor getProductCode(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + PRODUCTS_COLUMN_CODE + " FROM " + PRODUCTS_TABLE_NAME +
                " WHERE " + PRODUCTS_COLUMN_NAME + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // method for retrieving data by the producer's code
    // metoda to pobierania danych po kodzie productu
    public Cursor getDataByCode(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + PRODUCTS_TABLE_NAME + " WHERE " + PRODUCTS_COLUMN_CODE + " = ?";
        Cursor dataByCode = db.rawQuery(query, new String[]{code});

        Log.d(TAG, "getDataByCode: Geting Data");

        return dataByCode;
    }

    // method to upgrade the existing product in the table
    // metoda do upgrejdowania istniejącego produktu w tabeli
    public boolean updateProduct(Integer id, String code, String name, String kindPackage, String product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("kindPackage", kindPackage);
        contentValues.put("product", product);
        db.update("products", contentValues, "id = ?", new String[]{Integer.toString(id)});

        Log.d(TAG, "updateProduct: Updating Product");

        return true;
    }


    // remove the product from the database
    // usuwanie produktu z bazy danych
    public Integer deleteProduct(String code) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d(TAG, "deleteProduct: Deleted Product");

        return db.delete("products", "code = ?", new String[]{code});

    }

    // method to check if a given product exists by code
    // metoda do sprawdzania po kodzie czy dany produkt istnieje
    public boolean checkIfTheProductExists(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Check if product exists
        String query = "SELECT * FROM " + PRODUCTS_TABLE_NAME + " WHERE " + PRODUCTS_COLUMN_CODE + " = ?";
        Cursor res = db.rawQuery(query, new String[]{code});
        //If product exists
        if (res.getCount() == 0) {
            return false;
        }
        return true;
    }

}
