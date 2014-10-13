package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.Bridge;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.BridgesModel;
import com.xiaohui.bridge.view.IBridgeView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class BridgesViewModel {

    private IBridgeView bridgeView;
    private BridgesModel model;
    private int position;

    public BridgesViewModel(IBridgeView view) {
        model = new BridgesModel(StoreManager.Instance.getBridges());
        bridgeView = view;
    }

    @ItemPresentationModel(BridgeItemViewModel.class)
    public List<Bridge> getBridges() {
        return model.getBridges();
    }

    public void onItemClick(ItemClickEvent event) {
        position = event.getPosition();
        if (bridgeView != null) {
            bridgeView.notifyChange();
        }
    }

    public Bridge getCurrentBridge() {
        return model.getBridge(position);
    }
}
