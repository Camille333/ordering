package com.example.ordering;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {
    /*
     声明一个AndroidSDK自带的数据库变量db
     */
    private SQLiteDatabase db;

    DBOpenHelper(Context context){
        super(context,"db_test",null,1);
        db = getReadableDatabase();
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "password TEXT," +
                "email TEXT," +
                "phonenum TEXT)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS food(" +
                "food_name TEXT PRIMARY KEY AUTOINCREMENT," +
                "food_price FLOAT," +
                "food_count INTEGER)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS store(" +
                "store_name TEXT PRIMARY KEY AUTOINCREMENT," +
                "address TEXT)"
        );

    }

    //版本适应
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS food");
        db.execSQL("DROP TABLE IF EXISTS store");
        onCreate(db);
    }
    /*
     * 自定义增删改查方法
     * add()
     * delete()
     * update()
     * getAllData()
     */

    void add(String name, String password,String email,String phonenum){
        db.execSQL("INSERT INTO user (name,password,email,phonenum) VALUES(?,?,?,?)",new Object[]{name,password,email,phonenum});
    }

    public void delete(String name,String password){
        db.execSQL("DELETE FROM user WHERE name = AND password ="+name+password);
    }
    public void updata(String password){
        db.execSQL("UPDATE user SET password = ?",new Object[]{password});
    }

    ArrayList<User> getAllData(){
        ArrayList<User> list = new ArrayList<User>();
        @SuppressLint("Recycle") Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String phonenum = cursor.getString(cursor.getColumnIndex("phonenum"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));

            list.add(new User(name,password,email,phonenum));
        }
        return list;
    }

    public void add_food(List<Food> list){
        //将list列表的每行元素逐次插入数据表food中
        for (Food item : list) {
            db.execSQL("INSERT INTO food (food_name, food_price, food_count) VALUES(?, ?, ?)",
                    new Object[]{item.getFood_name(), item.getFood_price(), item.getFood_count()});
        }
        db.close();
    }

    ArrayList<Food> getFoodData(){
        ArrayList<Food> list = new ArrayList<Food>();
        @SuppressLint("Recycle") Cursor cursor = db.query("food",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            @SuppressLint("Range") String food_name = cursor.getString(cursor.getColumnIndex("food_name"));
            @SuppressLint("Range") Float food_price = cursor.getFloat(cursor.getColumnIndex("food_price"));
            @SuppressLint("Range") Integer food_count = cursor.getInt(cursor.getColumnIndex("food_count"));

            list.add(new Food(food_name, food_price, food_count));
        }
        return list;
    }

    public void add_store(List<HashMap<String,String>> list){
        //将list列表的每行元素逐次插入数据表store中
        for (HashMap<String,String> item : list) {
            db.execSQL("INSERT INTO store (store_name, address) VALUES(?, ?)",
                    new Object[]{item.get("store_name"), item.get("address")});
        }
        db.close();
    }

    List<HashMap<String,String>> getStoreData(){
        List<HashMap<String,String>> list = new ArrayList<>();

        @SuppressLint("Recycle") Cursor cursor = db.query("store",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            @SuppressLint("Range") String store_name = cursor.getString(cursor.getColumnIndex("store_name"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("store_name", store_name);
            hashMap.put("address", address);
            list.add(hashMap);
        }
        return list;
    }

}

