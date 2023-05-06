package com.eucleia.pdicheck.activity.normal;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.AppUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.adapter.MainPagerAdapter;
import com.eucleia.pdicheck.bean.normal.ModelYear;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.databinding.ActMainBinding;
import com.eucleia.pdicheck.databinding.LayoutIndexTabBinding;
import com.eucleia.pdicheck.dialog.AppUpdateDialog;
import com.eucleia.pdicheck.fragment.normal.IndexHomeFragment;
import com.eucleia.pdicheck.fragment.normal.IndexMineFragment;
import com.eucleia.pdicheck.fragment.normal.IndexReportFragment;
import com.eucleia.pdicheck.listener.DownloadListener;
import com.eucleia.pdicheck.net.model.AppVersion;
import com.eucleia.pdicheck.net.mvpview.AutoVinMvpView;
import com.eucleia.pdicheck.net.mvpview.UpdateAppMvpView;
import com.eucleia.pdicheck.net.mvpview.VciUpdateMvpView;
import com.eucleia.pdicheck.net.presenter.UpdateAppPresenter;
import com.eucleia.pdicheck.net.presenter.VciUpdatePresenter;
import com.eucleia.pdicheck.net.presenter.VinPresenter;
import com.eucleia.pdicheck.room.database.PDIDatabase;
import com.eucleia.tabscanap.bean.model.DiagModel;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.PathVar;
import com.eucleia.tabscanap.dialog.BaseDialog;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.EToastUtils;
import com.eucleia.tabscanap.util.ResUtils;
import com.eucleia.tabscanap.util.SDUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
public class MainActivity extends BlueBaseActivity implements View.OnClickListener {

    private ActMainBinding binding;
    private LayoutIndexTabBinding tabBinding;
    private AppUpdateDialog appUpdateDialog;
    private BaseDialog takeVciDialog;

    private final ArrayList<Fragment> fragments = new ArrayList<>();

    //app下载回调
    private final DownloadListener listener = new DownloadListener() {
        @Override
        public void download(String url, String name) {
            File path = new File(SDUtils.getLocalFile(), PathVar.downloadPath);
            showProgress(0);
            GetRequest<File> fileGetRequest = OkGo.get(url);
            fileGetRequest.cacheKey(name).cacheMode(CacheMode.IF_NONE_CACHE_REQUEST).execute(new FileCallback(path.getPath(), name) {
                @Override
                public void onSuccess(Response<File> response) {
                    dismissProgress();
                    if (response.isSuccessful()) {
                        File apkFile = response.body();
                        AppUtils.installApp(apkFile);
                    } else {
                        EToastUtils.get().showShort(R.string.install_error, false);
                    }
                }

                @Override
                public void downloadProgress(Progress progress) {
                    super.downloadProgress(progress);
                    showProgress((int) (progress.currentSize * 100 / progress.totalSize),
                            ResUtils.getString(R.string.app_updating));
                }
            });
        }
    };

    //固件升级回调
    private final VciUpdateMvpView vciUpdateMvpView = new VciUpdateMvpView() {
        @Override
        public void notifySuccess() {
            EToastUtils.get().showShort(R.string.vci_update_success, true);
            dismissProgress();
        }

        @Override
        public void notifyError() {
            EToastUtils.get().showShort(R.string.vci_update_fail, false);
            dismissProgress();
        }

        @Override
        public void notifyProgress(int progress, String content) {
            showProgress(progress, content);
        }

        @Override
        public void notifyStart() {
            showProgress(0, ResUtils.getString(R.string.vci_updating));
        }
    };

    //app更新请求回调
    private final UpdateAppMvpView updateAppMvpView = new UpdateAppMvpView() {
        @Override
        public void updateApkSucess(AppVersion appVersion) {
            dismissLoading();
            if (appVersion.getVersionCode() > AppUtils.getAppVersionCode()) {
                if (appUpdateDialog == null) {
                    appUpdateDialog = new AppUpdateDialog(getContext(), appVersion, listener);
                }
                appUpdateDialog.show();
            } else {
                EToastUtils.get().showShort(R.string.latest_version_of_apk, true);
            }
        }

        @Override
        public void updateNotData(String errroMsg) {
            dismissLoading();
            EToastUtils.get().showShort(errroMsg, false);
        }

        @Override
        public void updateError(String errroMsg) {
            dismissLoading();
            EToastUtils.get().showShort(errroMsg, false);

        }
    };

