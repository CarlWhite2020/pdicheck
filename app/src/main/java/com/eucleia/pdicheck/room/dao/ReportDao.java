package com.eucleia.pdicheck.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.room.entity.PdiReport;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

@Dao
public interface ReportDao {

    @Query("SELECT * FROM pdi_report WHERE" +
            " (:result=-1 OR result=:result)  " +
            "AND (:status=0 OR uploadStatus=:status)  " +
            "AND (:model='' OR model=:model)  " +
            "AND (:key='' OR vin LIKE :key)  " +
            "AND (:startTime=0 OR createTime>:startTime)  " +
            "AND (:endTime=0 OR createTime<:endTime)  " +
            "Order By createTime DESC")
    Observable<List<PdiReport>> loadByCondition(int result, int status, String model, long startTime, long endTime, String key);

    @Update
    void updateReport(PdiReport... report);

    @Insert
    Long createReport(PdiReport report);

    @Query("DELETE FROM pdi_report")
    void clearAll();

    @Query("DELETE FROM pdi_report WHERE createTime < (:timeInMillis)")
    void clearByTime(long timeInMillis);

    @Query("SELECT path FROM pdi_report WHERE createTime < (:timeInMillis)")
    List<String> queryByTime(long timeInMillis);

    @Query("SELECT * FROM pdi_report WHERE uploadStatus = 2")
    Observable<List<PdiReport>> queryError();

    @Query("SELECT * FROM pdi_report Order By createTime DESC Limit 1")
    Observable<PdiReport> queryLast();
}
