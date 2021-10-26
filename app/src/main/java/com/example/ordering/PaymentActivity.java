package com.example.ordering;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    public static final String TAG = "PaymentActivity";
    private RecyclerView selectedFoodsListView;
    private TextView totalPrice;
    private Button pay;
    private float total;
    private String foodname;
    private float foodprice;
    private int foodcount;
    String[] name = new String[50];
    float[] price = new float[50];
    int[] count = new int[50];
    List<Food> selectedFoods = new ArrayList<>(); //已选中的食物
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        selectedFoodsListView = findViewById(R.id.selected_foods);
        totalPrice = findViewById(R.id.totalPrice);
        pay = findViewById(R.id.pay);
        //获取订单数据
        Intent intent = getIntent();
        total = intent.getFloatExtra("total",0.0f);
        totalPrice.setText("总金额为：" + String.format("%.2f", total) + "元");

        selectedFoods = getIntent().<Food>getParcelableArrayListExtra("list");
        Log.i(TAG, selectedFoods.get(0).getFood_name());
        Log.i(TAG, String.valueOf(selectedFoods.get(0).getFood_price()) + "元");
        Log.i(TAG, String.valueOf(selectedFoods.get(0).getFood_count()));

        /*
        name = intent.getStringArrayExtra("selected_foodname");
        price = intent.getFloatArrayExtra("selected_foodprice");
        count = intent.getIntArrayExtra("selected_foodcount");
         */

        selectedFoodsListView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        selectedFoodsListView.setAdapter(new MyFoodsAdapter(selectedFoods));

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

    private class MyFoodsAdapter extends RecyclerView.Adapter<PaymentActivity.MyFoodsViewHolder>{
        private List<Food> items;
        public MyFoodsAdapter(List<Food> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public PaymentActivity.MyFoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
            PaymentActivity.MyFoodsViewHolder viewHolder = new PaymentActivity.MyFoodsViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final PaymentActivity.MyFoodsViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            //获取要绑定数据的行的控件
            Food food = selectedFoods.get(position);
            holder.foodName.setText(food.getFood_name());
            holder.foodPrice.setText(String.valueOf(food.getFood_price()));
            holder.foodCount.setText(String.valueOf(food.getFood_count()));

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

    }
}