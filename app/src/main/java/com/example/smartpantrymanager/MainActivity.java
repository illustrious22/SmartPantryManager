package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PantryAdapter.Listener {
    private DatabaseHelper db; private PantryAdapter adapter; private TextView empty;
    @Override protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); db = new DatabaseHelper(this); buildUi(); }
    @Override protected void onResume() { super.onResume(); if (db != null && adapter != null) refresh(); }
    private void buildUi() {
        LinearLayout root = base();
        TextView title = text("Smart Pantry Manager", 28, true); root.addView(title, lp(LinearLayout.LayoutParams.MATCH_PARENT, -2));
        TextView subtitle = text("Use what you have. Waste less.", 15, false); subtitle.setTextColor(0xff557064); root.addView(subtitle, lp(LinearLayout.LayoutParams.MATCH_PARENT, -2));
        LinearLayout actions = new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL); actions.setPadding(0, 20, 0, 8);
        Button add = button("+ Add ingredient"); add.setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class))); actions.addView(add, weightLp());
        Button suggest = button("Suggestions"); suggest.setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class))); actions.addView(suggest, weightLp()); root.addView(actions, lp(-1,-2));
        empty = text("Your pantry is empty. Add ingredients to get started.", 16, false); empty.setGravity(Gravity.CENTER); empty.setPadding(16, 40, 16, 40); root.addView(empty, lp(-1,-2));
        RecyclerView list = new RecyclerView(this); list.setLayoutManager(new LinearLayoutManager(this)); adapter = new PantryAdapter(this, this); list.setAdapter(adapter); root.addView(list, new LinearLayout.LayoutParams(-1, 0, 1));
        Button settings = button("Settings"); settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class))); root.addView(settings, lp(-1,-2)); setContentView(root); refresh();
    }
    private void refresh() { ArrayList<DatabaseHelper.PantryItem> items = db.getPantryItems(); adapter.submit(items); empty.setVisibility(items.isEmpty()?View.VISIBLE:View.GONE); }
    @Override public void onEdit(DatabaseHelper.PantryItem item) { Intent i = new Intent(this, AddEditIngredientActivity.class); i.putExtra("id", item.id); startActivity(i); }
    @Override public void onDelete(DatabaseHelper.PantryItem item) { new AlertDialog.Builder(this).setTitle("Delete ingredient?").setMessage("Remove "+item.name+" from your pantry?").setNegativeButton("Cancel", null).setPositiveButton("Delete", (d,w)->{db.deletePantryItem(item.id); refresh(); Toast.makeText(this,"Ingredient deleted",Toast.LENGTH_SHORT).show();}).show(); }
    private LinearLayout base(){ LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(22,28,22,16); l.setBackgroundColor(0xfff6faf7); return l; }
    private TextView text(String s,int size,boolean bold){ TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(0xff18332a); if(bold)t.setTypeface(null,1); t.setPadding(0,4,0,4); return t; }
    private Button button(String s){ Button b=new Button(this); b.setText(s); b.setAllCaps(false); return b; }
    private LinearLayout.LayoutParams lp(int w,int h){return new LinearLayout.LayoutParams(w,h);}
    private LinearLayout.LayoutParams weightLp(){return new LinearLayout.LayoutParams(0,-2,1);}
}
