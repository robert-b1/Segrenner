package com.example.segrenner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segrenner.Datebases.DbHelper;

//class for the tab in which you will enter data about the product (second page after the main Main)
//klasa do zakładki w którą będzie się wpisywać dane o produkcie (druga stona po głównym Main'ie)
public class InputData extends AppCompatActivity {

    private static final String TAG = "InputData";

    DbHelper dbHelper;
    private Button btnSaveData;
    private EditText code, name, kindPackage, product;
    private String codeResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_data);
        code = (EditText) findViewById(R.id.code);
        // catch an intent from MainActivity, then assign it to the code field, simultaneously printing it in the right place
        // przechwycenie intent z MainActivity, następnie przypisanie go do pola code, jednocześnie wypisując go w odpowiednim miejscu
        Intent receivedIntent = getIntent();
        codeResult = receivedIntent.getStringExtra("codeResult");
        code.setText(codeResult);
        name = (EditText) findViewById(R.id.name);
        kindPackage = (EditText) findViewById(R.id.kindPackage);
        product = (EditText) findViewById(R.id.product);
        btnSaveData = (Button) findViewById(R.id.btnSaveData);
        dbHelper = new DbHelper(this);


        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeText = code.getText().toString();
                String nameText = name.getText().toString();
                String kindPackegeText = kindPackage.getText().toString();
                String productText = product.getText().toString();
                if ((codeText.length() != 0) && (nameText.length() != 0) && (kindPackegeText.length() != 0) && (productText.length() != 0)) {
                    AddData(codeText, nameText, kindPackegeText, productText);
                    code.setText("");
                    name.setText("");
                    kindPackage.setText("");
                    product.setText("");
                    Intent intent = new Intent(InputData.this, ListDataActivity.class);
                    startActivity(intent);
                } else {
                    toastMessage("Nie możesz zostawić żadnego pola pustego!!!");
                }
            }
        });
    }

    public void AddData(String code, String name, String kindPackage, String product) {
        boolean insertData = dbHelper.addProduct(code, name, kindPackage, product);

        if (insertData) {
            toastMessage("Zapisywanie przebiegło poprawnie");
        } else {
            toastMessage("Coś poszło nie tak");
        }
    }

    // customizable toast
    // konfigurowany toast
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
