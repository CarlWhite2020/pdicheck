package com.eucleia.pdicheck.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "analyze_log")
public class AnalyzeLog {

   @PrimaryKey(autoGenerate = true)
   public Long id;

   @ColumnInfo
   public long startTime;

   @ColumnInfo
   public long endTime;

   @ColumnInfo
   public String user;

   @ColumnInfo
   public String jniModel;

   @ColumnInfo
   public String diagModel;

   @ColumnInfo
   public String diagStep;

   @ColumnInfo
   public String appName;

   @ColumnInfo
   public String appVersion;

   @ColumnInfo
   public String dynamicVersion;

   @ColumnInfo
   public String commVersion;

   @ColumnInfo
   public String logPath;

   @ColumnInfo
   public String vciVersion;

   @ColumnInfo
   public String snCode;

   @ColumnInfo
   public String checkCode;

   @ColumnInfo
   public String zipPath;

   @ColumnInfo
   public String fileName;

}
