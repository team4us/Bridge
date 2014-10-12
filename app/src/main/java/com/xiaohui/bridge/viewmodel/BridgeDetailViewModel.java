package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.Bridge;

import org.robobinding.aspects.PresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class BridgeDetailViewModel {

    private Bridge bridge;

    public void setBridge(Bridge bridge) {
        this.bridge = bridge;
    }

    public String getCode() {
        return bridge.getCode();
    }

    public String getName() {
        return bridge.getName();
    }

    public String getCategory() {
        return bridge.getCategory();
    }

    public String getMaintainType() {
        return bridge.getMaintainType();
    }

    public String getMaintainLevel() {
        return bridge.getMaintainLevel();
    }

    public String getCreateTime() {
        return bridge.getCreateTime();
    }

    public String getDesigner() {
        return bridge.getDesigner();
    }

    public String getBuilder() {
        return bridge.getBuilder();
    }

    public String getLoad() {
        return bridge.getLoad();
    }

    public int getCount() {
        return bridge.getCount();
    }

    public String getDescription() {
        return bridge.getDescription();
    }
}
