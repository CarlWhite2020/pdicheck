package com.eucleia.pdicheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.adapter.empty.EmptyAdapter;
import com.eucleia.pdicheck.adapter.empty.EmptyHolder;
import com.eucleia.pdicheck.bean.constant.PDF5Constant;
import com.eucleia.pdicheck.bean.normal.ReportGroup;
import com.eucleia.pdicheck.databinding.ItemReportGroupBinding;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.pdicheck.widget.NoScrollManager;
import com.eucleia.tabscanap.util.ArraysUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 首页报告 组
 */
public class ReportGroupAdapter extends EmptyAdapter {

    private List<PdiReport> groups;
    private LinkedHashMap<String, ReportGroup> reportMap = new LinkedHashMap<>();
    private List<String> keys;

    public ReportGroupAdapter(List<PdiReport> reports) {
        setReports(reports);
    }

    protected boolean emptyType() {
        return ArraysUtils.isEmpty(reportMap);
    }


    @NonNull
    @Override
    public EmptyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (!emptyType()) {
            return new ReportGroupHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_report_group, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected int getItemSize() {
        return keys.size();
    }


    @Override
    public void onBindViewHolder(@NonNull EmptyHolder holder, int position) {
        if (emptyType()) {
            super.onBindViewHolder(holder, position);
            return;
        }

        ReportGroupHolder groupHolder = (ReportGroupHolder) holder;
        String groupName = keys.get(position);
        ReportGroup groups = reportMap.get(groupName);
        if (groups != null) {
            if (groups.isShow) {
                groupHolder.groupBinding.itemRecycler.setVisibility(View.VISIBLE);

                ReportItemAdapter adapter = new ReportItemAdapter(groups);
                groupHolder.groupBinding.itemRecycler.setLayoutManager(new NoScrollManager(holder.itemView.getContext()));
                groupHolder.groupBinding.itemRecycler.setAdapter(adapter);
            } else {
                groupHolder.groupBinding.itemRecycler.setVisibility(View.GONE);
            }

            groupHolder.groupBinding.groupName.setText(groupName);
            groupHolder.groupBinding.groupName.setOnClickListener(v -> {
                groups.isShow = !groups.isShow;
            });
        }


    }

    public void setReports(List<PdiReport> groups) {
        this.groups = groups;
        reportMap.clear();
        if (!ArraysUtils.isEmpty(groups)) {
            for (PdiReport pr : groups) {
                String key = PDF5Constant.PATH_FORMAT.format(pr.createTime);
                ReportGroup reportGroup = reportMap.get(key);
                if (reportGroup == null) {
                    reportGroup = new ReportGroup();
                    reportGroup.groupName = key;
                }
                reportGroup.reports.add(pr);
                reportMap.put(key, reportGroup);
            }
        }
        keys = new ArrayList<>(reportMap.keySet());
    }

    public class ReportGroupHolder extends EmptyHolder {

        public ItemReportGroupBinding groupBinding;

        public ReportGroupHolder(ItemReportGroupBinding binding) {
            super(binding.getRoot());
            this.groupBinding = binding;
        }
    }

}
