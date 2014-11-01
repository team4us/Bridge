package com.xiaohui.bridge.business;

import android.os.Environment;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaohui on 14-10-27.
 */
public class BusinessManager {
    public static final String ALL_MEDIA_FILE_PATH = Environment.getExternalStorageDirectory() + "/IBridge/";
    public static String USER_MEDIA_FILE_PATH = "";

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

    private String[][] blockProperty = new String[][]{
            {"所在跨", "跨径或长度", "材料类型", "坐标系定义", "坐标系示意图", "截面", "表面展开图", "备注"},
            {"所在跨", "跨径或长度", "材料类型", "坐标系定义", "坐标系示意图", "截面", "表面展开图", "备注"},
            {"所在墩台", "支座类型", "支座型号", "X向", "Y向", "Z向", "备注"},
            {"所在桥台", "所在位置", "长度", "高度", "备注"},
            {"所在墩台", "所在位置", "锥坡护坡类型", "备注"},
            {"桥墩编号", "桥墩类型", "材料类型", "坐标系定义", "坐标系示意图", "截面", "表面展开图", "备注"},
            {"桥台编号", "桥台类型", "材料类型", "坐标系定义", "坐标系示意图", "截面", "表面展开图", "备注"},
            {"所在墩台", "基础类型", "备注"},
            {"河床类型", "河床防护方式", "备注"},
            {"调治构造物类型", "调治构造物简介", "备注"},
            {"所在跨", "桥面铺装类型", "铺装厚度（mm)", "备注"},
            {"所在墩台", "伸缩缝类型", "备注"},
            {"所在位置", "道板类型", "宽度", "备注"},
            {"所在位置", "栏杆类型", "高度", "备注"},
            {"排水系统简介", "备注"},
            {"照明设施简介", "标志类型及数量", "备注"},
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
                            block.setType(j);
                            block.setName(type);

                            String[] blockPropertyTitle = blockProperty[j];
                            Map<String, String> blockPropertyMap = new HashMap<String, String>();
                            for(int x = 0; x < blockPropertyTitle.length; x ++){
                                blockPropertyMap.put(blockPropertyTitle[x], "value " + x);
                            }

                            blockModel.setBlockProperty(blockPropertyMap);
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
