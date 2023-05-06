package com.eucleia.pdicheck.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.eucleia.pdicheck.room.entity.AnalyzeLog;

import java.util.List;

@Dao
public interface AnalyzeLogDao {

    @Insert
    void addLog(AnalyzeLog log);

    @Update
    void updateLog(AnalyzeLog log);

    @Query("DELETE FROM analyze_log WHERE id = :id")
    void deleteLog(long id);

    @Query("DELETE FROM analyze_log WHERE endTime = 0")
    void clearLog();

    @Query("SELECT * FROM analyze_log")
    List<AnalyzeLog> loadAll();

    @Query("SELECT * FROM analyze_log WHERE startTime = (:startTime)")
    AnalyzeLog loadCurr(long startTime);
}
