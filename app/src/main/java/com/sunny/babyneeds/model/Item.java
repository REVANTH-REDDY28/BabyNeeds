package com.sunny.babyneeds.model;

import androidx.annotation.NonNull;

public class Item {
    private int id;
    private String name;
    private String quantity;
    private String color;
    private String size;

    @NonNull
    @Override
    public String toString() {
        return String.format(" id:%d name:%s ",id,name);
    }

    public Item() {
    }

    public Item(String name, String quantity, String color, String size) {
        this.name = name;
        this.quantity = quantity;
        this.color = color;
        this.size = size;
    }

    public Item(int id, String name, String quantity, String color, String size) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.color = color;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
