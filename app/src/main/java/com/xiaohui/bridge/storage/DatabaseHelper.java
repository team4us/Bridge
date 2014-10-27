package com.xiaohui.bridge.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.xiaohui.bridge.model.ProjectModel;

import java.sql.SQLException;

/**
 * Created by xiaohui on 14-2-12.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "Store.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseTableConfig<ProjectModel> projectsConfig = new DatabaseTableConfig<ProjectModel>();
    private Dao<ProjectModel, Integer> projectsDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        projectsConfig.setDataClass(ProjectModel.class);
        projectsConfig.setTableName("Projects");
        projectsConfig.initialize();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, projectsConfig);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, projectsConfig, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
        projectsDao = null;
    }

    public Dao<ProjectModel, Integer> getProjectsDao() {
        if (projectsDao == null) {
            try {
                projectsDao = DaoManager.createDao(getConnectionSource(), projectsConfig);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return projectsDao;
    }
}
