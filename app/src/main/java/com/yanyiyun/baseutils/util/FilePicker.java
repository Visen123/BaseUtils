package com.yanyiyun.baseutils.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.yanyiyun.baseutils.activity.FilePickerActivity;
import com.yanyiyun.tool.filter.CompositeFilter;
import com.yanyiyun.tool.filter.HiddenFilter;
import com.yanyiyun.tool.filter.PatternFilter;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FilePicker {

    private Activity activity;
    private Fragment fragment;
    private android.support.v4.app.Fragment supporFragment;

    private Class<? extends FilePickerActivity> mFilePickerClass = FilePickerActivity.class;

    /**
     * 是否添加目录文件过滤器
     */
    private Boolean mDirectoriesFilter = false;
    /**
     * 根路径
     */
    private String mRootPath;
    /**
     * 当前路径
     */
    private String mCurrentPath;
    /**
     * 是否显示隐藏文件
     */
    private Boolean mShowHidden = false;
    /**
     * 页面标题
     */
    private CharSequence mTitle;
    /**
     * 跳转activity的requestcode
     */
    private Integer mRequestCode;
    /**
     * 文件过滤器
     */
    private Pattern mFileFilter;

    public FilePicker(){}

    public FilePicker withActivity(Activity activity){
        if(fragment!=null&&supporFragment!=null){
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }
        this.activity=activity;
        return this;
    }

    public FilePicker withFragment(Fragment fragment){
        if(activity!=null&&supporFragment!=null){
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }
        this.fragment=fragment;
        return this;
    }

    public FilePicker withSupporFragment(android.support.v4.app.Fragment fragment){
        if(activity!=null||fragment!=null){
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }
        supporFragment=fragment;
        return this;
    }

    public FilePicker withRequestCode(int requestCode){
        mRequestCode=requestCode;
        return this;
    }

    public FilePicker withFilter(Pattern pattern){
        mFileFilter=pattern;
        return this;
    }

    public FilePicker withFilterDirectories(boolean directoriesFilter) {
        mDirectoriesFilter = directoriesFilter;
        return this;
    }

    public FilePicker withRootPath(String rootPath) {
        mRootPath = rootPath;
        return this;
    }

    public FilePicker withPath(String path) {
        mCurrentPath = path;
        return this;
    }

    /**
     * 是否显示隐藏文件
     */
    public FilePicker withHiddenFiles(boolean show) {
        mShowHidden = show;
        return this;
    }

    public FilePicker withTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    public void start(){
        if (activity == null && fragment == null && supporFragment == null) {
            throw new RuntimeException("You must pass Activity/Fragment by calling withActivity/withFragment/withSupportFragment method");
        }

        if (mRequestCode == null) {
            throw new RuntimeException("You must pass request code by calling withRequestCode method");
        }

        Intent intent=getIntent();
        if(activity!=null){
            activity.startActivityForResult(intent,mRequestCode);
        }else if(fragment!=null){
            fragment.startActivityForResult(intent,mRequestCode);
        }else if(supporFragment!=null){
            supporFragment.startActivityForResult(intent,mRequestCode);
        }

    }

    private Intent getIntent(){
        CompositeFilter filter=getFilter();

        Activity ac=null;
        if(activity!=null){
            ac=activity;
        }else if(fragment!=null){
            ac=fragment.getActivity();
        }else if(supporFragment!=null){
            ac=supporFragment.getActivity();
        }

        Intent intent=new Intent(ac,mFilePickerClass);
        intent.putExtra(FilePickerActivity.ARG_FILTER, filter);

        if (mRootPath != null) {
            intent.putExtra(FilePickerActivity.ARG_START_PATH, mRootPath);
        }

        if (mCurrentPath != null) {
            intent.putExtra(FilePickerActivity.ARG_CURRENT_PATH, mCurrentPath);
        }

        if (mTitle != null) {
            intent.putExtra(FilePickerActivity.ARG_TITLE, mTitle);
        }
        return intent;
    }

    private CompositeFilter getFilter(){
        ArrayList<FileFilter> filters=new ArrayList<>();

        if(!mShowHidden){  //添加隐藏文件过滤器
            filters.add(new HiddenFilter());
        }

        if(mFileFilter!=null){
            filters.add(new PatternFilter(mFileFilter,mDirectoriesFilter));
        }

        return new CompositeFilter(filters);
    }
}
