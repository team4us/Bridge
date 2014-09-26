package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.view.IBridgeView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.aspects.PresentationModel;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class BridgesViewModel {

    private IBridgeView bridgeView;
    private List<String> bridges = new ArrayList<String>(Arrays.asList("桥梁一", "桥梁二", "桥梁三"));

    public BridgesViewModel(IBridgeView view) {
        bridgeView = view;
    }

    @ItemPresentationModel(BridgeItemViewModel.class)
    public List<String> getBridges() {
        return bridges;
    }

    public void onItemClick(ItemClickEvent event) {
        if (bridgeView != null) {
            bridgeView.selectItem(event.getPosition(), bridges.get(event.getPosition()));
        }
    }

    public String getBridgeName(int pos) {
        return bridges.get(pos);
    }
}
