package com.xiaohui.bridge.model;

import com.xiaohui.bridge.business.bean.Bridge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
public class BridgesModel {

    private List<Bridge> bridges;

    public BridgesModel(List<Bridge> bridges) {
        this.bridges = bridges;
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public Bridge getBridge(int pos) {
        return getBridges().get(pos);
    }

}
