package com.xiaohui.bridge.model;

import com.xiaohui.bridge.business.bean.Bridge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
public class BridgesModel {

    private List<Bridge> bridges;

    public List<Bridge> getBridges() {
        if (bridges == null) {
            bridges = new ArrayList<Bridge>();
            bridges.add(new Bridge("桥梁一"));
            bridges.add(new Bridge("桥梁二"));
            bridges.add(new Bridge("桥梁三"));
        }
        return bridges;
    }

    public Bridge getBridge(int pos) {
        return getBridges().get(pos);
    }

}
