package com.yanyiyun.baseutils.library.tool.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
 * 判断文件是否隐藏
 */
public class HiddenFilter implements FileFilter,Serializable {
    @Override
    public boolean accept(File pathname) {
        return !pathname.isHidden();
    }
}
