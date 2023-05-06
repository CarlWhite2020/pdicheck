/**
  * Copyright 2019 bejson.com 
  */
package com.eucleia.tabscanap.bean.diag.child;

import java.io.Serializable;

/**
 * Auto-generated: 2019-11-20 8:42:59
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Readiness implements Serializable {

    private String readiness_name;
    private String readiness_status;
    private int readiness_qualify;
    public void setReadiness_name(String readiness_name) {
         this.readiness_name = readiness_name;
     }
     public String getReadiness_name() {
         return readiness_name;
     }

    public void setReadiness_status(String readiness_status) {
         this.readiness_status = readiness_status;
     }
     public String getReadiness_status() {
         return readiness_status;
     }

    public int getReadiness_qualify() {
        return readiness_qualify;
    }

    public void setReadiness_qualify(int readiness_qualify) {
        this.readiness_qualify = readiness_qualify;
    }
}