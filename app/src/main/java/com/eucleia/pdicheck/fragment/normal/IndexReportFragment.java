package com.eucleia.pdicheck.fragment.normal;

import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.adapter.ReportGroupAdapter;
import com.eucleia.pdicheck.adapter.ReportModelAdapter;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.databinding.FragIndexReportBinding;
import com.eucleia.pdicheck.databinding.ItemIndexReportFilterModelBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadNormalBinding;
import com.eucleia.pdicheck.dialog.ReportClearDialog;
import com.eucleia.pdicheck.dialog.ReportDatePicker;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.pdicheck.listener.IndexReportFunListener;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.net.presenter.PdiReportPresenter;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.pdicheck.widget.ETextWatcher;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.dialog.BaseDialog;
import com.eucleia.tabscanap.util.BaseObserver;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.ResUtils;
import com.eucleia.tabscanap.util.RoomUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 报告
 */
public class IndexReportFragment extends BaseFragment {

    private FragIndexReportBinding binding;
    private LayoutHeadNormalBinding headNormal;

    private int resultFlag = -1;
    private int statusFlag = 0;
    private String modelFlag = CharVar.EMPTY;
    private long sd;
    private long ed;
    private CharSequence key = CharVar.EMPTY;

    private List<PdiReport> reports;
    private ReportGroupAdapter groupAdapter;
    private ReportModelAdapter modelAdapter;
    private ReportClearDialog clearDialog;
    private BaseDialog baseDialog;
    private ReportDatePicker datePicker;

    private final SingleClickListener listener = new SingleClickListener() {
        @Override
        public void singleClick(View view) {
            filterReport();
        }
    };

