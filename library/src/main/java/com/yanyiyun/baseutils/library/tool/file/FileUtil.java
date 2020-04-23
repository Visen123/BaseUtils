package com.yanyiyun.baseutils.library.tool.file;

import com.yanyiyun.baseutils.library.tool.comparator.FileComparator;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtil {

    private static FileUtil instance;

    private FileUtil( ) {
    }

    public static FileUtil getInstance(){
        if(instance==null){
            synchronized (FileUtil.class){
                if(instance==null){
                    instance=new FileUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 获得某路径下的所有文件
     * @param path 文件夹路径
     * @param filter 文件管理器 可为null
     * @return
     */
    public List<File> getFieListByDirPath(String path, FileFilter filter){
        File directory=new File(path);
        File[] files;
        if(filter!=null){
            files=directory.listFiles(filter);
        }else {
            files=directory.listFiles();
        }
        if(files==null){
            return new ArrayList<>();
        }

        List<File> result= Arrays.asList(files);
        Collections.sort(result,new FileComparator());
        return result;
    }

    /**
     * 获取当前文件夹的上层目录
     * @param path
     * @return
     */
    public String cutLastSegmentOfPath(String path){
        if(path.length()-path.replace("/","").length()<=1){
            return "/";
        }
        String newPath=path.substring(0,path.lastIndexOf("/"));
        if(newPath.equals("/storage/emulated")){
            newPath="/storage";
        }
        return newPath;
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
