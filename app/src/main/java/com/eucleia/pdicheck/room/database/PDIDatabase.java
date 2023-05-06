package com.eucleia.pdicheck.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.eucleia.pdicheck.room.dao.CheckPlanDao;
import com.eucleia.pdicheck.room.entity.CheckPlan;
import com.eucleia.tabscanap.util.RoomUtils;

@Database(entities = {CheckPlan.class}, version = 5, exportSchema = false)
public abstract class PDIDatabase extends RoomDatabase {

    private static class Instance {
        private static final PDIDatabase INSTANCE = RoomUtils.getPdiDB();
    }


    public static PDIDatabase get() {
        return Instance.INSTANCE;
    }


    public abstract CheckPlanDao getCheckPlanDao();

}
