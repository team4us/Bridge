package com.xiaohui.bridge.viewmodel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.xiaohui.bridge.business.bean.Bridge;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.BridgeModel;
import com.xiaohui.bridge.model.BridgesModel;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.view.IBridgeView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class BridgesViewModel {

    private IBridgeView bridgeView;
    private BridgesModel model;
    private int position;
    private Dao<BridgeModel, Integer> dao;
    private List<BridgeModel> bridges;
    private String projectCode;

    public BridgesViewModel(IBridgeView view, Dao<BridgeModel, Integer> dao, String projectCode) {
        model = new BridgesModel(StoreManager.Instance.getBridges());
        bridgeView = view;
        this.dao = dao;
        this.projectCode = projectCode;
    }

    @ItemPresentationModel(BridgeItemViewModel.class)
    public List<BridgeModel> getBridges() {
        if (bridges == null) {
            try {
                QueryBuilder<BridgeModel, Integer> builder = dao.queryBuilder();
                builder.where().eq("projectCode", projectCode);
                builder.orderBy("Id", false);
                bridges = builder.query();
            } catch (SQLException e) {
                e.printStackTrace();
                bridges = new ArrayList<BridgeModel>();
            }
        }
        return bridges;
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
