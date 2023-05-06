package com.eucleia.pdicheck.fragment.normal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.AppUtils;
import com.eucleia.pdicheck.BuildConfig;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.normal.ChooseCarActivity;
import com.eucleia.pdicheck.activity.normal.InputVinActivity;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.databinding.FragIndexHomeBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadBaseBinding;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.pdicheck.listener.BaseSingleListener;
import com.eucleia.pdicheck.listener.IndexHomeFunListener;
import com.eucleia.pdicheck.net.BaseCallBack;
import com.eucleia.pdicheck.net.presenter.PdiReportPresenter;
import com.eucleia.pdicheck.net.presenter.VinPresenter;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.dialog.BaseDialog;
import com.eucleia.tabscanap.util.BaseObserver;
import com.eucleia.tabscanap.util.ResUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 主页
 */
public class IndexHomeFragment extends BaseFragment {

    private FragIndexHomeBinding binding;
    private LayoutHeadBaseBinding headBase;
    private PdiReport errorReports;

    private void testHttp(){
        OkGo.post("https://www.fordtechservice.dealerconnection.com/vdirs/wds/PCMReprogram/DSFM_DownloadFile.asp")
                .params("filename","BM5T-14C025-AB.vbf")
                .headers(HttpHeaders.HEAD_KEY_ACCEPT_ENCODING,HttpHeaders.HEAD_VALUE_ACCEPT_ENCODING)
                .execute(new BaseCallBack() {
                    @Override
                    public void onSuccess(String result) {

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    final private IndexHomeFunListener listener = new IndexHomeFunListener() {
        @Override
        public void autoVin(View view) {
            if (BuildConfig.DEBUG) {
                testHttp();
                return;
            }
            if (needVci()) return;
            VinPresenter.get().autoVin();
        }

        @Override
        public void inputVin(View view) {
            if (needVci()) return;
            startActivity(InputVinActivity.class);
        }

        @Override
        public void chooseCar(View view) {
            if (needVci()) return;
            startActivity(ChooseCarActivity.class);
        }

    };


    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragIndexHomeBinding.inflate(inflater, container, false);
            headBase = binding.headBase;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_index_home;
    }


    @Override
    protected void initLayout() {
        base.setBackText(ResUtils.getString(R.string.home_page));
        base.setVciStatus(AppVM.get().getVciStatus());
        base.setTitle(AppUtils.getAppName());
        base.setBackVisibility(false);
        base.setRightListener(view -> {
            needVci();
        });
        headBase.setHead(base);
        binding.setListener(listener);
        binding.uploadFail.setOnClickListener(v -> {
            BaseDialog baseDialog = getBaseDialog();
            if (baseDialog != null) {
                baseDialog.setTitles(ResUtils.getString(R.string.retry_upload));
                baseDialog.setMsg(getString(R.string.retry_upload_hint));
                baseDialog.setLeftButton(ResUtils.getString(R.string.cancel), null);
                baseDialog.setRightButton(ResUtils.getString(R.string.define), () -> {
                    binding.uploadFail.setVisibility(View.GONE);
                    PdiReportPresenter.upLoad(errorReports, new BaseSingleListener() {
                        @Override
                        public void complete() {
                            judgeUploadStatus();
                        }
                    });
                });
                baseDialog.show();
            }


        });
    }

    @Override
    protected void reLoadFrag() {
    }


    @Override
    public void onResume() {
        super.onResume();
        judgeUploadStatus();
    }

    private void judgeUploadStatus() {
        AppDatabase.get().getReportDao().queryLast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<PdiReport>() {
                    @Override
                    public void onNext(PdiReport pdiReports) {
                        super.onNext(pdiReports);
                        errorReports = pdiReports;
                        if (errorReports == null || errorReports.uploadStatus != 2 || PdiReportPresenter.isUploadError) {
                            binding.uploadFail.setVisibility(View.GONE);
                        } else {
                            binding.uploadFail.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    @Override
    protected void vciChange(VciStatus vciStatus) {
        base.setVciStatus(vciStatus);
        headBase.setHead(base);
    }
}