    private final ReportDatePicker.DatePickerListener datePickerListener = new ReportDatePicker.DatePickerListener() {

        @Override
        public void onDatePicker(long s, long e) {
            sd = s;
            ed = e;
            filterReport();
        }
    };
    private final ReportClearDialog.ReportClearListener clearListener = new ReportClearDialog.ReportClearListener() {
        @Override
        public void clearType(int type) {
            if (baseDialog == null) {
                baseDialog = new BaseDialog(getContext());
            }
            baseDialog.hideTitle();
            baseDialog.setMsg(ResUtils.getString(R.string.clear_report_hint));
            baseDialog.setLeftButton(ResUtils.getString(R.string.cancel), null);
            baseDialog.setRightButton(ResUtils.getString(R.string.define), () -> {
                showLoadingDialog();
                Calendar calendar = Calendar.getInstance();
                switch (type) {
                    case 0:
                        PdiReportPresenter.clearByTime(System.currentTimeMillis());
                        break;

                    case 1:
                        GregorianCalendar keepMonth = new GregorianCalendar(Locale.CHINA);
                        keepMonth.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                        keepMonth.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                        keepMonth.set(Calendar.DAY_OF_MONTH, 1);
                        keepMonth.set(Calendar.HOUR_OF_DAY, 0);
                        keepMonth.set(Calendar.MINUTE, 0);
                        keepMonth.set(Calendar.SECOND, 0);
                        keepMonth.set(Calendar.MILLISECOND, 0);
                        long monthMillis = keepMonth.getTimeInMillis();
                        PdiReportPresenter.clearByTime(monthMillis);
                        break;

                    case 2:
                        GregorianCalendar keepWeek = new GregorianCalendar(Locale.CHINA);
                        keepWeek.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                        keepWeek.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                        keepWeek.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH));
                        keepWeek.set(Calendar.DAY_OF_WEEK, 1);
                        keepWeek.set(Calendar.HOUR_OF_DAY, 0);
                        keepWeek.set(Calendar.MINUTE, 0);
                        keepWeek.set(Calendar.SECOND, 0);
                        keepWeek.set(Calendar.MILLISECOND, 0);
                        long weekMillis = keepWeek.getTimeInMillis();
                        PdiReportPresenter.clearByTime(weekMillis);
                        break;

                    case 3:
                        GregorianCalendar keepDay = new GregorianCalendar(Locale.CHINA);
                        keepDay.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                        keepDay.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                        keepDay.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                        keepDay.set(Calendar.HOUR_OF_DAY, 0);
                        keepDay.set(Calendar.MINUTE, 0);
                        keepDay.set(Calendar.SECOND, 0);
                        keepDay.set(Calendar.MILLISECOND, 0);
                        long dayMillis = keepDay.getTimeInMillis();
                        PdiReportPresenter.clearByTime(dayMillis);
                        break;
                }
                filterReport();
            });
            baseDialog.show();

        }
    };

    private final IndexReportFunListener funListener = new IndexReportFunListener() {

        @Override
        public void clear(View view) {
            if (clearDialog == null) {
                clearDialog = new ReportClearDialog(getContext(), clearListener);
            }
            clearDialog.show();
        }

        @Override
        public void calendar(View view) {
            if (datePicker == null) {
                datePicker = new ReportDatePicker(getContext(), datePickerListener);
            }
            datePicker.show();
        }

        @Override
        public void resultClick(View result) {
            int temp = Integer.parseInt((String) result.getTag());
            if (resultFlag != temp) {
                resultFlag = temp;
                binding.setResultFilter(resultFlag);
            }
        }

        @Override
        public void statusClick(View statusView) {
            int temp = Integer.parseInt((String) statusView.getTag());
            if (statusFlag != temp) {
                statusFlag = temp;
                binding.setStatusFilter(statusFlag);
            }
        }

        @Override
        public void modelClick(View modelView) {
            if (modelView instanceof TextView) {
                String temp = ((TextView) modelView).getHint().toString();
                if (!TextUtils.equals(temp, modelFlag)) {
                    modelFlag = temp;
                    binding.modelFlow.setModel(modelFlag);
                }
            }

        }
    };

    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragIndexReportBinding.inflate(inflater, container, false);
            headNormal = binding.headNormal;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_index_report;
    }

    @Override
    protected void initView() {
        binding.setReportTitle(ResUtils.getString(R.string.main_report));
        headNormal.setHead(ResUtils.getString(R.string.fast_query));
        binding.setResultFilter(resultFlag);
        binding.setStatusFilter(statusFlag);
        binding.setListener(listener);

        initFlow();

        binding.search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtils.hideSoftInput(v);
                key = binding.search.getText();
                filterReport();
                return true;
            }
            return false;
        });
        binding.del.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            key = CharVar.EMPTY;
            binding.search.setText(key);
            binding.search.clearFocus();
            filterReport();
        });
        binding.search.addTextChangedListener(new ETextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.del.setVisibility(View.VISIBLE);
                } else {
                    binding.del.setVisibility(View.GONE);
                }
            }
        });
        binding.setFunListener(funListener);
        binding.dateClear.setOnClickListener(v -> {
            sd = ed = 0L;
            DSUtils.get().putLong(SPKEY.REPORT_START, sd);
            DSUtils.get().putLong(SPKEY.REPORT_END, ed);
            filterReport();
        });

        sd = DSUtils.get().getLong(SPKEY.REPORT_START);
        ed = DSUtils.get().getLong(SPKEY.REPORT_END);
        filterReport();
    }

    private void initFlow() {
        ItemIndexReportFilterModelBinding allText = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_index_report_filter_model, binding.modelFlow, false);
        allText.item.setHint(CharVar.EMPTY);
        allText.item.setText(ResUtils.getString(R.string.all));
        allText.setFunListener(funListener);
        binding.modelFlow.addView(allText.getRoot());

        String[] models = RoomUtils.getPdiDB().getCheckPlanDao().loadModel();
        for (String model : models) {
            ItemIndexReportFilterModelBinding modelText = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_index_report_filter_model, binding.modelFlow, false);
            modelText.item.setHint(model);
            modelText.item.setText(model);
            modelText.setFunListener(funListener);
            binding.modelFlow.addView(modelText.getRoot());
        }
        binding.modelFlow.setModel(modelFlag);
    }

    /**
     * 报告筛选
     */
    private void filterReport() {
        showLoadingDialog();

        if (sd > 0 && ed > 0) {
            binding.dateLayout.setVisibility(View.VISIBLE);
            DateFormat df = DateFormat.getDateInstance();
            binding.reportDate.setText(String.format("%s--%s", df.format(sd), df.format(ed)));
        } else {
            binding.dateLayout.setVisibility(View.GONE);
        }
        String searchWord = TextUtils.isEmpty(key) ? CharVar.EMPTY : CharVar.PERCENT + key + CharVar.PERCENT;
        AppDatabase.get()
                .getReportDao()
                .loadByCondition(resultFlag, statusFlag, modelFlag, sd, ed, searchWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<PdiReport>>() {


                    @Override
                    public void onNext(List<PdiReport> pdiReports) {
                        super.onNext(pdiReports);
                        reports = pdiReports;
                        if (groupAdapter == null) {
                            groupAdapter = new ReportGroupAdapter(reports);
                            LinearLayoutManager manager = new LinearLayoutManager(getContext());
                            binding.reportGroup.setLayoutManager(manager);
                            binding.reportGroup.setAdapter(groupAdapter);
                        } else {
                            groupAdapter.setReports(reports);
                            groupAdapter.notifyDataSetChanged();
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissDialog();
                    }

                });


    }
}
