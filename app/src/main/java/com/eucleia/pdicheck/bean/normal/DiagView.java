package com.eucleia.pdicheck.bean.normal;

import com.eucleia.tabscanap.constant.PageType;

public class DiagView {
   private int objKey;
   private PageType pageType;
   private long currTime;

   public int getObjKey() {
      return objKey;
   }

   public void setObjKey(int objKey) {
      this.objKey = objKey;
   }

   public PageType getPageType() {
      return pageType;
   }

   public void setPageType(PageType pageType) {
      this.pageType = pageType;
   }

   public long getCurrTime() {
      return currTime;
   }

   public void setCurrTime(long currTime) {
      this.currTime = currTime;
   }
}