    //自动定位回调
    private final AutoVinMvpView autoVinMvpView = new AutoVinMvpView() {
        @Override
        public void onProgress(int progress, int max) {
            int p = progress * 100 / max;
            showProgress(p, ResUtils.getString(R.string.read_vin));
        }

        @Override
        public void onComplete() {
            dismissProgress();
            String vin = EDiagUtils.get().getModel().vin;
            if (TextUtils.isEmpty(vin)) {
                EToastUtils.get().showShort(R.string.autovin_fail, false);
                return;
            }
            List<ModelYear> modelYears = PDIDatabase.get().getCheckPlanDao().loadModelYear();
            if (modelYears != null && modelYears.size() > 0) {
                ModelYear modelYear = modelYears.get(0);
                EDiagUtils.get().getModel().year = String.valueOf(modelYear.year);
                EDiagUtils.get().getModel().model = modelYear.model;
            }
            DiagModel model = EDiagUtils.get().getModel();
            model.entranceMode = CDispConstant.Enum_Entrance_Mode.ENTER_MODE_PDICHECK_AUTOVIN;
            startActivity(CarInfoActivity.class);
        }
    };

    @Override
    protected ViewDataBinding bindAct() {
        binding = ActMainBinding.inflate(getLayoutInflater());
        tabBinding = binding.indexTab;
        return binding;
    }



    @Override
    protected void initView() {
        UpdateAppPresenter.get().attachView(updateAppMvpView);
        VciUpdatePresenter.get().attachView(vciUpdateMvpView);
        VinPresenter.get().attachView(autoVinMvpView);

        tabBinding.homeTab.setOnClickListener(this);
        tabBinding.reportTab.setOnClickListener(this);
        tabBinding.mineTab.setOnClickListener(this);

        fragments.add(new IndexHomeFragment());
        fragments.add(new IndexReportFragment());
        fragments.add(new IndexMineFragment());


        MainPagerAdapter adapter = new MainPagerAdapter(this, fragments);
        binding.viewpager.setUserInputEnabled(false);
        binding.viewpager.setAdapter(adapter);

        onClick(tabBinding.homeTab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EDiagUtils.get().isNeedShowTakeDialog()) {
            showTakeDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateAppPresenter.get().detachView(updateAppMvpView);
        VinPresenter.get().detachView(autoVinMvpView);
    }


    @Override
    public void onClick(View v) {
        tabBinding.homeTab.setSelected(false);
        tabBinding.reportTab.setSelected(false);
        tabBinding.mineTab.setSelected(false);
        v.setSelected(true);
        if (v == tabBinding.homeTab) {
            binding.viewpager.setCurrentItem(0);
        } else if (v == tabBinding.reportTab) {
            binding.viewpager.setCurrentItem(1);
        } else if (v == tabBinding.mineTab) {
            binding.viewpager.setCurrentItem(2);
        }
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    /**
     * 显示退出诊断 拔走VCI提示.
     */
    private void showTakeDialog() {
        ELogUtils.d();
        EDiagUtils.get().setNeedShowTakeDialog(false);
        if (takeVciDialog == null) {
            takeVciDialog = new BaseDialog(getContext());
        }
        takeVciDialog.setTitles(ResUtils.getString(R.string.tip))
                .setMsg(ResUtils.getString(R.string.take_vci_hint))
                .setSingleButton(ResUtils.getString(R.string.define), null)
                .show();
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.take_vci);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void vciChange(VciStatus vciStatus) {
        super.vciChange(vciStatus);
        if (!BTConstant.isVciConnect) {
            if (takeVciDialog != null) {
                takeVciDialog.dismiss();
                takeVciDialog = null;
            }
        }
    }
}
