package com.example.ordering;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectFoodActivity extends AppCompatActivity implements Runnable{
    public static final String TAG = "SelectFoodActivity";
    private RecyclerView foodsListView;
    private BottomSheetDialog bottomSheetDialog;
    private ImageButton back;
    private FloatingActionButton check;
    Handler handler;
    List<Food> foods = new ArrayList<>();         //食物列表
    List<Food> selectedFoods = new ArrayList<>(); //已选中的食物
    float total = 0.0f;         //所有选中食物对应的总金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);

        foodsListView = findViewById(R.id.foods);
        foodsListView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        foodsListView.setAdapter(new MyFoodsAdapter(foods));

        foods.add(0, new Food("香辣牛肉面", 15.0f, 0));
        foods.add(1, new Food("红烧牛肉面", 13.2f, 0));
        foods.add(2, new Food("牛杂粉丝", 14.5f, 0));
        foods.add(3, new Food("杂酱面", 12.0f, 0));
        foods.add(4, new Food("酸辣螺蛳粉", 16.5f, 0));
        Log.i(TAG, String.valueOf(foods.get(0)));

        //实现底部弹出所有选中食物的清单
        /*
        bottomSheetDialog = new BottomSheetDialog(SelectFoodActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        selectedFoodsListView = findViewById(R.id.selected_foods);
        //selectedFoodsListView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
 */

        check = findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bottomSheetDialog.show();//点击后显示该bottomSheetDialog
                //selectedFoodsListView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                //selectedFoodsListView.setAdapter(new MyFoodsAdapter(selectedFoods));

                Intent intent = new Intent(SelectFoodActivity.this, PaymentActivity.class);
                intent.putExtra("total", total);
                /*
                String[] name = new String[50];
                float[] price = new float[50];
                int[] count = new int[50];
                int i = 0;
                for (Food item : selectedFoods){
                    name[i] = item.getFood_name();
                    price[i] = item.getFood_price();
                    count[i] = item.getFood_count();
                    i = i + 1;
                }
                */
                //实现将list列表传送到另一个页面
                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) selectedFoods);
               /*
                intent.putExtra("selected_foodname", name);
                intent.putExtra("selected_foodprice", price);
                intent.putExtra("selected_foodcount", count);

                */
                //Log.i(TAG, "选择的菜品名字为：" + name);
                startActivity(intent);
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectFoodActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
/*
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.check){
            Intent intent = new Intent(SelectFoodActivity.this, PaymentActivity.class);
            intent.putExtra("total", total);
            for (Food item : selectedFoods){
                intent.putExtra("selected_foodname", item.getFood_name());
                intent.putExtra("selected_foodprice", item.getFood_price());
                intent.putExtra("selected_foodcount", item.getFood_count());
            }

            startActivity(intent);
        }
    }

 */

    @Override
    public void run() {
        try {
            Thread.sleep(3000);//进度条出现时间为3s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //利用Jsoup从美团的网页上爬取商家列表以及每个商家对应的菜品列表
        //爬取之后再将element全部add到列表foods中
        //向list列表里添加所有食物的名字以及对应的价格

        foods.add(0, new Food("香辣牛肉面", 15.0f, 0));
        foods.add(1, new Food("香辣牛肉面", 15.0f, 0));
        foods.add(2, new Food("香辣牛肉面", 15.0f, 0));
        foods.add(3, new Food("香辣牛肉面", 15.0f, 0));
        foods.add(4, new Food("香辣牛肉面", 15.0f, 0));

        Message message = handler.obtainMessage(0);
        handler.sendMessage(message);

    }

    private class MyFoodsViewHolder extends RecyclerView.ViewHolder{

        private TextView foodName;
        private TextView foodInfo;
        private TextView foodPrice;
        private TextView foodCount;
        private Button increase;
        private Button decrease;

        public MyFoodsViewHolder(final View itemView){
            super(itemView);
            //获取行中显示各种数据的控件
            foodName = itemView.findViewById(R.id.foodName);
            foodInfo = itemView.findViewById(R.id.foodInfo);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodCount = itemView.findViewById(R.id.foodCount);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
        }
    }

    private class MyFoodsAdapter extends RecyclerView.Adapter<MyFoodsViewHolder>{
        private List<Food> items;
        public MyFoodsAdapter(List<Food> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public MyFoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
            MyFoodsViewHolder viewHolder = new MyFoodsViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyFoodsViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            //获取要绑定数据的行的控件
            Food food = foods.get(position);
            holder.foodName.setText(food.getFood_name());
            holder.foodPrice.setText(String.valueOf(food.getFood_price()));
            holder.foodCount.setText(String.valueOf(food.getFood_count()));

            //点击加号，食物数量+1
            holder.increase.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(holder.foodCount.getText().toString());
                    holder.foodCount.setText(String.valueOf(count + 1));
                    Food food = foods.get(position);
                    if(count == 0){
                        //该行食物数量为1
                        food.setFood_count(1);
                        //增加一行已选择食物
                        selectedFoods.add(food);
                        //selectedFoods.add(food);
                        //foodsListView.getAdapter().notifyDataSetChanged();
                        Log.i(TAG, "增加了：" + food.getFood_name());
                        System.out.println(selectedFoods);
                    }else {
                        //不新增，覆盖该位置的原数量值
                        selectedFoods.remove(food);
                        food.setFood_count(count + 1);
                        selectedFoods.add(food);
                        //foodsListView.getAdapter().notifyItemChanged(position);
                        Log.i(TAG, "增加了：" + food.getFood_name());
                        //System.out.println(selectedFoods);
                    }
                    total = total + food.getFood_price();
                }

            });

            //点击减号，食物数量-1
            holder.decrease.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(holder.foodCount.getText().toString());
                    Food food = foods.get(position);

                    //不让0有可乘之机，别把重复的抽出放外面！
                    if(count == 1){
                        Log.i(TAG, "减少了：" + food.getFood_name());
                        selectedFoods.remove(food);
                        //foodsListView.getAdapter().notifyDataSetChanged();
                        food.setFood_count(count - 1);
                        holder.foodCount.setText(String.valueOf(count - 1));
                        total = total - food.getFood_price();
                    }else if(count > 1){
                        Log.i(TAG, "减少了：" + food.getFood_name());
                        selectedFoods.remove(food);
                        food.setFood_count(count - 1);
                        selectedFoods.add(food);
                        //foodsListView.getAdapter().notifyItemChanged(position);
                        //System.out.println(selectedFoods);
                        holder.foodCount.setText(String.valueOf(count - 1));
                        total = total - food.getFood_price();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

    }

}