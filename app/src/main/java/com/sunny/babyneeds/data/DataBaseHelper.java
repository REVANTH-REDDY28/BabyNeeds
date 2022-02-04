package com.sunny.babyneeds.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sunny.babyneeds.model.Item;
import com.sunny.babyneeds.util.Util;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "
                +Util.TABLE_NAME_ITEMS
                +"( "
                +Util.TABLE_ITEM_COLUMN_ID+" INTEGER PRIMARY KEY ,"
                +Util.TABLE_ITEM_COLUMN_NAME+" TEXT ,"
                +Util.TABLE_ITEM_COLUMN_QUANTITY+" TEXT ,"
                +Util.TABLE_ITEM_COLUMN_COLOR+" TEXT, "
                +Util.TABLE_ITEM_COLUMN_SIZE+" TEXT "
                +")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Util.TABLE_NAME_ITEMS);
        onCreate(db);
    }

    //crud addItem getItem updateItem deleteItem
    public boolean addItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.TABLE_ITEM_COLUMN_NAME,item.getName());
        contentValues.put(Util.TABLE_ITEM_COLUMN_QUANTITY,item.getQuantity());
        contentValues.put(Util.TABLE_ITEM_COLUMN_COLOR,item.getColor());
        contentValues.put(Util.TABLE_ITEM_COLUMN_SIZE,item.getSize());

        db.insert(Util.TABLE_NAME_ITEMS,null,contentValues);
        return true;
    }

    @SuppressLint("Range")
    public Item getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                +Util.TABLE_NAME_ITEMS
                +" WHERE "
        +Util.TABLE_ITEM_COLUMN_ID+" = "
        +id+"",null);
        if(cursor != null)
            cursor.moveToFirst();
        Item item = new Item();
        if(cursor != null){
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.TABLE_ITEM_COLUMN_ID))));
            item.setName(cursor.getString(cursor.getColumnIndex(Util.TABLE_ITEM_COLUMN_NAME)));
            item.setQuantity(cursor.getString(cursor.getColumnIndex(Util.TABLE_ITEM_COLUMN_QUANTITY)));
            item.setColor(cursor.getString(cursor.getColumnIndex(Util.TABLE_ITEM_COLUMN_COLOR)));
            item.setSize(cursor.getString(cursor.getColumnIndex(Util.TABLE_ITEM_COLUMN_SIZE)));

        }
        return item;
    }

    public boolean updateItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.TABLE_ITEM_COLUMN_ID,item.getId());
        values.put(Util.TABLE_ITEM_COLUMN_NAME,item.getName());
        values.put(Util.TABLE_ITEM_COLUMN_QUANTITY,item.getQuantity());
        values.put(Util.TABLE_ITEM_COLUMN_COLOR,item.getColor());
        values.put(Util.TABLE_ITEM_COLUMN_SIZE,item.getSize());
        db.update(Util.TABLE_NAME_ITEMS,values,Util.TABLE_ITEM_COLUMN_ID+" = ?",
                new String[]{String.valueOf(item.getId())});
        return true;
    }

    public boolean deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME_ITEMS,Util.TABLE_ITEM_COLUMN_ID+" = ? ",
                new String[]{String.valueOf(id)});
        return true;
    }

    public ArrayList<Item> getAllItems(){
        ArrayList<Item> itemArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+Util.TABLE_NAME_ITEMS,null);
        if(cursor.moveToFirst()){
            do{
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setName(cursor.getString(1));
                item.setQuantity(cursor.getString(2));
                item.setColor(cursor.getString(3));
                item.setSize(cursor.getString(4));
                itemArrayList.add(item);
            }while (cursor.moveToNext());
        }
        return  itemArrayList;
    }

    public int getItemCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+Util.TABLE_NAME_ITEMS,null);
        return cursor.getCount();
    }

}
