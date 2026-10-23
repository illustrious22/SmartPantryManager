package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddEditIngredientActivity extends AppCompatActivity {
    private DatabaseHelper db; private EditText name, quantity, unit, expiry; private long id=-1;
    @Override protected void onCreate(Bundle b){super.onCreate(b); db=new DatabaseHelper(this); id=getIntent().getLongExtra("id",-1); build(); if(id!=-1) load();}
    private void build(){ LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(24,28,24,20);root.setBackgroundColor(0xfff6faf7);
        TextView title=new TextView(this);title.setText(id==-1?"Add pantry ingredient":"Edit pantry ingredient");title.setTextSize(26);title.setTypeface(null,1);title.setTextColor(0xff18332a);root.addView(title,lp(-1,-2));
        name=input("Ingredient name (e.g. tomatoes)",false);quantity=input("Quantity",true);unit=input("Unit (e.g. pieces, cups)",false);expiry=input("Expiry date (optional)",false);expiry.setFocusable(false);expiry.setOnClickListener(v->pickDate());
        root.addView(name);root.addView(quantity);root.addView(unit);root.addView(expiry);
        Button save=new Button(this);save.setText("Save ingredient");save.setAllCaps(false);save.setOnClickListener(v->save());root.addView(save,lp(-1,-2));
        Button cancel=new Button(this);cancel.setText("Cancel");cancel.setAllCaps(false);cancel.setOnClickListener(v->finish());root.addView(cancel,lp(-1,-2));setContentView(root); }
    private EditText input(String hint,boolean number){EditText e=new EditText(this);e.setHint(hint);e.setTextSize(16);e.setPadding(8,12,8,12);if(number)e.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);LinearLayout.LayoutParams p=lp(-1,-2);p.bottomMargin=8;e.setLayoutParams(p);return e;}
    private void load(){DatabaseHelper.PantryItem i=db.getPantryItem(id);if(i!=null){name.setText(i.name);quantity.setText(String.valueOf(i.quantity));unit.setText(i.unit);expiry.setText(i.expiry);}}
    private void pickDate(){Calendar c=Calendar.getInstance();new DatePickerDialog(this,(v,y,m,d)->expiry.setText(String.format(java.util.Locale.US,"%04d-%02d-%02d",y,m+1,d)),c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();}
    private void save(){String n=name.getText().toString().trim(),q=quantity.getText().toString().trim(),u=unit.getText().toString().trim();if(n.isEmpty()){name.setError("Enter an ingredient name");name.requestFocus();return;}if(q.isEmpty()){quantity.setError("Enter a quantity");quantity.requestFocus();return;}if(u.isEmpty()){unit.setError("Enter a unit");unit.requestFocus();return;}double amount;try{amount=Double.parseDouble(q);}catch(Exception e){quantity.setError("Use a valid number");return;}if(amount<=0){quantity.setError("Quantity must be greater than zero");return;}if(id==-1)db.addPantryItem(n,amount,u,expiry.getText().toString());else db.updatePantryItem(id,n,amount,u,expiry.getText().toString());Toast.makeText(this,"Ingredient saved",Toast.LENGTH_SHORT).show();finish();}
    private LinearLayout.LayoutParams lp(int w,int h){return new LinearLayout.LayoutParams(w,h);}
}
