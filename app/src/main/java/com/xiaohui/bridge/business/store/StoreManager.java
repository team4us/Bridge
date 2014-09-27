package com.xiaohui.bridge.business.store;

import com.xiaohui.bridge.business.bean.Bridge;
import com.xiaohui.bridge.business.bean.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhChen on 14/9/27.
 */
public enum StoreManager {

    Instance;

    public List<Bridge> getBridges() {
        List<Bridge> bridges = new ArrayList<Bridge>();
        bridges.add(new Bridge("桥梁一"));
        bridges.add(new Bridge("桥梁二"));
        bridges.add(new Bridge("桥梁三"));
        return bridges;
    }

    public List<Project> getProjects() {
        List<Project> projects = new ArrayList<Project>();
        projects.add(new Project("桥梁一"));
        projects.add(new Project("桥梁二"));
        projects.add(new Project("桥梁三"));
        return projects;
    }

}
