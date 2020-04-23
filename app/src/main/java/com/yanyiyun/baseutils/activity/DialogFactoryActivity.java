package com.yanyiyun.baseutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.function.dialog.factory.DialogFactory;
import com.yanyiyun.function.dialog.product.ConfirmAndCancleDialog;
import com.yanyiyun.function.dialog.product.LoadDialog;
import com.yanyiyun.function.dialog.product.TextCACD;

/**
 * 对话框工厂
 */
public class DialogFactoryActivity extends BaseActivity implements View.OnClickListener {
    private TextView head_text_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_factory_acitivity);
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title = findViewById(R.id.head_text_title);
        head_text_title.setText("DialogFactory");

        findViewById(R.id.show_b).setOnClickListener(this);
    }

    private void textCACDDialog() {
        final TextCACD textCACD = (TextCACD) DialogFactory.getInstance().
                createDialog(this, DialogFactory.DialogType.TEXT_CACD);
        textCACD.title_tv.setText("提示");
        textCACD.content_tv.setText("我们的什么");
        textCACD.setOnCancleAndConfirmListener(new ConfirmAndCancleDialog.OnCancleAndConfirmListener() {
            @Override
            public void cancle() {
                textCACD.dismiss();
            }

            @Override
            public void confirm() {
                textCACD.dismiss();
            }
        });
        textCACD.show();
    }

    private void loadDialog(){
        LoadDialog dialog= (LoadDialog) DialogFactory.getInstance().
                createDialog(this,DialogFactory.DialogType.LOAD_DIALOG);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_img_left:
                finish();
                break;
            //显示对话框
            case R.id.show_b:
                loadDialog();
                break;
        }
    }
}
