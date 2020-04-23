package com.yanyiyun.baseutils.library.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 集合操作工具
 */
public class CollectionProcessTool {

    /**
     *返回一个定制类型的，不可修改的列表
     * 如果视图修改会抛出UnsupportedOperationException异常
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> immutableList(List<T> list){
        return Collections.unmodifiableList(new ArrayList<>(list));
    }

    /**
     * 返回包含elements的不可修改的列表
     * 如果视图修改会抛出UnsupportedOperationException异常
     * @param elements
     * @param <T>
     * @return
     */
    public static <T> List<T> immutableList(T... elements){
        return Collections.unmodifiableList(Arrays.asList(elements.clone()));
    }
}
