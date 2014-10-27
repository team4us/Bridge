package com.xiaohui.bridge.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.xiaohui.bridge.model.BridgeModel;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.model.UserModel;

import java.sql.SQLException;

/**
 * Created by xiaohui on 14-2-12.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "Store.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseTableConfig<ProjectModel> projectConfig = new DatabaseTableConfig<ProjectModel>();
    private DatabaseTableConfig<BridgeModel> bridgeConfig = new DatabaseTableConfig<BridgeModel>();
    private DatabaseTableConfig<UserModel> userConfig = new DatabaseTableConfig<UserModel>();
    private Dao<ProjectModel, Integer> projectDao = null;
    private Dao<BridgeModel, Integer> bridgeDao = null;
    private Dao<UserModel, Integer> userDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        projectConfig.setDataClass(ProjectModel.class);
        projectConfig.setTableName("Project");
        projectConfig.initialize();

        bridgeConfig.setDataClass(BridgeModel.class);
        bridgeConfig.setTableName("Bridge");
        bridgeConfig.initialize();

        userConfig.setDataClass(UserModel.class);
        userConfig.setTableName("User");
        userConfig.initialize();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, projectConfig);
            TableUtils.createTable(connectionSource, bridgeConfig);
            TableUtils.createTable(connectionSource, userConfig);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, projectConfig, true);
            TableUtils.dropTable(connectionSource, bridgeConfig, true);
            TableUtils.dropTable(connectionSource, userConfig, true);
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
}
