package com.eucleia.pdicheck.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey
    public long id;
    @ColumnInfo
    public String userCode;
    @ColumnInfo
    public String userName;
    @ColumnInfo
    public String roleCode;
    @ColumnInfo
    public String roleName;
    @ColumnInfo
    public String userStatus;
    @ColumnInfo
    public String userStatusName;
    @ColumnInfo
    public String phone;
    @ColumnInfo
    public String email;
    @ColumnInfo
    public String addrCode;
    @ColumnInfo
    public String addrName;
    @ColumnInfo
    public String createdBy;
    @ColumnInfo
    public String createdOn;
    @ColumnInfo
    public String modifiedBy;
    @ColumnInfo
    public String modifiedOn;
    @ColumnInfo
    public boolean isLogin;
}
