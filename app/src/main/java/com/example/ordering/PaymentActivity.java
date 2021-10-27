package com.example.ordering;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    public static final String TAG = "PaymentActivity";
    private RecyclerView selectedFoodsListView;
    private TextView storeName;
    private TextView totalPrice;
    private Button pay;
    private float total;
    List<Food> selectedFoods = new ArrayList<>(); //已选中的食物

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        selectedFoodsListView = findViewById(R.id.selected_foods);
        totalPrice = findViewById(R.id.totalPrice);
        storeName = findViewById(R.id.storeName);

        //获取订单数据
        Intent intent = getIntent();
        storeName.setText(intent.getStringExtra("storeName"));
        total = intent.getFloatExtra("total",0.0f);
        totalPrice.setText("总金额为：" + String.format("%.2f", total) + "元");

        selectedFoods = getIntent().<Food>getParcelableArrayListExtra("list");
        Log.i(TAG, selectedFoods.get(0).getFood_name());
        Log.i(TAG, String.valueOf(selectedFoods.get(0).getFood_price()) + "元");
        Log.i(TAG, String.valueOf(selectedFoods.get(0).getFood_count()));

        selectedFoodsListView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        selectedFoodsListView.setAdapter(new MyFoodsAdapter(selectedFoods));

        //点击pay按钮确认支付后，弹出提示信息
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                builder.setTitle("提示").setMessage("您是否还想要再下一单？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "对话框事件处理");
                        Intent intent = new Intent(PaymentActivity.this, StoreActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "对话框事件处理");
                        Intent intent = new Intent(PaymentActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.create().show();

                /*
                Intent intent = new Intent(PaymentActivity.this, FinishActivity.class);
                startActivity(intent);
                finish();

                 */
            }
        });
    }

    private class MyFoodsViewHolder extends RecyclerView.ViewHolder{
        private TextView order_fName;
        private TextView order_fPrice;
        private TextView order_fCount;

        public MyFoodsViewHolder(final View itemView){
            super(itemView);
            //获取行中显示各种数据的控件
            order_fName = itemView.findViewById(R.id.order_fName);
            order_fPrice = itemView.findViewById(R.id.order_fPrice);
            order_fCount = itemView.findViewById(R.id.order_fCount);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
            PaymentActivity.MyFoodsViewHolder viewHolder = new PaymentActivity.MyFoodsViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final PaymentActivity.MyFoodsViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            //获取要绑定数据的行的控件
            Food food = selectedFoods.get(position);
            holder.order_fName.setText(food.getFood_name());
            holder.order_fPrice.setText(String.valueOf(food.getFood_price()) + " ¥");
            holder.order_fCount.setText(String.valueOf(food.getFood_count()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

    }

}