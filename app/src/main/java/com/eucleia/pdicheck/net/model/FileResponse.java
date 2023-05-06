package com.eucleia.pdicheck.net.model;

public class FileResponse {
   /**
    * {"msg":"vin:无VIN,没有PDI检测数据，不允许上传电检报告文件","code":0}
    */
   private String msg;
   private int code;

   public String getMsg() {
      return msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public int getCode() {
      return code;
   }

   public void setCode(int code) {
      this.code = code;
   }
}
