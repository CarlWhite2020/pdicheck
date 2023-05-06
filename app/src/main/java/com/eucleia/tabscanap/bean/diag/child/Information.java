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
public class Information implements Serializable {

    private String information_name;
    private String information_value;
    public void setInformation_name(String information_name) {
         this.information_name = information_name;
     }
     public String getInformation_name() {
         return information_name;
     }

    public void setInformation_value(String information_value) {
         this.information_value = information_value;
     }
     public String getInformation_value() {
         return information_value;
     }

}