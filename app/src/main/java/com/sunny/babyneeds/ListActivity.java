package com.sunny.babyneeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sunny.babyneeds.data.DataBaseHelper;
import com.sunny.babyneeds.model.Item;
import com.sunny.babyneeds.ui.RecyclerViewAdapter;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    FloatingActionButton fab;

    ArrayList<Item> itemArrayList;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    private Button saveButton;
    private EditText itemName;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar my_toolbar = findViewById(R.id.my_o_toolbar);
//        toolbar.setTitle("");
        setSupportActionBar(my_toolbar);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.fab_activity_list);

        dataBaseHelper = new DataBaseHelper(this);

        itemArrayList = new ArrayList<>();

        itemArrayList.addAll(dataBaseHelper.getAllItems());

        adapter = new RecyclerViewAdapter(this, itemArrayList);

        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopDialog();
            }
        });


    }

    private void createPopDialog() {
        builder = new AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_item, null);

        itemName = popupView.findViewById(R.id.textView_popup_name);
        itemQuantity = popupView.findViewById(R.id.textView_popup_quantity);
        itemColor = popupView.findViewById(R.id.textView_popup_color);
        itemSize = popupView.findViewById(R.id.textView_popup_size);
        saveButton = popupView.findViewById(R.id.btn_popup_save);

        builder.setView(popupView);
        alertDialog = builder.create();
        alertDialog.show();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem(view);
            }
        });
    }

    private void saveItem(View view) {
        if (!itemName.getText().toString().isEmpty()
                && !itemQuantity.getText().toString().isEmpty()
                && !itemColor.getText().toString().isEmpty()
                && !itemSize.getText().toString().isEmpty()) {
            String name = String.valueOf(itemName.getText());
            String qty = String.valueOf(itemQuantity.getText());
            String color = String.valueOf(itemColor.getText());
            String size = String.valueOf(itemSize.getText());
            Item item = new Item(name, qty, color, size);
            dataBaseHelper.addItem(item);
            Snackbar.make(view, "Item saved", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    startActivity(new Intent(ListActivity.this,ListActivity.class));
                    finish();
                    overridePendingTransition(0,0);

                }
            }, 1200);

        } else {
            Snackbar.make(view, "Empty fields not allowed", Snackbar.LENGTH_SHORT).show();
        }
    }
}