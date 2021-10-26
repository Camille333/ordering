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
    /**
     * 声明一个AndroidSDK自带的数据库变量db
     */
    private SQLiteDatabase db;

    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * 不过，以现如今的内存容量，估计一辈子也见不到几次内存占满的状态
     * db = getReadableDatabase();
     */
    DBOpenHelper(Context context){
        super(context,"db_test",null,1);
        db = getReadableDatabase();
        db = getWritableDatabase();
    }

    /**
     * 重写两个必须要重写的方法，因为class DBOpenHelper extends SQLiteOpenHelper
     * 而这两个方法是 abstract 类 SQLiteOpenHelper 中声明的 abstract 方法
     * 所以必须在子类 DBOpenHelper 中重写 abstract 方法
     * 想想也是，为啥规定这么死必须重写？
     * 因为，一个数据库表，首先是要被创建的，然后免不了是要进行增删改操作的
     * 所以就有onCreate()、onUpgrade()两个方法
     *
     */

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
    /**
     * 接下来写自定义的增删改查方法
     * 这些方法，写在这里归写在这里，以后不一定都用
     * add()
     * delete()
     * update()
     * getAllData()
     */

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



    void add(String name, String password,String email,String phonenum){
        db.execSQL("INSERT INTO user (name,password,email,phonenum) VALUES(?,?,?,?)",new Object[]{name,password,email,phonenum});
    }

    public void delete(String name,String password){
        db.execSQL("DELETE FROM user WHERE name = AND password ="+name+password);
    }
    public void updata(String password){
        db.execSQL("UPDATE user SET password = ?",new Object[]{password});
    }

    /**
     * 前三个没啥说的，都是一套的看懂一个其他的都能懂了
     * 下面重点说一下查询表user全部内容的方法
     * 我们查询出来的内容，需要有个容器存放，以供使用，
     * 所以定义了一个ArrayList类的list
     * 有了容器，接下来就该从表中查询数据了，
     * 这里使用游标Cursor，这就是数据库的功底了，
     * 在Android中我就不细说了，因为我数据库功底也不是很厚，
     * 但我知道，如果需要用Cursor的话，第一个参数："表名"，中间5个：null，
     *                                                     最后是查询出来内容的排序方式："name DESC"
     * 游标定义好了，接下来写一个while循环，让游标从表头游到表尾
     * 在游的过程中把游出来的数据存放到list容器中
     *
     */
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
/*
    //读取数据
    Cursor c = db.rawQuery("SELECT* FROM food", new String[]{"33"});
        while (c.moveToNext()) {
        int _id = c.getInt(c.getColumnIndex("_id"));
        String name = c.getString(c.getColumnIndex("name"));
        int age = c.getInt(c.getColumnIndex("age"));
        Log.i("db", "_id=>" + _id + ", name=>" + name + ", age=>" + age);
    }

 */

}

