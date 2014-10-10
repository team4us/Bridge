package com.xiaohui.bridge.business.bean;

/**
 * Created by xhChen on 14/9/27.
 */
public class Project {

    private String code; //项目编号
    private String name; //项目名称
    private String company; //委托单位
    private String category; //行业分类
    private String manager; //项目经理
    private String creator; //项目创建人
    private String auditor; //审核人
    private String judge; //审定人
    private String decisionMaker; //决策人
    private String member; //项目成员
    private String decription; //项目概况

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCompany() {
        return company;
    }

    public String getCategory() {
        return category;
    }

    public String getManager() {
        return manager;
    }

    public String getCreator() {
        return creator;
    }

    public String getAuditor() {
        return auditor;
    }

    public String getJudge() {
        return judge;
    }

    public String getDecisionMaker() {
        return decisionMaker;
    }

    public String getMember() {
        return member;
    }

    public String getDecription() {
        return decription;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public void setDecisionMaker(String decisionMaker) {
        this.decisionMaker = decisionMaker;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    @Override
    public String toString() {
        return "Project{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", category='" + category + '\'' +
                ", manager='" + manager + '\'' +
                ", creator='" + creator + '\'' +
                ", auditor='" + auditor + '\'' +
                ", judge='" + judge + '\'' +
                ", decisionMaker='" + decisionMaker + '\'' +
                ", member='" + member + '\'' +
                ", decription='" + decription + '\'' +
                '}';
    }
}
