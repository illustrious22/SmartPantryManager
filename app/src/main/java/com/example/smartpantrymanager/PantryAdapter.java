package com.example.smartpantrymanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.Holder> {
    public interface Listener { void onEdit(DatabaseHelper.PantryItem item); void onDelete(DatabaseHelper.PantryItem item); }
    private final Listener listener; private final ArrayList<DatabaseHelper.PantryItem> items = new ArrayList<>();
    public PantryAdapter(Listener listener, android.content.Context ignored) { this.listener=listener; }
    public void submit(ArrayList<DatabaseHelper.PantryItem> data){items.clear();items.addAll(data);notifyDataSetChanged();}
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        LinearLayout row=new LinearLayout(parent.getContext()); row.setOrientation(LinearLayout.VERTICAL); row.setPadding(16,14,16,14); row.setBackgroundColor(Color.WHITE);
        return new Holder(row);
    }
    @Override public void onBindViewHolder(@NonNull Holder h,int position){
        DatabaseHelper.PantryItem item=items.get(position); h.title.setText(item.name); h.details.setText(String.format(java.util.Locale.US,"%.2f %s%s",item.quantity,item.unit,item.expiry.isEmpty()?"":"  •  Expires "+item.expiry));
        h.actions.removeAllViews(); Button edit=new Button(h.actions.getContext()); edit.setText("Edit"); edit.setAllCaps(false); edit.setOnClickListener(v->listener.onEdit(item)); Button del=new Button(h.actions.getContext()); del.setText("Delete"); del.setAllCaps(false); del.setOnClickListener(v->listener.onDelete(item)); h.actions.addView(edit,new LinearLayout.LayoutParams(0,-2,1)); h.actions.addView(del,new LinearLayout.LayoutParams(0,-2,1));
    }
    @Override public int getItemCount(){return items.size();}
    static class Holder extends RecyclerView.ViewHolder { TextView title,details; LinearLayout actions; Holder(View v){super(v); LinearLayout row=(LinearLayout)v; title=new TextView(v.getContext()); title.setTextSize(18);title.setTextColor(0xff18332a);title.setTypeface(null,1); details=new TextView(v.getContext());details.setTextSize(14);details.setTextColor(0xff557064);actions=new LinearLayout(v.getContext());actions.setOrientation(LinearLayout.HORIZONTAL);row.addView(title);row.addView(details);row.addView(actions); } }
}
