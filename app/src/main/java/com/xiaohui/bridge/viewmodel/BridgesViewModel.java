package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.model.BridgeModel;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.view.IBridgeView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class BridgesViewModel {

    private IBridgeView bridgeView;
    private List<BridgeModel> bridges;
    private Cookie cookie;

    public BridgesViewModel(IBridgeView view, Cookie cookie) {
        bridgeView = view;
        this.cookie = cookie;
    }

    @ItemPresentationModel(BridgeItemViewModel.class)
    public List<BridgeModel> getBridges() {
        ProjectModel project = (ProjectModel) cookie.get(Keys.PROJECT);
        if (bridges == null) {
            bridges = new ArrayList<BridgeModel>();
            Iterator<BridgeModel> iterator = project.getBridges().iterator();
            while (iterator.hasNext()) {
                BridgeModel bridgeModel = iterator.next();
                bridges.add(bridgeModel);
            }
        }
        return bridges;
    }

    public void onItemClick(ItemClickEvent event) {
        onItemClick(event.getPosition());
    }

    public void onItemClick(int position) {
        BridgeModel bridgeModel = bridges.get(position);
        cookie.put(Keys.BRIDGE, bridgeModel);
        if (bridgeView != null) {
            bridgeView.onItemSelect(position, bridgeModel);
        }
    }
}
