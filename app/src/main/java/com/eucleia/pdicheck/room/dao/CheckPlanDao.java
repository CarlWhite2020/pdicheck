package com.eucleia.pdicheck.room.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.eucleia.pdicheck.room.entity.CheckPlan;
import com.eucleia.pdicheck.bean.normal.ModelYear;

import java.util.List;

@Dao
public interface CheckPlanDao {

    @Query("SELECT * FROM CheckPlan")
    List<CheckPlan> loadAll();

    @Query("SELECT DISTINCT  CheckPlan.model FROM CheckPlan ORDER BY id ASC ")
    String[] loadModel();

    @Query("SELECT DISTINCT CheckPlan.year AS year ,CheckPlan.model AS model FROM CheckPlan  ORDER BY id ASC ")
    List<ModelYear> loadModelYear();

    @Query("SELECT * FROM CheckPlan WHERE CheckPlan.datatype <= 1 AND CheckPlan.model = :model")
    List<CheckPlan> loadPlanSettings(String model);

    @Query("SELECT DISTINCT CheckPlan.funBlock AS String FROM CheckPlan WHERE CheckPlan.datatype <= 1  AND CheckPlan.model = :model")
    String[] loadPlanItems(String model);

    @Query("SELECT CheckPlan.funCode AS String FROM CheckPlan WHERE CheckPlan.enable = 1 Order by funCode ASC")
    String[] loadPlanFun();

    @Update
    void update(CheckPlan... plans);

    @Update
    void update(List<CheckPlan> plans);

    @Query("SELECT CheckPlan.datatype AS Integer FROM CheckPlan WHERE CheckPlan.enable = 1 AND CheckPlan.datatype > 1 limit 1")
    Integer getTranMode();

    @Query("UPDATE CheckPlan SET enable = 0 WHERE datatype > 1")
    void disTranMode();

    @Query("UPDATE CheckPlan SET enable = 1 WHERE datatype = 2")
    void closeTranMode();

    @Query("UPDATE CheckPlan SET enable = 1 WHERE datatype = 3")
    void openTranMode();

    @Query("SELECT funcode FROM CheckPlan  WHERE  CheckPlan.model = :model AND CheckPlan.year = :year limit 1")
    String getFunCodeByModelYear(String model, String year);
}
