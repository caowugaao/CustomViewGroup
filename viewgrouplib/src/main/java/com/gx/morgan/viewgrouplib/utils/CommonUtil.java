package com.gx.morgan.viewgrouplib.utils;

import java.util.List;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/5/5 16:14
 */
public class CommonUtil {
    private CommonUtil() {
    }

    public static <T> void checkEmpty(List<T> datas, String targetDesc) {
        checkNull(datas, targetDesc);
        if (datas.isEmpty()) {
            throw new NullPointerException(targetDesc + " cannor be empty. this size is 0");
        }
    }

    public static void checkNull(Object obj, String targetDesc) {
        if (null == obj) {
            throw new NullPointerException(targetDesc + " cannor be NULL");
        }
    }
}
