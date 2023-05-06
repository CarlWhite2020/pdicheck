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
public class Livedata implements Serializable {

    private String livedata_name;
    private String livedata_unit;
    private String livedata_value;
    private String livedata_maxvalue;
    private String livedata_minvalue;
    private int livedata_qualify;

    public void setLivedata_name(String livedata_name) {
         this.livedata_name = livedata_name;
     }
     public String getLivedata_name() {
         return livedata_name;
     }

    public void setLivedata_unit(String livedata_unit) {
         this.livedata_unit = livedata_unit;
     }
     public String getLivedata_unit() {
         return livedata_unit;
     }

    public void setLivedata_value(String livedata_value) {
         this.livedata_value = livedata_value;
     }
     public String getLivedata_value() {
         return livedata_value;
     }

    public String getLivedata_maxvalue() {
        return livedata_maxvalue;
    }

    public void setLivedata_maxvalue(String livedata_maxvalue) {
        this.livedata_maxvalue = livedata_maxvalue;
    }

    public String getLivedata_minvalue() {
        return livedata_minvalue;
    }

    public void setLivedata_minvalue(String livedata_minvalue) {
        this.livedata_minvalue = livedata_minvalue;
    }

    public int getLivedata_qualify() {
        return livedata_qualify;
    }

    public void setLivedata_qualify(int livedata_qualify) {
        this.livedata_qualify = livedata_qualify;
    }
}