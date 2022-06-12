package com.zmj.demo.common;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class CollectionUtil {
    /**
     * 集合按长度分组
     *
     * @param list 集合
     * @param size 分割大小，100则为每100条数据为一组
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> partition(final List<T> list, final int size) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");


        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        List<List<T>> result = new ArrayList<>();
        Iterator<T> it = list.iterator();
        List<T> subList = null;
        while (it.hasNext()) {
            if (subList == null) {
                subList = new ArrayList<>();
            }
            T t = it.next();
            subList.add(t);
            if (subList.size() == size) {
                result.add(subList);
                subList = null;
            }
        }
        //补充最后一页
        if (subList != null) {
            result.add(subList);
        }
        return result;

    }
}
