package com.xiaohui.bridge.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
public class BridgeModel {

    private List<String> bridges = new ArrayList<String>(Arrays.asList("桥梁一", "桥梁二", "桥梁三"));

    public List<String> getBridges() {
        return bridges;
    }

    public String getBridge(int pos) {
        return bridges.get(pos);
    }

}
