package com.example.admin.ggupta_pos_app;

import android.content.ClipData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Admin on 9/29/2017.
 */

public class Order {
    public ArrayList<FoodItem> TotalItems;
    public Order()
    {
        this.TotalItems = new ArrayList<FoodItem>();
    }
}
