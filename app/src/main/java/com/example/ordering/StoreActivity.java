package com.example.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static final String TAG = "StoreActivity";
    private DBOpenHelper mDBOpenHelper;
    private ImageView back;
    List<HashMap<String,String>> store = new ArrayList<>();
    ListView listView;
    MyAdapter adapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        listView = findViewById(R.id.listview);//列表

        //向商家列表store里添加所有商家的名字以及对应的地址
        String[] s_name = {"美好面馆", "书亦烧仙草", "三米粥铺", "叫了只炸鸡", "五谷渔粉", "一品麻辣香锅"};
        String[] s_addr = {"温江区柳浪湾7号路", "温江大学城北一街211号", "柳城街道柳南四路118号", "南熏大道三段155号", "柳台大道555号", "柳城和平社区10号"};
        int i;
        for(i = 0; i < s_name.length; i++){
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("store_name", s_name[i]);
            hashMap.put("address", s_addr[i]);
            store.add(hashMap);
        }
        Log.i(TAG, store.get(0).get("store_name"));
        //将store列表存放进数据库中
        //mDBOpenHelper = new DBOpenHelper(this);
        //mDBOpenHelper.onCreate(db);
        //mDBOpenHelper.add_store(store);
        //从数据库中取出store列表
        //store = mDBOpenHelper.getStoreData();

        adapter = new MyAdapter(StoreActivity.this, R.layout.item_listview, store);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(StoreActivity.this);
        listView.setOnItemLongClickListener(StoreActivity.this);

        back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //点击商家列表中的项跳转到对应食物下单页面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = listView.getItemAtPosition(position);
        HashMap<String,String> hashMap = (HashMap<String, String>)item;
        String store_name = hashMap.get("store_name");
        String address = hashMap.get("address");
        Intent intent = new Intent(StoreActivity.this, SelectFoodActivity.class);
        intent.putExtra("store_name", store_name);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    @Override
    //长按时实现删除数据项
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i(TAG, "长按操作：第" + position + "项");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "对话框事件处理");
                adapter.remove(position);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否", null);
        builder.create().show();
        Log.i(TAG, "List size=" + store.size());
        return true;//true:当长按时，不执行点击操作
    }
}
