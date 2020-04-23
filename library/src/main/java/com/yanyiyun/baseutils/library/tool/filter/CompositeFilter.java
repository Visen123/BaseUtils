package com.yanyiyun.baseutils.library.tool.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 混合文件过滤器
 */
public class CompositeFilter implements FileFilter,Serializable {

    private ArrayList<FileFilter> mFilters;

    public CompositeFilter(ArrayList<FileFilter> mFilters) {
        this.mFilters = mFilters;
    }

    @Override
    public boolean accept(File pathname) {
        for(FileFilter filter:mFilters){
            if(!filter.accept(pathname)){
                return false;
            }
        }
        return true;
    }
}
