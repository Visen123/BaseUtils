package com.yanyiyun.tool.comparator;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {
    /**
     * 文件是路径小于文件
     * 如果都是文件或者都是文件路径则比较名称
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(File o1, File o2) {
        if(o1==o2){
            return 0;
        }
        if(o1.isDirectory()&&o2.isFile()){
            return -1;
        }
        if(o1.isFile()&&o2.isDirectory()){
            return 1;
        }
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
