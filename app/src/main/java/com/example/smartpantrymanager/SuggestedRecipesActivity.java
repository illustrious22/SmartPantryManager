package com.example.smartpantrymanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private DatabaseHelper db; private LinearLayout root;
    @Override protected void onCreate(Bundle b){super.onCreate(b);db=new DatabaseHelper(this);build();}
    @Override protected void onResume(){super.onResume();if(root!=null)populate();}
    private void build(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(24,28,24,20);root.setBackgroundColor(0xfff6faf7);TextView title=new TextView(this);title.setText("Suggested Recipes");title.setTextSize(27);title.setTypeface(null,1);title.setTextColor(0xff18332a);root.addView(title);TextView hint=new TextView(this);hint.setText("Only recipes you can make with every required ingredient are shown.");hint.setTextSize(15);hint.setTextColor(0xff557064);root.addView(hint);setContentView(root);}
    private void populate(){while(root.getChildCount()>2)root.removeViewAt(2);ArrayList<DatabaseHelper.Recipe> recipes=db.getStrictMatches();if(recipes.isEmpty()){TextView empty=new TextView(this);empty.setText("No recipes match your pantry yet — add more ingredients or increase quantities.");empty.setTextSize(17);empty.setTextColor(0xff18332a);empty.setPadding(0,36,0,20);root.addView(empty);return;}ListView list=new ListView(this);ArrayList<String> labels=new ArrayList<>();for(DatabaseHelper.Recipe r:recipes)labels.add(r.name);list.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,labels));list.setOnItemClickListener((p,v,pos,which)->{Intent i=new Intent(this,RecipeDetailActivity.class);i.putExtra("recipeId",recipes.get(pos).id);startActivity(i);});root.addView(list,new LinearLayout.LayoutParams(-1,0,1));}
}
