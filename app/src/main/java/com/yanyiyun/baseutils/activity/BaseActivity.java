package com.yanyiyun.baseutils.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
        ListView view;
    }
    public static void ShowToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void startIntent(Context context, Class<?> c) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        context.startActivity(intent);
    }

    public static void startIntentPost(Context context, Class<?> c, Bundle b) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        intent.putExtras(b);
        context.startActivity(intent);
    }
}
