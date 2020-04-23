package com.yanyiyun.baseutils.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.fragment.FilePickerFragment;
import com.yanyiyun.tool.file.FileUtil;
import com.yanyiyun.tool.filter.CompositeFilter;
import com.yanyiyun.tool.filter.PatternFilter;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FilePickerActivity extends BaseActivity implements View.OnClickListener,FilePickerFragment.FileClickListener{

    public static final String ARG_START_PATH = "arg_start_path";
    public static final String ARG_CURRENT_PATH = "arg_current_path";

    public static final String ARG_FILTER = "arg_filter";
    public static final String ARG_CLOSEABLE = "arg_closeable";
    public static final String ARG_TITLE = "arg_title";

    public static final String STATE_START_PATH = "state_start_path";
    private static final String STATE_CURRENT_PATH = "state_current_path";

    public static final String RESULT_FILE_PATH = "result_file_path";

    private TextView head_text_title;

    private Context mContext;
    private String START_PATH= Environment.getExternalStorageDirectory().getAbsolutePath();
    private String CURRENT_PATH=START_PATH;
    private CharSequence title;
    private static final int HANDLE_CLICK_DELAY = 150;
    private CompositeFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_picker_activity);
        mContext=this;
        initdata();
        initview();
    }

    private void initdata() {
        //获得过滤器
        if(getIntent().hasExtra(ARG_FILTER)){
            Serializable filter=getIntent().getSerializableExtra(ARG_FILTER);

            if(filter instanceof Pattern){
                ArrayList<FileFilter> filters=new ArrayList<>();
                filters.add(new PatternFilter((Pattern) filter,false));
                mFilter=new CompositeFilter(filters);
            }else {
                mFilter= (CompositeFilter) filter;
            }
        }

        if(getIntent().hasExtra(ARG_CURRENT_PATH)){
            START_PATH=getIntent().getStringExtra(ARG_START_PATH);
            CURRENT_PATH=START_PATH;
        }

        if(getIntent().hasExtra(ARG_CURRENT_PATH)){
            String currentPath=getIntent().getStringExtra(ARG_CURRENT_PATH);
            if(currentPath.startsWith(START_PATH)){
                CURRENT_PATH=currentPath;
            }
        }

        if (getIntent().hasExtra(ARG_TITLE)) {
            title = getIntent().getCharSequenceExtra(ARG_TITLE);
        }
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);

        AndPermission.with(this)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .requestCode(100)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if(requestCode==100){
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_fl, FilePickerFragment.getInstance(CURRENT_PATH))
                                    .addToBackStack(null)
                                    .commit();
                            setTitle(title);
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        if(requestCode==100){
                            AndPermission.defaultSettingDialog(FilePickerActivity.this,400).show();
                        }
                    }
                })
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(mContext,rationale).show();
                    }
                })
                .start();
    }

    @Override
    public void onFileClicked(final File clickedFile) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleFileClicked(clickedFile);
            }
        },HANDLE_CLICK_DELAY);
    }

    private void handleFileClicked(File clickedFile){
        if(clickedFile.isDirectory()){
            CURRENT_PATH=clickedFile.getPath();
            if(CURRENT_PATH.equals("/storage/emulated")){
                CURRENT_PATH=Environment.getExternalStorageDirectory().getAbsolutePath();
            }
            addFragmentToBackStack(CURRENT_PATH);
            setTitle(CURRENT_PATH);
        }else {
            ShowToast(mContext,"您选中了文件"+clickedFile.getName());
        }
    }

    private void addFragmentToBackStack(String path){
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content_fl,FilePickerFragment.getInstance(path))
                .addToBackStack(null)
                .commit();
    }

    private void setTitle(String t){
        if(!TextUtils.isEmpty(t)){
            head_text_title.setText(t);
        }
    }

    @Override
    public void onBackPressed() {
        if(!CURRENT_PATH.equals(START_PATH)){
            getSupportFragmentManager().popBackStack();
            CURRENT_PATH= FileUtil.getInstance().cutLastSegmentOfPath(CURRENT_PATH);
           setTitle(CURRENT_PATH);
        }else {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_img_left:
                onBackPressed();
                break;
        }
    }
}
