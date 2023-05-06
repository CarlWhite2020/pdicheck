package com.eucleia.pdicheck.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.eucleia.pdicheck.room.dao.AnalyzeLogDao;
import com.eucleia.pdicheck.room.dao.ReportDao;
import com.eucleia.pdicheck.room.dao.UserDao;
import com.eucleia.pdicheck.room.entity.AnalyzeLog;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.pdicheck.room.entity.User;
import com.eucleia.tabscanap.util.RoomUtils;

@Database(entities = {User.class,PdiReport.class, AnalyzeLog.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static class Instance {
        private final static AppDatabase instance = RoomUtils.getAppDB();
    }

    public static AppDatabase get() {
        return Instance.instance;
    }

    public abstract UserDao getUserDao();

    public abstract ReportDao getReportDao();

    public abstract AnalyzeLogDao getLogDao();
}
