package com.example.admin.ggupta_pos_app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;


public class POSActivity extends AppCompatActivity {


    // properties related to orders
    boolean isTotalCalculated = false;
    Order order;
    FoodItem foodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        // getting UI controls
        final AutoCompleteTextView itemNameACTV = (AutoCompleteTextView) findViewById(R.id.editItem);
        final EditText unitPriceEV = (EditText) findViewById(R.id.editUnitPrice);
        final EditText quantityEV = (EditText) findViewById(R.id.editQuantity);
        final TextView totalTV = (TextView) findViewById(R.id.textTotal);
        final TextView summaryTV = (TextView) findViewById(R.id.textSummary);

        // setting adapter for auto-complete for item name
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ITEMS);
        itemNameACTV.setAdapter(adapter);

        // handling click event for clicking an item in auto-complete dropdown
        itemNameACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getAdapter().getItem(position);
                if (Arrays.asList(ITEMS).contains(selectedItem.toLowerCase())){
                    double unitPrice = PopulateUnitPrice(selectedItem);
                    unitPriceEV.setText(Double.toString(unitPrice));
                }
                else {
                    unitPriceEV.setText("0.00");
                }
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        // handling event for manually entering an item name
        itemNameACTV.setOnEditorActionListener(
                new AutoCompleteTextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            InputMethodManager imm = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            unitPriceEV.setText("0.00");
                            handled = true;
                        }
                        return handled;
                    }
        });

        // handling New Item button's click event
        Button btnNewItem = (Button) findViewById(R.id.btnNewItem);
        btnNewItem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //isTotalCalculated = false;

                if(isTotalCalculated){
                    ResetFields();
                    order = AddNewItem(order);
                    isTotalCalculated = false;
                }
                else{
                    order = AddNewItem(order);
                    ResetFields();
                    isTotalCalculated = false;
                }
            }
        });

        // handling click event for Total button
        Button btnTotal = (Button) findViewById(R.id.btnTotal);
        btnTotal.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                double totalPrice = 0.0;
                String summary = "";
                if(!isTotalCalculated) {    // if total is not calculated, then calculate it
                    if (order != null && order.TotalItems.size() != 0) {
                        order = AddNewItem(order);
                        for (FoodItem fi : order.TotalItems) {
                            totalPrice += (fi.UnitPrice * fi.Quantity);
                            summary = summary + fi.Name + " x" + Integer.toString(fi.Quantity) + "\r\n";
                            totalTV.setText("$" + Double.toString(totalPrice));
                            summaryTV.setText(summary);
                        }
                    } else {
                        String prevItemName = itemNameACTV.getText().toString();
                        Integer prevItemQuantity = Integer.parseInt(quantityEV.getText().toString());
                        Double prevItemUnitPrice = Double.parseDouble(unitPriceEV.getText().toString());
                        if ((prevItemName != null && prevItemName != "") && prevItemQuantity > 0 && prevItemUnitPrice > 0) {
                            totalPrice += (prevItemQuantity * prevItemUnitPrice);
                            summary = summary + prevItemName + " x" + Integer.toString(prevItemQuantity) + "\r\n";
                            totalTV.setText("$" + Double.toString(totalPrice));
                            summaryTV.setText(summary);
                        }
                    }
                    isTotalCalculated = true;
                }
                //ResetFields();
            }
        });


        // handling click event New Order button
        Button btnNewOrder = (Button) findViewById(R.id.btnNewOrder);
        btnNewOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ResetFields();
                summaryTV.setText("");
                order = new Order();
                isTotalCalculated = false;
            }
        });

    }
    protected Order AddNewItem(Order order){

        AutoCompleteTextView itemNameACTV = (AutoCompleteTextView) findViewById(R.id.editItem);
        EditText unitPriceEV = (EditText) findViewById(R.id.editUnitPrice);
        EditText quantityEV = (EditText) findViewById(R.id.editQuantity);

        String prevItemName = itemNameACTV.getText().toString();
        Integer prevItemQuantity = Integer.parseInt(quantityEV.getText().toString());
        Double prevItemUnitPrice = Double.parseDouble(unitPriceEV.getText().toString());
        if ((prevItemName != null && prevItemName != "") && prevItemQuantity > 0 && prevItemUnitPrice > 0) {
            foodItem = new FoodItem(prevItemName, prevItemQuantity, prevItemUnitPrice);
            if (order == null) {
                order = new Order();
            }
            order.TotalItems.add(foodItem);
        }
        return order;
    }
    protected void ResetFields(){

        // getting UI controls
        AutoCompleteTextView itemNameACTV = (AutoCompleteTextView) findViewById(R.id.editItem);
        EditText unitPriceEV = (EditText) findViewById(R.id.editUnitPrice);
        EditText quantityEV = (EditText) findViewById(R.id.editQuantity);
        TextView totalTV = (TextView) findViewById(R.id.textTotal);
        TextView summaryTV = (TextView) findViewById(R.id.textSummary);

        itemNameACTV.setText("");
        quantityEV.setText("1");
        unitPriceEV.setText("0.00");
        totalTV.setText("$0.00");
        //summaryTV.setText("");
    }
    protected double PopulateUnitPrice(String item){

        HashMap<String,String> hm = new HashMap<String, String>();
        hm.put("Pasta","4.00");
        hm.put("burger","3.00");
        hm.put("hot dog","3.00");
        hm.put("salad","5.00");
        hm.put("donut","2.00");
        hm.put("pastry","3.00");
        hm.put("muffin","1.50");
        hm.put("sandwich","2.50");
        hm.put("diet coke","1.00");

        Double unitPrice = Double.parseDouble( hm.get(item).toString());
        /*
        double unitPrice = 0.00;
        switch (item) {
            case "pasta":
                unitPrice = 4.00;
                break;
            case "burger":
                unitPrice = 3.00;
                break;
            case "hot dog":
                unitPrice = 3.00;
                break;
            case "salad":
                unitPrice = 5.00;
                break;
            case "donut":
                unitPrice = 2.00;
                break;
            case "pastry":
                unitPrice = 3.00;
                break;
            case "muffin":
                unitPrice = 1.50;
                break;
            case "diet coke":
                unitPrice = 1.00;
                break;
            case "sandwich":
                unitPrice = 2.50;
                break;
            default: unitPrice = 0.00;
                break;
        }
        */
        return unitPrice;
    }

    static final String[] ITEMS = new String[] {
            "pasta","burger","hot dog","salad","donut","pastry","muffin","diet coke","sandwich"
    };

    // protected static final Dictionary<String,double> ITEM_PRICES_LIST = new Dictionary<String, double>() {    }
}
