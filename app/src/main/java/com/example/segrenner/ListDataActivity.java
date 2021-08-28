package com.example.segrenner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segrenner.Datebases.DbHelper;

import java.util.ArrayList;

// class to the tab showing a list of all products
// klasa do zakładki pokazująca listę wszystkich produktów
public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DbHelper dbHelper;

    private ListView listView;
    private Button btnScanAgain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        listView = (ListView) findViewById(R.id.listView);
        btnScanAgain = (Button) findViewById(R.id.bntScanAgain);
        dbHelper = new DbHelper(this);

        productListView();

        btnScanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListDataActivity.this, "Ponowne skanowanie", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ListDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void productListView() {
        Log.d(TAG, "productListView: Displaying data in the ListView");

        // get the data and append to a list
        // pobierz dane i dołącz do listy
        Cursor data = dbHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // get the value from the database in column 1, then add it to the ArrayList
            // pobierz wartość z bazy danych w kolumnie 1, następnie dodaj ją do ArrayList
            listData.add(data.getString(2));
        }
        // create the list adapter and set the adapter
        // stwórz listę adapterów i ustaw adapter
        // to jak i to wyrzej może być inaczej żeby pobierało więcej kolumn niż tylko jedną
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);

        // set an onItemCliclistener to the ListView
        // ustaw onItemCliclistener na ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nameProduct = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + nameProduct);

                Cursor data = dbHelper.getProductCode(nameProduct);
                // get the code associated with that nameProduct
                // pobierz kod powiązany z tą nazwąProduktu
                String code = null;
                while (data.moveToNext()) {
                   code = data.getString(0);
                }
                if (code !=null) {
                    Log.d(TAG, "onItemClick: The Code is: " + code);
                    Intent editScreenIntent = new Intent(ListDataActivity.this, ShowProduct.class);
                    // issue the value (that is, the String value) of the product code to be captured in the EditDataActivity class
                    // wystawienie wartości (czyli wartość String)code produktu do przechwycenia w klasie EditDataActivity
                    editScreenIntent.putExtra("codeResult", code);
                    startActivity(editScreenIntent);
                } else {
                    toastMessage("Nie ma kodu powiązanego z tą nazwą produktu");
                }
            }
        });
    }

    // customizable toast
    // konfigurowany toast
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
