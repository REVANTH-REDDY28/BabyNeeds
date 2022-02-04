package com.sunny.babyneeds.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sunny.babyneeds.R;
import com.sunny.babyneeds.data.DataBaseHelper;
import com.sunny.babyneeds.model.Item;

import java.text.MessageFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<Item> itemArrayList;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    EditText itemName;
    EditText itemQuantity;
    EditText itemColor;
    EditText itemSize;

    public RecyclerViewAdapter(Context context, ArrayList<Item> itemArrayList){
      this.context = context;
      this.itemArrayList = itemArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_item,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Item item = itemArrayList.get(position);
            holder.nameTextView.setText(MessageFormat.format("name: {0}", item.getName()));
            holder.qtyTextView.setText(MessageFormat.format("quantity: {0}", item.getQuantity()));
            holder.colorTextView.setText(MessageFormat.format("color: {0}", item.getColor()));
            holder.sizeTextView.setText(MessageFormat.format("size: {0}", item.getSize()));
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        public TextView nameTextView;
        public TextView qtyTextView;
        public TextView colorTextView;
        public TextView sizeTextView;
        public TextView dateTextView;
        public ImageButton editBtn;
        public ImageButton deleteBtn;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context = ctx;
            nameTextView = itemView.findViewById(R.id.textView_recycleItem_name);
            qtyTextView = itemView.findViewById(R.id.textView_recycleItem_quantity);
            colorTextView = itemView.findViewById(R.id.textView_recycleItem_color);
            sizeTextView = itemView.findViewById(R.id.textView_recycleItem_size);
            dateTextView = itemView.findViewById(R.id.textView_recycleItem_date);
            editBtn = itemView.findViewById(R.id.button_recycleItem_edit);
            deleteBtn = itemView.findViewById(R.id.button_recycleItem_delete);

            editBtn.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            switch (view.getId()){
                case R.id.button_recycleItem_edit:
                    position = getAdapterPosition();
                    Item itemUpdate = itemArrayList.get(position);
                    //popup to edit
                    updateItemDialog(itemUpdate);
//                    updateItem(itemUpdate);
                    break;
                case R.id.button_recycleItem_delete:
                    position = getAdapterPosition();
                    Item itemDelete = itemArrayList.get(position);
                    deleteItem(itemDelete.getId(),position);
                    break;
            }
        }

        private void updateItemDialog(Item itemUpdate) {
            builder = new AlertDialog.Builder(context);

            View popupView = LayoutInflater.from(context).inflate(R.layout.popup_item,null);

            itemName = popupView.findViewById(R.id.textView_popup_name);
            itemQuantity = popupView.findViewById(R.id.textView_popup_quantity);
            itemColor = popupView.findViewById(R.id.textView_popup_color);
            itemSize = popupView.findViewById(R.id.textView_popup_size);
            Button saveButton = popupView.findViewById(R.id.btn_popup_save);

            itemName.setText(itemUpdate.getName());
            itemQuantity.setText(itemUpdate.getQuantity());
            itemColor.setText(itemUpdate.getColor());
            itemSize.setText(itemUpdate.getSize());

            builder.setView(popupView);
            alertDialog = builder.create();
            alertDialog.show();
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveItem(view,itemUpdate);
                }
            });
        }



        private void deleteItem(int itemDeleteId, int adapterPosition) {
            deleteAlert(itemDeleteId,adapterPosition);

        }
        private void saveItem(View view,Item itemUpdate) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            if (!itemName.getText().toString().isEmpty()
                    && !itemQuantity.getText().toString().isEmpty()
                    && !itemColor.getText().toString().isEmpty()
                    && !itemSize.getText().toString().isEmpty()) {

                itemUpdate.setName(String.valueOf(itemName.getText()));
                itemUpdate.setQuantity(String.valueOf(itemQuantity.getText()));
                itemUpdate.setColor(String.valueOf(itemColor.getText()));
                itemUpdate.setSize(String.valueOf(itemSize.getText()));

                dataBaseHelper.updateItem(itemUpdate);
                Snackbar.make(view, "Item updated", Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                        notifyItemChanged(getAdapterPosition());

                    }
                }, 1000);

            } else {
                Snackbar.make(view, "Empty fields not allowed", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteAlert(int id, int adapterPosition) {
        builder = new AlertDialog.Builder(this.context);
        builder.setMessage("Do you really want to delete?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataBaseHelper db = new DataBaseHelper(RecyclerViewAdapter.this.context);
                db.deleteItem(id);
                itemArrayList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                dialogInterface.dismiss();

            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.setOnShowListener(args0 ->{
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        });
        alertDialog.show();
    }
}
