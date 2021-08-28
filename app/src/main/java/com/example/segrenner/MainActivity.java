package com.example.segrenner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segrenner.Datebases.DbHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

// main class - tab that starts the entire program
// główna klasa - zakładka rozpoczynająca cały program
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    Button scanBtn, listViewBtn;
    DbHelper dbHelper;
    private String codeResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Start MainActivity");

        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);
        listViewBtn = findViewById(R.id.listViewBtn);
        listViewBtn.setOnClickListener(this);
        dbHelper = new DbHelper(this);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        listViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewBtn();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    private void scanCode() {
        Log.d(TAG, "scanCode: Code scanning");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Skanowanie");
        integrator.initiateScan();
    }

    private void listViewBtn(){
        Log.d(TAG, "listViewBtn: List view");
        Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //creating a scan result (result) and extracting a code number from it (codeResult)
        //utworzenie wyniku skanowania (result) i wyodrębnienie z niego numeru kodu (codeResult)
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        codeResult = result.getContents();
        //checking if there is a product and such code in the database
        //sprawdzenie czy istnieje produkt i takim kodzie w bazie danych
        boolean existanceResult = CheckIfTheProductExists(codeResult);
        if (result != null) {
            if (codeResult != null) {
                Log.d(TAG, "onActivityResult: Scan result = " + result.getContents());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Wynik Skanowania");
                builder.setPositiveButton("Zapisz Skan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (existanceResult != false) {
                            Intent intent = new Intent(MainActivity.this, ShowProduct.class);
                            intent.putExtra("codeResult", codeResult);
                            startActivity(intent);
                        } else {
                            //downloaded the scan result and set the intent to capture in the InputData class
                            //pobrałem wynik skanowania, a intentem wystawiam do przechwycenia w klasie InputData
                            Intent intent = new Intent(MainActivity.this, InputData.class);
                            intent.putExtra("codeResult", codeResult);
                            startActivity(intent);
                        }
                    }
//                }).setPositiveButton("Skanuj Ponownie", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        scanCode();
//                    }
                }).setNegativeButton("Zakończ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "Nie ma wyniku", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean CheckIfTheProductExists(String code) {
        boolean result = dbHelper.checkIfTheProductExists(code);
        if (result) {
            return true;
        } else {
            return false;
        }
    }
}