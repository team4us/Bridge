package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.Bridge;
import com.xiaohui.bridge.model.BridgeModel;

import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
public class BridgeItemViewModel implements ItemPresentationModel<BridgeModel> {

    private String title;
    private String code;

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return "桥梁编号：" + code;
    }

    @Override
    public void updateData(BridgeModel bridge, ItemContext itemContext) {
        title = bridge.getBridge().getName();
        code = bridge.getBridge().getCode();
    }
}
