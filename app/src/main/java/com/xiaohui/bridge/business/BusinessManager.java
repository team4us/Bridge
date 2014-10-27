package com.xiaohui.bridge.business;

import com.xiaohui.bridge.business.bean.Bridge;
import com.xiaohui.bridge.business.bean.Project;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.BridgeModel;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.storage.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiaohui on 14-10-27.
 */
public class BusinessManager {

    public void download(DatabaseHelper helper, String userName) {
        List<Project> projects = StoreManager.Instance.getProjects();
        List<Bridge> bridges = StoreManager.Instance.getBridges();
        try {
            for (Project project : projects) {
                ProjectModel projectModel = new ProjectModel();
                projectModel.setProject(project);
                projectModel.setUserName(userName);
                helper.getProjectDao().create(projectModel);

                for (Bridge bridge : bridges) {
                    BridgeModel bridgeModel = new BridgeModel();
                    bridgeModel.setProjectCode(project.getCode());
                    bridgeModel.setBridge(bridge);
                    helper.getBridgeDao().create(bridgeModel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upload() {

    }
}
