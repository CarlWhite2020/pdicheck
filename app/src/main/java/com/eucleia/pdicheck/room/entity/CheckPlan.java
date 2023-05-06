package com.eucleia.pdicheck.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "checkplan")
public class CheckPlan {

    @PrimaryKey
    @ColumnInfo(name = "id")
    public Integer id;

    @ColumnInfo(name = "funblock")
    public String funBlock;

    @ColumnInfo(name = "funname")
    public String funName;

    @ColumnInfo(name = "funcode")
    public String funCode;

    @ColumnInfo(name = "model")
    public String model;

    @ColumnInfo(name = "year")
    public Integer year;

    @ColumnInfo(name = "datatype")
    public Integer dataType;

    @ColumnInfo(name = "enable")
    public Integer enable;

}
