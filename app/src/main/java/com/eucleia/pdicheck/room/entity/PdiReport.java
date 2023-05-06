package com.eucleia.pdicheck.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pdi_report")
public class PdiReport {

   @PrimaryKey(autoGenerate = true)
   public Long id;

   @ColumnInfo
   public int result;

   @ColumnInfo
   public String year;

   @ColumnInfo
   public String model;

   @ColumnInfo
   public String brand;

   @ColumnInfo
   public int uploadStatus;//0未上传，1上传成功，2上传失败

   @ColumnInfo
   public String path;

   @ColumnInfo
   public String vin;

   @ColumnInfo
   public long createTime;

   @ColumnInfo
   public int dtcNum;

   @ColumnInfo
   public String co;

   @ColumnInfo
   public String fileName;

}
