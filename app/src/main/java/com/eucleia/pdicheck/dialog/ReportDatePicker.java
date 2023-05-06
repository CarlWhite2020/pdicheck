package com.eucleia.pdicheck.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.databinding.DialogReportDatePickerBinding;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.EToastUtils;
import com.eucleia.tabscanap.util.ResUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 报告日期筛选器
 */
public class ReportDatePicker extends Dialog {

    DialogReportDatePickerBinding binding;

    private boolean isEndDate;
    private long startTime, endTime;
    private DatePickerListener listener;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

    public ReportDatePicker(@NonNull Context context, DatePickerListener listener) {
        super(context, R.style.DialogStyle);
        this.listener = listener;
        setOwnerActivity((Activity) context);
        initDate();
        initView();
        showDate();
        startClick();
    }

    private void initDate() {
        startTime = DSUtils.get().getLong(SPKEY.REPORT_START);
        endTime = DSUtils.get().getLong(SPKEY.REPORT_END);
        if (startTime == 0 || endTime == 0) {
            Date sdate = new Date();
            sdate.setHours(0);
            sdate.setMinutes(0);
            sdate.setSeconds(0);
            startTime = sdate.getTime();

            Date edate = new Date();
            edate.setHours(23);
            edate.setMinutes(59);
            edate.setSeconds(59);
            endTime = edate.getTime();
        }
    }

    private void showDate() {
        binding.startDate.setText(sdf.format(startTime));
        binding.endDate.setText(sdf.format(endTime));
    }


    private void initView() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_report_date_picker, null, false);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        binding.picker.setIndicator(true);
        binding.picker.setItemTextSize(ResUtils.getDimen(R.dimen.sp_12), false);
        binding.picker.setVisibleItemCount(5);
        binding.picker.setYearStart(2022);
        binding.picker.setYearEnd(Calendar.getInstance().get(Calendar.YEAR));
        binding.picker.setOnDateSelectedListener(date -> {

            if (isEndDate) {
                date.setHours(23);
                date.setMinutes(59);
                date.setSeconds(59);
                endTime = date.getTime();
            } else {
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                startTime = date.getTime();
            }
            showDate();
        });
        binding.btnCancel.setOnClickListener(v -> dismiss());
        binding.btnOk.setOnClickListener(v -> {
            if (startTime > endTime) {
                EToastUtils.get().showShort(R.string.start_less_end,false);
                return;
            }

            if (listener != null) {
                listener.onDatePicker(startTime, endTime);
            }
            dismiss();
        });
        binding.startTv.setOnClickListener(v -> startClick());
        binding.startDate.setOnClickListener(v -> startClick());

        binding.endTv.setOnClickListener(v -> endClick());
        binding.endDate.setOnClickListener(v -> endClick());
    }


    private void startClick() {
        isEndDate = false;
        initDateTv(binding.startTv, binding.startDate);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(startTime);
        binding.picker.setSelectedYear(instance.get(Calendar.YEAR));
        binding.picker.setSelectedMonth(instance.get(Calendar.MONTH) + 1);
        binding.picker.setSelectedDay(instance.get(Calendar.DAY_OF_MONTH));
    }


    private void endClick() {
        isEndDate = true;
        initDateTv(binding.endTv, binding.endDate);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(endTime);
        binding.picker.setSelectedYear(instance.get(Calendar.YEAR));
        binding.picker.setSelectedMonth(instance.get(Calendar.MONTH) + 1);
        binding.picker.setSelectedDay(instance.get(Calendar.DAY_OF_MONTH));
    }

    private void initDateTv(TextView tv, TextView date) {
        binding.startTv.setTextColor(ResUtils.getColor(R.color.text_hint));
        binding.startDate.setTextColor(ResUtils.getColor(R.color.text_hint));
        binding.startDate.setTypeface(Typeface.DEFAULT);

        binding.endTv.setTextColor(ResUtils.getColor(R.color.text_hint));
        binding.endDate.setTextColor(ResUtils.getColor(R.color.text_hint));
        binding.endDate.setTypeface(Typeface.DEFAULT);

        tv.setTextColor(ResUtils.getColor(R.color.text_second));
        date.setTextColor(ResUtils.getColor(R.color.text_main));
        date.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public interface DatePickerListener {
        void onDatePicker(long sd, long ed);
    }

}
