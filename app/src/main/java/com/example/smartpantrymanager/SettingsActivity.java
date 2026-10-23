package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){super.onCreate(b);SharedPreferences prefs=getSharedPreferences("settings",MODE_PRIVATE);LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(24,28,24,20);root.setBackgroundColor(0xfff6faf7);TextView title=new TextView(this);title.setText("Settings");title.setTextSize(28);title.setTypeface(null,1);title.setTextColor(0xff18332a);root.addView(title);TextView intro=new TextView(this);intro.setText("Personalise your pantry experience.");intro.setTextSize(15);intro.setTextColor(0xff557064);root.addView(intro);Switch alerts=new Switch(this);alerts.setText("Expiring-soon reminders");alerts.setTextSize(17);alerts.setChecked(prefs.getBoolean("expiry_alerts",true));alerts.setOnCheckedChangeListener((button,checked)->prefs.edit().putBoolean("expiry_alerts",checked).apply());root.addView(alerts,new LinearLayout.LayoutParams(-1,-2));TextView about=new TextView(this);about.setText("Smart Pantry Manager\n\nLocal SQLite storage keeps your pantry available after the app is closed. Recipe suggestions always require every listed ingredient and the required quantity.");about.setTextSize(16);about.setTextColor(0xff18332a);about.setPadding(0,28,0,0);root.addView(about);setContentView(root);}
}
