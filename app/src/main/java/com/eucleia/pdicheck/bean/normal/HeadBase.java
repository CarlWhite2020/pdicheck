package com.eucleia.pdicheck.bean.normal;

import com.eucleia.pdicheck.listener.SingleClickListener;

public class HeadBase {

   private VciStatus vciStatus;
   private boolean vciVisibility =true;
   private boolean backVisibility = true;
   private String title;
   private int backResId;
   private String backText;
   private SingleClickListener backListener;
   private SingleClickListener rightListener;

   public VciStatus getVciStatus() {
      return vciStatus;
   }

   public void setVciStatus(VciStatus vciStatus) {
      this.vciStatus = vciStatus;
   }

   public boolean isVciVisibility() {
      return vciVisibility;
   }

   public void setVciVisibility(boolean vciVisibility) {
      this.vciVisibility = vciVisibility;
   }

   public boolean isBackVisibility() {
      return backVisibility;
   }

   public void setBackVisibility(boolean backVisibility) {
      this.backVisibility = backVisibility;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public int getBackResId() {
      return backResId;
   }

   public void setBackResId(int backResId) {
      this.backResId = backResId;
   }

   public String getBackText() {
      return backText;
   }

   public void setBackText(String backText) {
      this.backText = backText;
   }

   public SingleClickListener getBackListener() {
      return backListener;
   }

   public void setBackListener(SingleClickListener backListener) {
      this.backListener = backListener;
   }

   public SingleClickListener getRightListener() {
      return rightListener;
   }

   public void setRightListener(SingleClickListener rightListener) {
      this.rightListener = rightListener;
   }
}
