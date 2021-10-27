package com.example.ordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Info_StoreActivity extends AppCompatActivity {
    public static final String TAG = "Info_StoreActivity";
    private TextView info_name;
    private TextView info_addr;
    private TextView info;
    private ImageView info_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_store);

        info_name = findViewById(R.id.info_name);
        info_addr = findViewById(R.id.info_addr);
        info = findViewById(R.id.info);
        info_back = findViewById(R.id.info_back);

        //获取商家数据
        Intent intent = getIntent();
        info_name.setText("欢迎来到-" + intent.getStringExtra("store_name"));
        info_addr.setText("地址：" + intent.getStringExtra("address"));

        //为各个商家添加对应的商家信息描述
        String s = "商家描述：";
        String s_flag = intent.getStringExtra("store_name");
        switch (s_flag) {
            case "美好面馆":
                info.setText(s + "柳浪湾小吃快餐人气榜第2名——本店供应各类面食小吃，不仅价格实惠、分量足，味道更是一级棒！欢迎大家前来本店品尝！");
                break;
            case "书亦烧仙草":
                info.setText(s + "温江大学城奶茶热销榜第4名（点评高分店铺）——我们选用的水果都由当日供应，以确保做出的每份饮品口感绝佳！");
                break;
            case "三米粥铺":
                info.setText(s + "温江区粥店回头率榜第6名——本店均用新鲜的真材实料制作餐点，保证口感的同时，还会每周提供花式菜品给顾客尝鲜！");
                break;
            case "叫了只炸鸡":
                info.setText(s + "温江大学城炸物小吃热销榜第4名——选用上好的食用油，保障每位食客的身体健康，当然味道也超级美味哟，快来尝尝吧！");
                break;
            case "五谷渔粉":
                info.setText(s + "财大校内店——别看本店规模不大，我们可以为广大的顾客提供类别丰富的渔粉，只要你能想到的口味，我们都有！！！");
                break;
            case "一品麻辣香锅":
                info.setText(s + "温江区中式菜肴热销榜第3名——我们提供家常版现炒麻辣香锅，无论是个人还是家庭聚餐都合适，为顾客带来满意的服务就是本店的宗旨！");
                break;
        }

        //点击info_back按钮回退到商家列表界面
        info_back = findViewById(R.id.info_back);
        info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Info_StoreActivity.this, StoreActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}