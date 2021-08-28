package com.example.segrenner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segrenner.Datebases.DbHelper;

public class ShowProduct extends AppCompatActivity {

    private static final String TAG = "ShowProduct";

    DbHelper dbHelper;
    private Button btnEdit, btnScanAgain;
    private EditText code, name, kindPackage, product;
    private String codeResult, codeDB, nameDB, kindPackageDB, productDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_product);
        code = (EditText) findViewById(R.id.code);
        name = (EditText) findViewById(R.id.name);
        kindPackage = (EditText) findViewById(R.id.kindPackage);
        product = (EditText) findViewById(R.id.product);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnScanAgain = (Button) findViewById(R.id.btnScanAgain);
        dbHelper = new DbHelper(this);

        Intent receivedIntent = getIntent();
        codeResult = receivedIntent.getStringExtra("codeResult");

        // retrieving data from the database by the issued and intercepted number of the scanned code
        // pobieranie danych z bazy przez wystawiony i przechwycony numer zeskanowanego kodu
        Cursor data = dbHelper.getDataByCode(codeResult);

        while (data.moveToNext()) {
            codeDB = data.getString(1);
            nameDB = data.getString(2);
            kindPackageDB = data.getString(3);
            productDB = data.getString(4);
        }

        // assigning data to appropriate fields
        // przypisywanie danych do odpowiednich pól
        code.setText(codeDB);
        name.setText(nameDB);
        kindPackage.setText(kindPackageDB);
        product.setText(productDB);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowProduct.this, EditDataActivity.class);
                intent.putExtra("codeResult", codeResult);
                startActivity(intent);
            }
        });

        btnScanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowProduct.this, "Ponowne skanowanie", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowProduct.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    // customizable toast
    // konfigurowany toast
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
