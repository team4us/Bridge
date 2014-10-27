package com.xiaohui.bridge.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xhChen on 14/9/27.
 */
public class Cookie {

    private Map<String, Object> map = new HashMap<String, Object>();

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public void clear() {
        map.clear();
    }

    public void remove(String key) {
        map.remove(key);
    }

}
