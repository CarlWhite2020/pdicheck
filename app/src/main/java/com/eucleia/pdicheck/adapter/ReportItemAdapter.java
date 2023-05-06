package com.eucleia.pdicheck.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.bean.constant.PDF5Constant;
import com.eucleia.pdicheck.bean.normal.ReportGroup;
import com.eucleia.pdicheck.databinding.ItemReportItemBinding;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.constant.MimeType;
import com.eucleia.tabscanap.constant.RequestCode;
import com.eucleia.tabscanap.util.EUriUtils;
import com.eucleia.tabscanap.util.ResUtils;

/**
 * 报告子项.
 */
public class ReportItemAdapter extends
        RecyclerView.Adapter<ReportItemAdapter.ReportItemHolder> {

    /**
     * 当前组全部数据.
     */
    private final ReportGroup groups;

    /**
     * 传入当前组.
     *
     * @param groups
     */
    public ReportItemAdapter(final ReportGroup groups) {
        this.groups = groups;
    }


    /**
     * 布局.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ReportItemHolder onCreateViewHolder(
            @NonNull final ViewGroup parent, final int viewType) {
        return new ReportItemHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_report_item, parent, false));
    }

    /**
     * 数据填充.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull final ReportItemHolder holder,
                                 final int position) {
        PdiReport report = groups.reports.get(position);
        holder.binding.dataId.setText(String.valueOf(report.id));
        String vin = TextUtils.isEmpty(report.vin)
                ? CharVar.EMPTY_VIN : report.vin;
        String number = ResUtils.getString(R.string.report_number)
                + vin + CharVar.MINUS
                + PDF5Constant.FORMAT.format(report.createTime);
        holder.binding.number.setText(number);
        holder.binding.yearModel
                .setText(report.year
                        + CharVar.SPACE
                        + report.model);

        if (report.result == 1) {
            holder.binding.result
                    .setText(ResUtils.getString(R.string.qualified));
            holder.binding.uploadStatus
                    .setTextColor(ResUtils.getColor(R.color.text_third));
        } else {
            holder.binding.result
                    .setText(ResUtils.getString(R.string.unqualified));
            holder.binding.uploadStatus
                    .setTextColor(ResUtils.getColor(R.color.negative));
        }

        if (report.uploadStatus == 0) {
            holder.binding.uploadStatus
                    .setText(ResUtils.getString(R.string.unload));
            holder.binding.uploadStatus
                    .setTextColor(ResUtils.getColor(R.color.text_third));
        } else if (report.uploadStatus == 1) {
            holder.binding.uploadStatus
                    .setText(ResUtils.getString(R.string.unload_success));
            holder.binding.uploadStatus
                    .setTextColor(ResUtils.getColor(R.color.text_third));
        } else if (report.uploadStatus == 2) {
            holder.binding.uploadStatus
                    .setText(ResUtils.getString(R.string.unload_fail));
            holder.binding.uploadStatus
                    .setTextColor(ResUtils.getColor(R.color.error));
        }

        holder.itemView.setOnClickListener(v -> {


            if (ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityUtils.getTopActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCode.READ_WRITE);
                return;
            }
            openPdf(report.path);

        });
    }


    private void openPdf(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri reportUri = EUriUtils.getUri(path);
        intent.setDataAndType(reportUri, MimeType.Type_Pdf);
        intent = Intent.createChooser(intent, ResUtils.getString(R.string.open_pdf));
        ActivityUtils.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return groups != null ? groups.reports.size() : 0;
    }

    public class ReportItemHolder extends RecyclerView.ViewHolder {
        ItemReportItemBinding binding;

        public ReportItemHolder(ItemReportItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
