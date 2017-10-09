package com.example.admin.ggupta_pos_app;

/**
 * Created by Admin on 9/29/2017.
 */

public class FoodItem {
    public String Name;
    public int Quantity;
    public double UnitPrice;


    public FoodItem(){ }

    public FoodItem(String itemName,int qty, double unitPrice){
        this.Name = itemName;
        this.Quantity = qty;
        this.UnitPrice = unitPrice;
    }

}

