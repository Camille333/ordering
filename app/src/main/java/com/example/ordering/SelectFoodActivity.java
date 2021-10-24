package com.example.ordering;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.List;

public class SelectFoodActivity extends AppCompatActivity implements Runnable{
    public static final String TAG = "SelectFoodActivity";
    private RecyclerView foodsListView;
    private RecyclerView selectedFoodsListView;
    private BottomSheetDialog bottomSheetDialog;
    private FloatingActionButton check;
    private TextView totalPrice;

    Handler handler;
    List<Food> foods;         //食物列表
    List<Food> selectedFoods; //已选中的食物
    BigDecimal total;         //所有选中食物对应的总金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);

        foodsListView = findViewById(R.id.foods);
        foodsListView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        foodsListView.setAdapter(new MyFoodsAdapter(foods));

        totalPrice = findViewById(R.id.totalPrice);

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    totalPrice.setText(String.valueOf(total)); //View.ininvalidate()
                    sendEmptyMessageDelayed(0, 1000);
                }
            }
        };

        //实现底部弹出所有选中食物的清单
        bottomSheetDialog = new BottomSheetDialog(SelectFoodActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        check = findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();//点击后显示该bottomSheetDialog
            }
        });

        //开启一个线程
        Thread thread = new Thread();
        thread.start();
    }

    @Override
    public void run() {
        //利用Jsoup从美团的网页上爬取商家列表以及每个商家对应的菜品列表

        //爬取之后再将element全部add到列表foods中


    }

    private class MyFoodsViewHolder extends RecyclerView.ViewHolder{

        private ImageView foodImage;
        private TextView foodName;
        private TextView foodInfo;
        private TextView foodPrice;
        private TextView foodCount;
        private ImageButton increase;
        private ImageButton decrease;

        public MyFoodsViewHolder(final View itemView){
            super(itemView);
            //获取行中显示各种数据的控件
            foodImage = itemView.findViewById(R.id.foodImage);
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
            Food food = items.get(position);
            holder.foodName.setText(food.getFood_name());
            byte[] image = food.getFood_image();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);


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
                        selectedFoodsListView.getAdapter().notifyDataSetChanged();
                        Log.i(TAG, "增加了：" + food.getFood_name());
                        System.out.println(selectedFoods);
                    }else {
                        //不新增，覆盖该位置的原数量值
                        selectedFoods.remove(food);
                        food.setFood_count(count + 1);
                        selectedFoods.add(food);
                        selectedFoodsListView.getAdapter().notifyItemChanged(position);
                        Log.i(TAG, "增加了：" + food.getFood_name());
                        System.out.println(selectedFoods);
                    }
                    total = BigDecimal.valueOf(Double.parseDouble(total.toString())).add(food.getFood_price());
                    refresh(total);
                }
                //更新总金额
                private void refresh(BigDecimal total) {
                    handler.sendEmptyMessageDelayed(0, 1000);
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
                        selectedFoodsListView.getAdapter().notifyDataSetChanged();
                        food.setFood_count(count - 1);
                        holder.foodCount.setText(String.valueOf(count - 1));

                        total = BigDecimal.valueOf(Double.parseDouble(total.toString())).subtract(food.getFood_price());
                        refresh(total);
                    }else if(count > 1){
                        Log.i(TAG, "减少了：" + food.getFood_name());
                        selectedFoods.remove(food);
                        food.setFood_count(count - 1);
                        selectedFoods.add(food);
                        selectedFoodsListView.getAdapter().notifyItemChanged(position);
                        System.out.println(selectedFoods);
                        holder.foodCount.setText(String.valueOf(count - 1));
                        total = BigDecimal.valueOf(Double.parseDouble(total.toString())).subtract(food.getFood_price());
                        refresh(total);
                    }
                }
                //更新总金额
                private void refresh(BigDecimal total) {
                    handler.sendEmptyMessageDelayed(0, 1000);
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}