package com.xiaohui.bridge.business.store;

import com.xiaohui.bridge.business.bean.Bridge;
import com.xiaohui.bridge.business.bean.ChildBridge;
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
        Bridge bridge = new Bridge("天保桥");
        bridge.setCode("Q001");
        bridge.setCategory("大桥");
        bridge.setMaintainType("Ⅴ");
        bridge.setMaintainLevel("Ⅰ");
        bridge.setCreateTime("2005");
        bridge.setDesigner("设计单位1");
        bridge.setBuilder("施工单位1");
        bridge.setLoad("汽车-超20级");
        bridge.setCount(2);
        ChildBridge childBridge = new ChildBridge("左幅桥");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        childBridge = new ChildBridge("右幅桥");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        bridges.add(bridge);

        bridge = new Bridge("纬九路匝道");
        bridge.setCode("Q002");
        bridge.setCategory("特大桥");
        bridge.setMaintainType("Ⅲ");
        bridge.setMaintainLevel("Ⅰ");
        bridge.setCreateTime("2013");
        bridge.setDesigner("设计单位2");
        bridge.setBuilder("施工单位2");
        bridge.setLoad("汽车-10级");
        childBridge = new ChildBridge("A匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        childBridge = new ChildBridge("B匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        bridges.add(bridge);
        childBridge = new ChildBridge("C匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        bridges.add(bridge);


        bridge = new Bridge("纬九路匝道");
        bridge.setCode("Q002");
        bridge.setCategory("特大桥");
        bridge.setMaintainType("Ⅲ");
        bridge.setMaintainLevel("Ⅰ");
        bridge.setCreateTime("2013");
        bridge.setDesigner("设计单位2");
        bridge.setBuilder("施工单位2");
        bridge.setLoad("汽车-10级");
        childBridge = new ChildBridge("A匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        childBridge = new ChildBridge("B匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        bridges.add(bridge);
        childBridge = new ChildBridge("C匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        bridges.add(bridge);

        bridge = new Bridge("纬九路匝道");
        bridge.setCode("Q002");
        bridge.setCategory("特大桥");
        bridge.setMaintainType("Ⅲ");
        bridge.setMaintainLevel("Ⅰ");
        bridge.setCreateTime("2013");
        bridge.setDesigner("设计单位2");
        bridge.setBuilder("施工单位2");
        bridge.setLoad("汽车-10级");
        childBridge = new ChildBridge("A匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        childBridge = new ChildBridge("B匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        bridges.add(bridge);
        childBridge = new ChildBridge("C匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);

        bridge = new Bridge("纬九路匝道");
        bridge.setCode("Q002");
        bridge.setCategory("特大桥");
        bridge.setMaintainType("Ⅲ");
        bridge.setMaintainLevel("Ⅰ");
        bridge.setCreateTime("2013");
        bridge.setDesigner("设计单位2");
        bridge.setBuilder("施工单位2");
        bridge.setLoad("汽车-10级");
        childBridge = new ChildBridge("A匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        childBridge = new ChildBridge("B匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        bridges.add(bridge);
        childBridge = new ChildBridge("C匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);


        bridge = new Bridge("纬九路匝道");
        bridge.setCode("Q002");
        bridge.setCategory("特大桥");
        bridge.setMaintainType("Ⅲ");
        bridge.setMaintainLevel("Ⅰ");
        bridge.setCreateTime("2013");
        bridge.setDesigner("设计单位2");
        bridge.setBuilder("施工单位2");
        bridge.setLoad("汽车-10级");
        childBridge = new ChildBridge("A匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        childBridge = new ChildBridge("B匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        bridges.add(bridge);
        childBridge = new ChildBridge("C匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);


        bridge = new Bridge("纬九路匝道");
        bridge.setCode("Q002");
        bridge.setCategory("特大桥");
        bridge.setMaintainType("Ⅲ");
        bridge.setMaintainLevel("Ⅰ");
        bridge.setCreateTime("2013");
        bridge.setDesigner("设计单位2");
        bridge.setBuilder("施工单位2");
        bridge.setLoad("汽车-10级");
        childBridge = new ChildBridge("A匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        childBridge = new ChildBridge("B匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);
        bridges.add(bridge);
        childBridge = new ChildBridge("C匝道");
        childBridge.setCategory("梁式桥");
        bridge.addChildBridge(childBridge);

        return bridges;
    }

    public List<Project> getProjects() {
        List<Project> projects = new ArrayList<Project>();
        Project project = new Project("南京城市桥梁常规定期检测");
        project.setCode("20140057");
        project.setCompany("南京市城管局");
        project.setCategory("市政");
        project.setManager("张榕");
        project.setCreator("张国庆");
        project.setAuditor("朱从明");
        project.setJudge("朱晓文");
        project.setTechManager("刘玉胜");
        project.setDecisionMaker("徐庆丰");
        project.setMember("张国立 魏万刚 邵加玉 王鑫 王召全");
        project.setDescription("");
        projects.add(project);

        project = new Project("宁连高速公路桥梁定期检查");
        project.setCode("20140057");
        project.setCompany("江苏省高速公路管理中心");
        project.setCategory("市政");
        project.setManager("李琦");
        project.setCreator("万涛");
        project.setAuditor("韩尚武");
        project.setJudge("赖广胜");
        project.setDecisionMaker("徐庆丰");
        project.setMember("张国立 魏万刚 邵加玉 王鑫 王召全");
        projects.add(project);

        project = new Project("宁连高速公路桥梁定期检查");
        project.setCode("20140057");
        project.setCompany("江苏省高速公路管理中心");
        project.setCategory("市政");
        project.setManager("李琦");
        project.setCreator("万涛");
        project.setAuditor("韩尚武");
        project.setJudge("赖广胜");
        project.setDecisionMaker("徐庆丰");
        project.setMember("张国立 魏万刚 邵加玉 王鑫 王召全");
        projects.add(project);

        project = new Project("宁连高速公路桥梁定期检查");
        project.setCode("20140057");
        project.setCompany("江苏省高速公路管理中心");
        project.setCategory("市政");
        project.setManager("李琦");
        project.setCreator("万涛");
        project.setAuditor("韩尚武");
        project.setJudge("赖广胜");
        project.setDecisionMaker("徐庆丰");
        project.setMember("张国立 魏万刚 邵加玉 王鑫 王召全");
        projects.add(project);

        project = new Project("宁连高速公路桥梁定期检查");
        project.setCode("20140057");
        project.setCompany("江苏省高速公路管理中心");
        project.setCategory("市政");
        project.setManager("李琦");
        project.setCreator("万涛");
        project.setAuditor("韩尚武");
        project.setJudge("赖广胜");
        project.setDecisionMaker("徐庆丰");
        project.setMember("张国立 魏万刚 邵加玉 王鑫 王召全");
        projects.add(project);

        project = new Project("宁连高速公路桥梁定期检查");
        project.setCode("20140057");
        project.setCompany("江苏省高速公路管理中心");
        project.setCategory("市政");
        project.setManager("李琦");
        project.setCreator("万涛");
        project.setAuditor("韩尚武");
        project.setJudge("赖广胜");
        project.setDecisionMaker("徐庆丰");
        project.setMember("张国立 魏万刚 邵加玉 王鑫 王召全");
        projects.add(project);

        project = new Project("无锡2014第一批城市桥梁定期检测");
        project.setCode("20140057");
        project.setCompany("无锡市市政设施管理处");
        project.setCategory("市政");
        project.setManager("万捷");
        project.setCreator("钱宏刚");
        project.setAuditor("韩尚武");
        project.setJudge("赖广胜");
        project.setDecisionMaker("徐庆丰");
        project.setMember("张国立 魏万刚 邵加玉 王鑫 王召全");
        projects.add(project);

        return projects;
    }

    public String[] generalsTypes = new String[]{
            "左腹板",
            "底板",
            "右腹板",
            "右翼缘板"};

    public String[] diseaseTypes = new String[]{"裂缝", "破损", "钢筋锈蚀", "其他"};

    public String[] diseaseInputTypes = new String[]{"方法1", "方法2", "方法3", "方法4", "方法5"};

}
