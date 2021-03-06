package com.example.segrenner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segrenner.Datebases.DbHelper;

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnSave, btnDelete;
    private EditText code, name, kindPackage, product;

    DbHelper dbHelper;

    private String selectedCode, selectedName, selectedKindPackage, selectedProduct;
    private String codeResult, codeDB, nameDB, kindPackageDB, productDB;
    private int selectedID,selectedID_DB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        code = (EditText) findViewById(R.id.code);
        name = (EditText) findViewById(R.id.name);
        kindPackage = (EditText) findViewById(R.id.kindPackage);
        product = (EditText) findViewById(R.id.product);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        dbHelper = new DbHelper(this);

        Intent receivedIntent = getIntent();
        codeResult = receivedIntent.getStringExtra("codeResult");

        // retrieving data from the database by the issued and intercepted number of the scanned code
        // pobieranie danych z bazy przez wystawiony i przechwycony numer zeskanowanego kodu
        Cursor data = dbHelper.getDataByCode(codeResult);

        while (data.moveToNext()) {
            selectedID_DB = data.getInt(0);
            codeDB = data.getString(1);
            nameDB = data.getString(2);
            kindPackageDB = data.getString(3);
            productDB = data.getString(4);
        }

        // assigning data to appropriate fields
        // przypisywanie danych do odpowiednich p??l
        code.setText(codeDB);
        name.setText(nameDB);
        kindPackage.setText(kindPackageDB);
        product.setText(productDB);

//        // get the productID we passed as an extra
//        // pobierz productID (identyfikator produktu), kt??ry przekazali??my jako dodatkowy
//        selectedID = receivedIntent.getIntExtra("id", -1);
//
//        // get the code we passed as an extra
//        // pobierz cod, kt??ry przekazali??my jako dodatkowy
//        selectedCode = receivedIntent.getStringExtra("code");
//
//        // get the name we passed as an extra
//        // pobierz name, kt??ry przekazali??my jako dodatkowy
//        selectedName = receivedIntent.getStringExtra("name");
//
//        // get the kindPackage we passed as an extra
//        // pobierz rodzaj opakowania, kt??ry przekazali??my jako dodatkowy
//        selectedKindPackage = receivedIntent.getStringExtra("kindPackage");
//
//        // get the product we passed as an extra
//        // pobierz produkt, kt??ry przekazali??my jako dodatkowy
//        selectedProduct = receivedIntent.getStringExtra("product");
//
//        // set the text to show the current selected code
//        // ustaw tekst tak, aby pokazywa?? aktualnie wybrany cod
//        code.setText(selectedCode);
//
//        // set the text to show the current selected name
//        // ustaw tekst tak, aby pokazywa?? aktualnie wybrany nazwa
//        name.setText(selectedName);
//
//        // set the text to show the current selected kindPackage
//        // ustaw tekst tak, aby pokazywa?? aktualnie wybrany rodzaj opakowania
//        kindPackage.setText(selectedKindPackage);
//
//        // set the text to show the current selected product
//        // ustaw tekst tak, aby pokazywa?? aktualnie wybrany produkt
//        product.setText(selectedProduct);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item1 = code.getText().toString();
                String item2 = name.getText().toString();
                String item3 = kindPackage.getText().toString();
                String item4 = product.getText().toString();
                if (!(item1.equals("") && item2.equals("") && item3.equals("") && item4.equals(""))) {
                    dbHelper.updateProduct(selectedID_DB, item1, item2, item3, item4);
                    Log.d(TAG, "onClick: Updated product");
                    toastMassage("Zapisano poprawki w tym produkcie");
                    Intent intent = new Intent(getApplicationContext(), ListDataActivity.class);
                    startActivity(intent);
                } else {
                    toastMassage("??adne pole nie mo??e zosta?? puste");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteProduct(codeDB);
                Log.d(TAG, "onClick: Deleted product");
                toastMassage("Usuni??to produkt");
                Intent intent = new Intent(getApplicationContext(), ListDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void toastMassage(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }
}
