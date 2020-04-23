package com.yanyiyun.baseutils.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;

public class CreditCardActivity extends BaseActivity implements View.OnClickListener{

    private TextView head_text_title,money_tv,front_money_tv;
    private EditText number_et;

    private Context mContext;
    private String temp="money";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card_activity);
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("信用卡");

        number_et=findViewById(R.id.number_et);
        front_money_tv=findViewById(R.id.front_money_tv);

        money_tv=findViewById(R.id.money_tv);
        SharedPreferences sp=getSharedPreferences(temp,0);
        money_tv.setText(""+sp.getFloat(temp,0.0f));
        findViewById(R.id.add_b).setOnClickListener(this);
        findViewById(R.id.clear_b).setOnClickListener(this);
    }

    private void addMoney(float addMoney){
        SharedPreferences sp=getSharedPreferences(temp,0);
        float money=Float.parseFloat(money_tv.getText().toString());
        money=money+addMoney;
        money_tv.setText(""+money);
        sp.edit().putFloat(temp,money).commit();
        number_et.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
                break;
                //
            case R.id.add_b:
                String money_s=number_et.getText().toString();
                if(money_s!=null&&!money_s.isEmpty()){
                    addMoney(Float.parseFloat(money_s));
                    front_money_tv.setText(money_s);
                }
                break;
                //
            case R.id.clear_b:
                SharedPreferences sp=getSharedPreferences(temp,0);
                sp.edit().putFloat(temp,0).commit();
                money_tv.setText("0.00");
                number_et.setText("");
                break;
        }
    }

}
