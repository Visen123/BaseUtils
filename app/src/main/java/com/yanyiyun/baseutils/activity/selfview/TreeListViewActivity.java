package com.yanyiyun.baseutils.activity.selfview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.baseutils.adapter.SimpleTreeListViewAdapter;
import com.yanyiyun.baseutils.entity.OrgBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形列表
 */
public class TreeListViewActivity extends BaseActivity implements View.OnClickListener {

    private TextView head_text_title;
    private ListView list_lv;

    private Context mContext;
    private SimpleTreeListViewAdapter<OrgBean> mAdapter;
    private List<OrgBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        initDatas();
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("TreeListView");

        list_lv=findViewById(R.id.list_lv);
        try {
            mAdapter=new SimpleTreeListViewAdapter<OrgBean>(list_lv,mContext,data,0);
            list_lv.setAdapter(mAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initDatas() {
        data = new ArrayList<OrgBean>();
        OrgBean bean2 = new OrgBean(1, 0, "根目录1");
        data.add(bean2);
        bean2 = new OrgBean(2, 0, "根目录2");
        data.add(bean2);
        bean2 = new OrgBean(3, 0, "根目录3");
        data.add(bean2);
        bean2 = new OrgBean(4, 1, "根目录1-1");
        data.add(bean2);
        bean2 = new OrgBean(5, 1, "根目录1-2");
        data.add(bean2);
        bean2 = new OrgBean(6, 5, "根目录1-2-1");
        data.add(bean2);
        bean2 = new OrgBean(7, 3, "根目录3-1");
        data.add(bean2);
        bean2 = new OrgBean(8, 3, "根目录3-2");
        data.add(bean2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
                break;
        }
    }
}
