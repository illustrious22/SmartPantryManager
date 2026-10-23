package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;
    public static final String PANTRY = "pantry";
    public static final String RECIPES = "recipes";

    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, ingredients TEXT NOT NULL, steps TEXT NOT NULL)");
        seedRecipes(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public long addPantryItem(String name, double quantity, String unit, String expiry) {
        ContentValues v = new ContentValues(); v.put("name", name.trim()); v.put("quantity", quantity); v.put("unit", unit.trim()); v.put("expiry", expiry == null ? "" : expiry.trim());
        return getWritableDatabase().insert(PANTRY, null, v);
    }
    public int updatePantryItem(long id, String name, double quantity, String unit, String expiry) {
        ContentValues v = new ContentValues(); v.put("name", name.trim()); v.put("quantity", quantity); v.put("unit", unit.trim()); v.put("expiry", expiry == null ? "" : expiry.trim());
        return getWritableDatabase().update(PANTRY, v, "id=?", new String[]{String.valueOf(id)});
    }
    public int deletePantryItem(long id) { return getWritableDatabase().delete(PANTRY, "id=?", new String[]{String.valueOf(id)}); }

    public ArrayList<PantryItem> getPantryItems() {
        ArrayList<PantryItem> result = new ArrayList<>();
        try (Cursor c = getReadableDatabase().query(PANTRY, null, null, null, null, null, "name COLLATE NOCASE ASC")) {
            while (c.moveToNext()) result.add(new PantryItem(c.getLong(c.getColumnIndexOrThrow("id")), c.getString(c.getColumnIndexOrThrow("name")), c.getDouble(c.getColumnIndexOrThrow("quantity")), c.getString(c.getColumnIndexOrThrow("unit")), c.getString(c.getColumnIndexOrThrow("expiry"))));
        }
        return result;
    }
    public PantryItem getPantryItem(long id) {
        try (Cursor c = getReadableDatabase().query(PANTRY, null, "id=?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (c.moveToFirst()) return new PantryItem(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4));
        }
        return null;
    }
    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> result = new ArrayList<>();
        try (Cursor c = getReadableDatabase().query(RECIPES, null, null, null, null, null, "name COLLATE NOCASE ASC")) {
            while (c.moveToNext()) result.add(new Recipe(c.getLong(0), c.getString(1), c.getString(2), c.getString(3)));
        }
        return result;
    }

    public ArrayList<Recipe> getStrictMatches() {
        ArrayList<Recipe> matches = new ArrayList<>();
        List<PantryItem> pantry = getPantryItems();
        for (Recipe recipe : getRecipes()) {
            boolean qualifies = true;
            for (String requirement : recipe.ingredients.split("\\|")) {
                String[] parts = requirement.split("~");
                String wanted = normalize(parts[0]);
                double required = parts.length > 1 ? Double.parseDouble(parts[1]) : 1;
                PantryItem found = null;
                for (PantryItem item : pantry) if (normalize(item.name).equals(wanted)) { found = item; break; }
                if (found == null || found.quantity < required) { qualifies = false; break; }
            }
            if (qualifies) matches.add(recipe);
        }
        return matches;
    }

    public static String normalize(String value) {
        String s = value.toLowerCase(Locale.US).trim();
        if (s.endsWith("ies")) s = s.substring(0, s.length() - 3) + "y";
        else if (s.endsWith("oes")) s = s.substring(0, s.length() - 2);
        else if (s.endsWith("s") && !s.endsWith("ss")) s = s.substring(0, s.length() - 1);
        return s;
    }

    private void seedRecipes(SQLiteDatabase db) {
        String[][] data = {
            {"Tomato Omelette", "egg~2|tomato~1|onion~1", "Whisk eggs. Saute onion and tomato. Add eggs and cook until set."},
            {"Garden Salad", "lettuce~1|tomato~1|cucumber~1|carrot~1", "Chop vegetables, combine in a bowl, and season to taste."},
            {"Vegetable Stir Fry", "broccoli~1|carrot~1|onion~1|soy sauce~1", "Stir-fry vegetables in a hot pan. Add soy sauce and serve."},
            {"Cheese Toast", "bread~2|cheese~1|butter~1", "Butter bread, add cheese, and toast until golden."},
            {"Banana Pancakes", "banana~1|egg~1|flour~1|milk~1", "Mash banana, mix ingredients, and cook small pancakes in a pan."},
            {"Garlic Pasta", "pasta~1|garlic~1|olive oil~1|parmesan~1", "Boil pasta. Saute garlic in oil, toss with pasta, and top with parmesan."},
            {"Chicken Rice Bowl", "chicken~1|rice~1|carrot~1|soy sauce~1", "Cook rice. Cook chicken and vegetables, then combine with soy sauce."},
            {"Apple Oatmeal", "oats~1|apple~1|milk~1|cinnamon~1", "Simmer oats in milk. Stir in apple and cinnamon."},
            {"Tuna Sandwich", "bread~2|tuna~1|mayonnaise~1|lettuce~1", "Mix tuna and mayonnaise. Fill bread with tuna and lettuce."},
            {"Bean Tacos", "tortilla~2|beans~1|tomato~1|cheese~1", "Warm tortillas, fill with beans, tomato, and cheese."},
            {"Potato Hash", "potato~2|onion~1|pepper~1|olive oil~1", "Dice potato and cook with onion and pepper until tender."},
            {"French Toast", "bread~2|egg~1|milk~1|cinnamon~1", "Dip bread in egg and milk mixture, then cook both sides."},
            {"Lentil Soup", "lentils~1|carrot~1|onion~1|tomato~1", "Simmer lentils and vegetables until soft. Season and serve."},
            {"Yogurt Fruit Bowl", "yogurt~1|banana~1|apple~1|honey~1", "Slice fruit, add yogurt, and drizzle with honey."},
            {"Mushroom Toast", "bread~2|mushroom~1|butter~1|garlic~1", "Saute mushrooms and garlic in butter. Serve over toasted bread."},
            {"Avocado Salad", "avocado~1|tomato~1|onion~1|lemon~1", "Dice ingredients, toss gently, and add lemon juice."},
            {"Creamy Tomato Pasta", "pasta~1|tomato~2|cream~1|garlic~1", "Cook pasta. Simmer tomatoes, cream, and garlic, then combine."},
            {"Egg Fried Rice", "rice~1|egg~1|carrot~1|soy sauce~1", "Scramble egg, fry vegetables and rice, then add soy sauce."}
        };
        for (String[] item : data) { ContentValues v = new ContentValues(); v.put("name", item[0]); v.put("ingredients", item[1]); v.put("steps", item[2]); db.insert(RECIPES, null, v); }
    }

    public static class PantryItem { public long id; public String name, unit, expiry; public double quantity; public PantryItem(long id, String name, double quantity, String unit, String expiry) { this.id=id; this.name=name; this.quantity=quantity; this.unit=unit; this.expiry=expiry; } }
    public static class Recipe { public long id; public String name, ingredients, steps; public Recipe(long id, String name, String ingredients, String steps) { this.id=id; this.name=name; this.ingredients=ingredients; this.steps=steps; } public List<String> ingredientLabels() { ArrayList<String> out = new ArrayList<>(); for (String x : ingredients.split("\\|")) { String[] p=x.split("~"); out.add(p[0]+" (required: "+p[1]+")"); } return out; } }
}
