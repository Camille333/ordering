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

public class SelectFoodActivity extends AppCompatActivity {
    public static final String TAG = "SelectFoodActivity";
    private RecyclerView foodsListView;
    private TextView storeName;
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
        storeName = findViewById(R.id.storeName);
        foodsListView = findViewById(R.id.foods);
        foodsListView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        foodsListView.setAdapter(new MyFoodsAdapter(foods));

        //获取所选商家名字数据
        Intent intent = getIntent();
        storeName.setText(intent.getStringExtra("store_name"));

        //向食物列表foods里添加所有食物的名字、价格以及对应的数量
        String s_flag = intent.getStringExtra("store_name");
        switch (s_flag) {
            case "美好面馆":
                foods.add(0, new Food("香辣牛肉面", 15.0f, 0));
                foods.add(1, new Food("红烧牛肉面", 13.2f, 0));
                foods.add(2, new Food("牛杂粉丝", 14.5f, 0));
                foods.add(3, new Food("杂酱面", 12.0f, 0));
                foods.add(4, new Food("酸辣螺蛳粉", 16.5f, 0));
                break;
            case "书亦烧仙草":
                foods.add(0, new Food("杨枝甘露烧仙草", 15.0f, 0));
                foods.add(1, new Food("焦糖珍珠奶茶", 11.5f, 0));
                foods.add(2, new Food("红豆燕麦奶茶", 12.0f, 0));
                foods.add(3, new Food("芒椰芋泥绵绵", 14.5f, 0));
                foods.add(4, new Food("芝士多肉葡萄", 16.3f, 0));
                foods.add(5, new Food("桂花酒酿草莓", 13.4f, 0));
                break;
            case "三米粥铺":
                foods.add(0, new Food("皮蛋瘦肉粥", 7.9f, 0));
                foods.add(1, new Food("红枣山药粥", 9.9f, 0));
                foods.add(2, new Food("腊八粥", 11.8f, 0));
                foods.add(3, new Food("猪肉酸菜粉丝包", 4.5f, 0));
                foods.add(4, new Food("红糖锅盔", 3.0f, 0));
                foods.add(5, new Food("炼乳馒头", 6.3f, 0));
                break;
            case "叫了只炸鸡":
                foods.add(0, new Food("韩式无骨炸鸡", 28.58f, 0));
                foods.add(1, new Food("豪华辣翅套餐", 34.68f, 0));
                foods.add(2, new Food("奥尔良烤翅", 8.88f, 0));
                foods.add(3, new Food("蟹角棒", 4.2f, 0));
                foods.add(4, new Food("爆浆鸡腿卷", 6.85f, 0));
                foods.add(5, new Food("千丝万缕虾", 5.68f, 0));
                break;
            case "五谷渔粉":
                foods.add(0, new Food("五谷肥牛粉", 14.0f, 0));
                foods.add(1, new Food("五谷酥肉粉", 13.0f, 0));
                foods.add(2, new Food("酸辣渔粉", 14.5f, 0));
                foods.add(3, new Food("经典酸菜渔粉", 12.0f, 0));
                foods.add(4, new Food("五谷鱼头渔粉", 13.8f, 0));
                foods.add(5, new Food("五谷茄汁渔粉", 12.9f, 0));
                break;
            case "一品麻辣香锅":
                foods.add(0, new Food("干锅鸡+虾饺套餐", 15.0f, 0));
                foods.add(1, new Food("干锅兔套餐", 13.2f, 0));
                foods.add(2, new Food("干锅牛蛙套餐", 14.5f, 0));
                foods.add(3, new Food("干锅虾套餐", 12.0f, 0));
                foods.add(4, new Food("素菜双拼套餐", 16.5f, 0));
                break;
        }

        check = findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectFoodActivity.this, PaymentActivity.class);
                intent.putExtra("total", total);
                intent.putExtra("storeName", s_flag);

                //实现将list列表传送到另一个页面
                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) selectedFoods);

                startActivity(intent);
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectFoodActivity.this, StoreActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private class MyFoodsViewHolder extends RecyclerView.ViewHolder{

        private TextView foodName;
        private TextView foodPrice;
        private TextView foodCount;
        private Button increase;
        private Button decrease;

        public MyFoodsViewHolder(final View itemView){
            super(itemView);
            //获取行中显示各种数据的控件
            foodName = itemView.findViewById(R.id.foodName);
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
                        //增加一行已选食物
                        selectedFoods.add(food);
                        Log.i(TAG, "增加了：" + food.getFood_name());
                    }else {
                        //不新增，覆盖该位置的原数量值
                        selectedFoods.remove(food);
                        food.setFood_count(count + 1);
                        selectedFoods.add(food);
                        Log.i(TAG, "增加了：" + food.getFood_name());
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
                    if(count == 1){
                        Log.i(TAG, "减少了：" + food.getFood_name());
                        selectedFoods.remove(food);
                        food.setFood_count(count - 1);
                        holder.foodCount.setText(String.valueOf(count - 1));
                        total = total - food.getFood_price();
                    }else if(count > 1){
                        Log.i(TAG, "减少了：" + food.getFood_name());
                        selectedFoods.remove(food);
                        food.setFood_count(count - 1);
                        selectedFoods.add(food);
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