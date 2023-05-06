package com.eucleia.tabscanap.util;

import androidx.room.Room;

import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.database.PDIDatabase;
import com.eucleia.tabscanap.constant.FileVar;

public class RoomUtils {

    public static PDIDatabase getPdiDB() {
        return Room.databaseBuilder(Utils.getApp(), PDIDatabase.class, FileVar.DB_PDI)
                .createFromAsset("database/pdi.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
    }

    public static AppDatabase getAppDB() {
        return Room.databaseBuilder(Utils.getApp(), AppDatabase.class, FileVar.DB_APP)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
    }

}
