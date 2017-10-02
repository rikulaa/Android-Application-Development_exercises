package com.example.riku.a12_shoppinglistexercise;

/**
 * Created by Riku on 30.9.2017.
 */

public class ShoppingListItem {
    private String name;
    private int amount;
    private double price;
    private long index;

    public ShoppingListItem(String name, int amount, double price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    // read values
    public long getIndex() { return this.index; }
    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public double getPrice() {
        return this.price;
    }

    // update properties
    public void setIndex (long index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(float price) {
        this.price = price;
    }


}
