package com.xiaohui.bridge.viewmodel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.QueryBuilder;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.business.bean.Project;
import com.xiaohui.bridge.model.BridgeModel;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.view.IBridgeView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private BridgeModel currentBridgeModel;

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
        currentBridgeModel = bridges.get(position);
        cookie.put(Keys.BRIDGE, currentBridgeModel);
        if (bridgeView != null) {
            bridgeView.notifyChange();
        }
    }

    public BridgeModel getCurrentBridgeModel() {
        if (currentBridgeModel == null) {
            currentBridgeModel = getBridges().get(0);
        }
        return currentBridgeModel;
    }
}
