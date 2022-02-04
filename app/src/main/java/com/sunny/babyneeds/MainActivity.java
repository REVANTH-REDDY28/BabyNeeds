package com.sunny.babyneeds;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sunny.babyneeds.data.DataBaseHelper;
import com.sunny.babyneeds.model.Item;

public class MainActivity extends AppCompatActivity {
    //popup
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText itemName;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;

    DataBaseHelper myDb;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            Toast.makeText(this,"settings",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar my_toolbar = findViewById(R.id.my_o_toolbar);
//        toolbar.setTitle("");
        setSupportActionBar(my_toolbar);

        myDb = new DataBaseHelper(this);
        byPassActivity();

        //------------fab-------
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });




//        myDb.addItem(new Item("diaper1","1","blue","1"));
//        myDb.addItem(new Item("diaper2","1","blue","1"));
//        myDb.addItem(new Item("diaper3","1","blue","1"));
//        myDb.addItem(new Item("diaper4","1","blue","1"));
//        myDb.deleteItem(0);
//        Item item = new Item(1,"cheddi1","1","blue","1");
//        myDb.updateItem(item);
        Log.d("loli", "onCreate: "+myDb.getAllItems());
    }

    private void byPassActivity() {
        if(myDb.getItemCount() > 0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }


    private void createPopupDialog() {
        builder = new AlertDialog.Builder(MainActivity.this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_item,null);

        itemName = popupView.findViewById(R.id.textView_popup_name);
        itemQuantity = popupView.findViewById(R.id.textView_popup_quantity);
        itemColor = popupView.findViewById(R.id.textView_popup_color);
        itemSize = popupView.findViewById(R.id.textView_popup_size);
        saveButton = popupView.findViewById(R.id.btn_popup_save);

        builder.setView(popupView);
        dialog = builder.create();
        dialog.show();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem(view);
            }
        });
    }

    private void saveItem(View view) {
        if(!itemName.getText().toString().isEmpty()
        &&!itemQuantity.getText().toString().isEmpty()
        &&!itemColor.getText().toString().isEmpty()
        &&!itemSize.getText().toString().isEmpty()){
            String name = String.valueOf(itemName.getText());
            String qty = String.valueOf(itemQuantity.getText());
            String color = String.valueOf(itemColor.getText());
            String size = String.valueOf(itemSize.getText());
            Item item = new Item(name,qty,color,size);
            myDb.addItem(item);
            dialog.dismiss();
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            overridePendingTransition(0,0);
            finish();

        }else{
            Snackbar.make(view,"Empty fields not allowed",Snackbar.LENGTH_SHORT).show();
        }
    }
}