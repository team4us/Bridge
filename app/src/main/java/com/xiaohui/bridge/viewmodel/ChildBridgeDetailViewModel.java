package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.ChildBridge;

import org.robobinding.aspects.PresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class ChildBridgeDetailViewModel {

    private ChildBridge childBridge;

    public void setChildBridge(ChildBridge childBridge) {
        this.childBridge = childBridge;
    }

    public String getName() {
        return childBridge.getName();
    }

    public String getCategory() {
        return childBridge.getCategory();
    }

    public String getCount() {
        return childBridge.getCount();
    }

    public String getCombination() {
        return childBridge.getCombination();
    }

    public String getLength() {
        return childBridge.getLength();
    }

    public String getWidth() {
        return childBridge.getWidth();
    }
}
