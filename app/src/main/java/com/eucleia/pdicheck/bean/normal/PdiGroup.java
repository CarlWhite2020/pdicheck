package com.eucleia.pdicheck.bean.normal;

import java.util.Stack;

public class PdiGroup {
   private String groupName;
   private boolean isShowChild;
   private Stack<PdiItem> items = new Stack<>();
   private int result;
   private boolean isShowGroup;
   private volatile boolean needUpdate = true;

   public String getGroupName() {
      return groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public boolean isShowChild() {
      return isShowChild;
   }

   public void setShowChild(boolean showChild) {
      isShowChild = showChild;
   }


   public int getResult() {
      return result;
   }

   public void setResult(int result) {
      this.result = result;
   }


   public PdiItem getPdiItem() {
      return items.peek();
   }

   public void putPdiItem(PdiItem group) {
      items.push(group);
   }

   public Stack<PdiItem> getItems() {
      return items;
   }

   public void setItems(Stack<PdiItem> items) {
      this.items = items;
   }

   public boolean isShowGroup() {
      return isShowGroup;
   }

   public void setShowGroup(boolean showGroup) {
      this.isShowGroup = showGroup;
   }

   public boolean isNeedUpdate() {
      return needUpdate;
   }

   public void setNeedUpdate(boolean needUpdate) {
      this.needUpdate = needUpdate;
   }
}
