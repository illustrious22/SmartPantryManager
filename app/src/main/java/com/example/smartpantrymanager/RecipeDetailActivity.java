package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){super.onCreate(b);DatabaseHelper db=new DatabaseHelper(this);long id=getIntent().getLongExtra("recipeId",-1);DatabaseHelper.Recipe recipe=null;for(DatabaseHelper.Recipe r:db.getRecipes())if(r.id==id)recipe=r;LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(24,28,24,24);root.setBackgroundColor(0xfff6faf7);if(recipe==null){TextView t=new TextView(this);t.setText("Recipe not found");root.addView(t);setContentView(root);return;}TextView title=t(recipe.name,28,true);root.addView(title);root.addView(t("Ingredients",20,true));for(String label:recipe.ingredientLabels())root.addView(t("• "+label,16,false));root.addView(t("Preparation",20,true));root.addView(t(recipe.steps,16,false));setContentView(root);}
    private TextView t(String s,int size,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(0xff18332a);t.setPadding(0,8,0,8);if(bold)t.setTypeface(null,1);return t;}
}
