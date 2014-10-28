package com.xiaohui.bridge.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.xiaohui.bridge.business.bean.Component;
import com.xiaohui.bridge.model.BridgeModel;
import com.xiaohui.bridge.model.ChildBridgeModel;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.model.UserModel;

import java.sql.SQLException;

/**
 * Created by xiaohui on 14-2-12.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "Store.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseTableConfig<UserModel> userConfig = new DatabaseTableConfig<UserModel>();
    private DatabaseTableConfig<ProjectModel> projectConfig = new DatabaseTableConfig<ProjectModel>();
    private DatabaseTableConfig<BridgeModel> bridgeConfig = new DatabaseTableConfig<BridgeModel>();
    private DatabaseTableConfig<ChildBridgeModel> childBridgeConfig = new DatabaseTableConfig<ChildBridgeModel>();
    private DatabaseTableConfig<ComponentModel> componentConfig = new DatabaseTableConfig<ComponentModel>();
    private DatabaseTableConfig<DiseaseModel> diseaseConfig = new DatabaseTableConfig<DiseaseModel>();

    private Dao<UserModel, Integer> userDao = null;
    private Dao<ProjectModel, Integer> projectDao = null;
    private Dao<BridgeModel, Integer> bridgeDao = null;
    private Dao<ChildBridgeModel, Integer> childBridgeDao = null;
    private Dao<ComponentModel, Integer> componentDao = null;
    private Dao<DiseaseModel, Integer> diseaseDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        userConfig.setDataClass(UserModel.class);
        userConfig.initialize();

        projectConfig.setDataClass(ProjectModel.class);
        projectConfig.initialize();

        bridgeConfig.setDataClass(BridgeModel.class);
        bridgeConfig.initialize();

        childBridgeConfig.setDataClass(ChildBridgeModel.class);
        childBridgeConfig.initialize();

        componentConfig.setDataClass(ComponentModel.class);
        componentConfig.initialize();

        diseaseConfig.setDataClass(DiseaseModel.class);
        diseaseConfig.initialize();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, userConfig);
            TableUtils.createTable(connectionSource, projectConfig);
            TableUtils.createTable(connectionSource, bridgeConfig);
            TableUtils.createTable(connectionSource, childBridgeConfig);
            TableUtils.createTable(connectionSource, componentConfig);
            TableUtils.createTable(connectionSource, diseaseConfig);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, userConfig, true);
            TableUtils.dropTable(connectionSource, projectConfig, true);
            TableUtils.dropTable(connectionSource, bridgeConfig, true);
            TableUtils.dropTable(connectionSource, childBridgeConfig, true);
            TableUtils.dropTable(connectionSource, componentConfig, true);
            TableUtils.dropTable(connectionSource, diseaseConfig, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
        projectDao = null;
        bridgeDao = null;
        userDao = null;
        childBridgeDao = null;
        componentDao = null;
        diseaseDao = null;
    }

    public Dao<UserModel, Integer> getUserDao() {
        if (userDao == null) {
            try {
                userDao = DaoManager.createDao(getConnectionSource(), userConfig);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userDao;
    }

    public Dao<ProjectModel, Integer> getProjectDao() {
        if (projectDao == null) {
            try {
                projectDao = DaoManager.createDao(getConnectionSource(), projectConfig);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return projectDao;
    }

    public Dao<BridgeModel, Integer> getBridgeDao() {
        if (bridgeDao == null) {
            try {
                bridgeDao = DaoManager.createDao(getConnectionSource(), bridgeConfig);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bridgeDao;
    }

    public Dao<ChildBridgeModel, Integer> getChildBridgeDao() {
        if (childBridgeDao == null) {
            try {
                childBridgeDao = DaoManager.createDao(getConnectionSource(), childBridgeConfig);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return childBridgeDao;
    }

    public Dao<ComponentModel, Integer> getComponentDao() {
        if (componentDao == null) {
            try {
                componentDao = DaoManager.createDao(getConnectionSource(), componentConfig);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return componentDao;
    }

    public Dao<DiseaseModel, Integer> getDiseaseDao() {
        if (diseaseDao == null) {
            try {
                diseaseDao = DaoManager.createDao(getConnectionSource(), diseaseConfig);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return diseaseDao;
    }
}
