package com.yanyiyun.tool.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.regex.Pattern;

public class PatternFilter implements FileFilter,Serializable {
    private Pattern mPattern;
    /**
     * 是否是目录文件过滤器
     */
    private boolean mDirectoriesFilter;

    public PatternFilter(Pattern mPattern, boolean mDirectoriesFilter) {
        this.mPattern = mPattern;
        this.mDirectoriesFilter = mDirectoriesFilter;
    }

    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory()&&!mDirectoriesFilter||mPattern.matcher(pathname.getName()).matches();
    }
}
