package com.xiaohui.bridge.util;

import java.util.List;

/**
 * Created by xiaohui on 14-11-5.
 */
public class ListUtil {

    public static int sizeOfList(List<?> list) {
        return list == null ? 0 : list.size();
    }

    public static boolean isEmpty(List<?> list) {
        return sizeOfList(list) == 0;
    }
}
