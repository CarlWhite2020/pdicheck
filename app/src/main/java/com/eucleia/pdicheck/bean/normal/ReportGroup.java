package com.eucleia.pdicheck.bean.normal;

import com.eucleia.pdicheck.room.entity.PdiReport;

import java.util.ArrayList;
import java.util.List;

public class ReportGroup {
   public String groupName;
   public List<PdiReport> reports  = new ArrayList<>();
   public boolean isShow = true;
}
