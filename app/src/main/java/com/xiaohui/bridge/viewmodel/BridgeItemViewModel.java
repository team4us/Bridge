package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.Bridge;

import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
public class BridgeItemViewModel implements ItemPresentationModel<Bridge> {

    private String title;
    private String code;

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return "桥梁编号：" + code;
    }

    @Override
    public void updateData(Bridge bridge, ItemContext itemContext) {
        title = bridge.getName();
        code = bridge.getCode();
    }
}
