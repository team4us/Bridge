package com.xiaohui.bridge.business;

import com.xiaohui.bridge.business.bean.Block;
import com.xiaohui.bridge.business.bean.Bridge;
import com.xiaohui.bridge.business.bean.ChildBridge;
import com.xiaohui.bridge.business.bean.Component;
import com.xiaohui.bridge.business.bean.Project;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.BlockModel;
import com.xiaohui.bridge.model.BridgeModel;
import com.xiaohui.bridge.model.ChildBridgeModel;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.model.UserModel;
import com.xiaohui.bridge.storage.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiaohui on 14-10-27.
 */
public class BusinessManager {

    private String[] generalsTypes = {"上部承重构件",
            "上部一般构件",
            "支座",
            "翼墙、耳墙",
            "锥坡、护坡",
            "桥墩",
            "桥台",
            "墩台基础",
            "河床",
            "调治构造物",
            "桥面铺装",
            "伸缩缝装置",
            "人行道",
            "栏杆、护栏",
            "排水系统",
            "照明、标志"};

    private String[][] generals = new String[][]{
            {"1号跨箱梁", "2号跨箱梁", "3号跨箱梁", "4号跨箱梁", "5号跨箱梁"},
            {"1-1横隔板", "1-2横隔板", "1-3横隔板", "1-4横隔板", "1-5横隔板"},
            {"支座1-0-1", "支座1-0-2", "支座1-0-3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"},
            {"构件1", "构件2", "构件3"}
    };

    public void download(DatabaseHelper helper, UserModel user) {
        List<Project> projects = StoreManager.Instance.getProjects();
        List<Bridge> bridges = StoreManager.Instance.getBridges();
        try {
            for (Project project : projects) {
                ProjectModel projectModel = new ProjectModel();
                projectModel.setProject(project);
                projectModel.setUserName(user.getUserName());
                projectModel.setUser(user);
                helper.getProjectDao().create(projectModel);

                for (Bridge bridge : bridges) {
                    BridgeModel bridgeModel = new BridgeModel();
                    bridgeModel.setProjectCode(project.getCode());
                    bridgeModel.setBridge(bridge);
                    bridgeModel.setProject(projectModel);
                    helper.getBridgeDao().create(bridgeModel);

                    String[] names = {"左幅桥", "右幅桥"};
                    for (int i = 0; i < 2; i++) {
                        ChildBridge childBridge = new ChildBridge(names[i]);
                        childBridge.setCategory("梁式桥");
                        childBridge.setCount("10");
                        childBridge.setCombination("10×80");
                        childBridge.setLength("800");
                        childBridge.setWidth("9");
                        ChildBridgeModel childBridgeModel = new ChildBridgeModel();
                        childBridgeModel.setChildBridge(childBridge);
                        childBridgeModel.setBridge(bridgeModel);
                        helper.getChildBridgeDao().create(childBridgeModel);
                        for (int j = 0; j < generalsTypes.length; j++) {
                            String type = generalsTypes[j];
                            BlockModel blockModel = new BlockModel();
                            Block block = new Block();
                            block.setId(j);
                            block.setName(type);
                            blockModel.setBlock(block);
                            blockModel.setChildBridge(childBridgeModel);
                            helper.getBlockDao().create(blockModel);
                            String[] componentNames = generals[j];
                            for (int k = 0; k < componentNames.length; k++) {
                                ComponentModel componentModel = new ComponentModel();
                                Component component = new Component();
                                component.setType(j);
                                component.setName(componentNames[k]);
                                componentModel.setComponent(component);
                                componentModel.setBlock(blockModel);
                                helper.getComponentDao().create(componentModel);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upload() {

    }
}
