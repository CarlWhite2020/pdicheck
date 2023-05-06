package com.eucleia.pdicheck.activity.normal;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.adapter.CheckGroupAdapter;
import com.eucleia.pdicheck.adapter.CheckInfoAdapter;
import com.eucleia.pdicheck.bean.normal.DiagView;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.databinding.ActCarCheckBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadBaseBinding;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.net.mvpview.PDFStatusMvpView;
import com.eucleia.pdicheck.net.presenter.FileUploadPresenter;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.pdicheck.widget.NoScrollManager;
import com.eucleia.tabscanap.bean.diag.BaseBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispPdiCheckBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.dialog.BaseDialog;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.Pdf5Utils;
import com.eucleia.tabscanap.util.ResUtils;

/**
 * 车辆检测
 */
public class CarCheckActivity extends BlueBaseActivity {

    private ActCarCheckBinding binding;
    private LayoutHeadBaseBinding headBase;
    private Animation rotateAnim = AnimationUtils.loadAnimation(Utils.getApp(), R.anim.rotate_always);

    private final SingleClickListener startOrStopListener = new SingleClickListener() {
        @Override
        public void singleClick(View view) {
            binding.bottom.setEnabled(false);
            setEventFlag(1);
        }
    };
    private final PDFStatusMvpView pdfStatusMvpView = new PDFStatusMvpView() {
        @Override
        public void beginCreate() {
            showPdfStatus(ResUtils.getString(R.string.pdf_creating), R.drawable.ic_refresh, false, true);
        }

        @Override
        public void errorCreate() {
            showPdfStatus(ResUtils.getString(R.string.pdf_create_fail), R.drawable.ic_fail, false, false);

        }

        @Override
        public void cancelUpload() {
            showPdfStatus(ResUtils.getString(R.string.pdf_upload_cancel), R.drawable.ic_warn, false, false);
        }

        @Override
        public void beginUpload() {
            showPdfStatus(ResUtils.getString(R.string.pdf_uploading), R.drawable.ic_refresh, false, true);
        }

        @Override
        public void errorUpload(PdiReport report) {
            showPdfStatus(ResUtils.getString(R.string.pdf_upload_fail), R.drawable.ic_fail, true, false);
            binding.statusRetry.setOnClickListener(v -> {
                FileUploadPresenter.get().beginUpload();
                FileUploadPresenter.get().upload(report);
            });
        }

        @Override
        public void completeUpload() {
            showPdfStatus(ResUtils.getString(R.string.pdf_upload_success), R.drawable.ic_success, false, false);
        }
    };

    private CheckGroupAdapter groupAdapter = new CheckGroupAdapter();
    private CheckInfoAdapter infoAdapter = new CheckInfoAdapter();

    @Override
    protected ViewDataBinding bindAct() {
        binding = ActCarCheckBinding.inflate(getLayoutInflater());
        headBase = binding.headBase;
        return binding;
    }

    @Override
    protected boolean isDiagnosticAct() {
        return true;
    }


    @Override
    protected void initView() {

        FileUploadPresenter.get().attachView(pdfStatusMvpView);

        base.setBackText(ResUtils.getString(R.string.home_page));
        base.setVciStatus(AppVM.get().getVciStatus());
        base.setTitle(ResUtils.getString(R.string.car_check));
        base.setBackListener(headFunListener);
        base.setRightListener(rightFunListener);
        headBase.setHead(base);

        binding.info.setAdapter(infoAdapter);

        binding.groupRecycler.setLayoutManager(new NoScrollManager(getContext()));
        binding.groupRecycler.setAdapter(groupAdapter);

        binding.setListener(startOrStopListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUploadPresenter.get().detachView(pdfStatusMvpView);
    }

    @Override
    protected void vciChange(VciStatus vciStatus) {
        base.setVciStatus(vciStatus);
        headBase.setHead(base);
        if (vciStatus.isStatus() && vciStatus.getVciStatus() == 0) {
            setEventFlag(2);
            BaseDialog baseDialog = createBaseDialog();
            baseDialog.setTitles(ResUtils.getString(R.string.vci_disconnect));
            baseDialog.setMsg(ResUtils.getString(R.string.vci_disconnect_hint));
            baseDialog.setSingleButton(ResUtils.getString(R.string.define), () -> {
                setEventFlag(0);
            });
            baseDialog.show();
        }
    }

    @Override
    protected void OnBackClick() {
        createBaseDialog();
        baseDialog.hideTitle();
        baseDialog.setMsg(ResUtils.getString(R.string.is_exit_diag));
        baseDialog.setLeftButton(ResUtils.getString(R.string.cancel), () -> setEventFlag(3));
        baseDialog.setRightButton(ResUtils.getString(R.string.define), () -> setEventFlag(0));
        baseDialog.show();
    }

    @Override
    public void onBackPressed() {
        OnBackClick();
    }

    private void setEventFlag(int status) {
        DiagView value = AppVM.get().getDiagView();
        BaseBeanEvent event = EDiagUtils.get().getEvent(value);
        if (event != null) {
            switch (status) {
                case 0:
                    event.setBackFlag(CDispConstant.PageDiagnosticType.DF_ID_BACK);
                    event.lockAndSignalAll();
                    break;

                case 1:
                    if (event instanceof CDispPdiCheckBeanEvent) {
                        CDispPdiCheckBeanEvent pdiCheckBeanEvent = (CDispPdiCheckBeanEvent) event;
                        if (pdiCheckBeanEvent.getScanState() == CDispConstant.PageDiagnosticType.DF_SCAN_START) {
                            event.setBackFlag(CDispConstant.PageDiagnosticType.DF_ID_STOP);
                        } else if (pdiCheckBeanEvent.getScanState() == CDispConstant.PageDiagnosticType.DF_SCAN_END_OF_PAUSE) {
                            event.setBackFlag(CDispConstant.PageDiagnosticType.DF_ID_START);
                        } else if (pdiCheckBeanEvent.getScanState() == CDispConstant.PageDiagnosticType.DF_SCAN_END_OF_RESULT) {
                            event.setBackFlag(CDispConstant.PageDiagnosticType.DF_ID_OK);
                        }
                    }
                    event.lockAndSignalAll();
                    break;

                case 2:
                    event.setBackFlag(CDispConstant.PageDiagnosticType.DF_ID_STOP);
                    event.lockAndSignalAll();
                    break;

                case 3:
                    event.lockAndSignalAll();
                    break;

            }
        }
    }

    private void showPdfStatus(String hint, int res, boolean showError, boolean isRotate) {
        binding.progressHint.setVisibility(View.GONE);
        binding.progressLayout.setVisibility(View.GONE);
        binding.uploadHint.setVisibility(View.VISIBLE);
        if (showError) {
            binding.statusRetry.setVisibility(View.VISIBLE);
        } else {
            binding.statusRetry.setVisibility(View.GONE);
        }

        if (isRotate) {
            binding.statusImage.startAnimation(rotateAnim);
        } else {
            binding.statusImage.clearAnimation();
        }

        binding.statusText.setText(hint);
        binding.statusImage.setImageResource(res);
    }

    @Override
    protected PageType getPageType() {
        return PageType.Type_PdiCheck;
    }

    @Override
    protected void onDiagViewChange(DiagView diagView) {
        super.onDiagViewChange(diagView);
        if (diagView.getPageType() == PageType.Type_PdiCheck) {
            CDispPdiCheckBeanEvent event = (CDispPdiCheckBeanEvent) EDiagUtils.get().getEvent(diagView);
            if (event != null) {
                int progress = event.getPercent() * 100 / event.getAllPercent();
                binding.setProgress(progress);
                binding.setHint(event.getStrContext());

                if (infoAdapter != null) {
                    infoAdapter.setEvent(event);
                }

                if (event.getScanState() == CDispConstant.PageDiagnosticType.DF_SCAN_START) {
                    binding.bottom.setVisibility(View.VISIBLE);
                    binding.bottom.setText(ResUtils.getString(R.string.stop));
                } else if (event.getScanState() == CDispConstant.PageDiagnosticType.DF_SCAN_END_OF_PAUSE) {
                    binding.bottom.setVisibility(View.VISIBLE);
                    binding.bottom.setText(ResUtils.getString(R.string.start));
                } else if (event.getScanState() == CDispConstant.PageDiagnosticType.DF_SCAN_END_OF_RESULT
                        && event.isPdfSwitch()) {
                    if (event.getResult() == 1) {
                        binding.result.setImageResource(R.drawable.ic_qualified);
                    } else {
                        binding.result.setImageResource(R.drawable.ic_unqualified);
                    }
                    binding.result.setVisibility(View.VISIBLE);

                    binding.bottom.setVisibility(View.VISIBLE);
                    binding.bottom.setText(ResUtils.getString(R.string.define));
                    event.setPdfSwitch(false);
                    if (TextUtils.isEmpty(event.getVin())) {
                        event.setVin(EDiagUtils.get().getJniModel().vin);
                    }
                    binding.scrollView.scrollTo(0, 0);
                    Pdf5Utils.get().savePdf(event);
                } else {
                    binding.bottom.setVisibility(View.VISIBLE);
                    binding.bottom.setText(ResUtils.getString(R.string.define));
                }
                binding.bottom.setEnabled(true);

                binding.scrollView.setScrollBottom(event.isPdfSwitch());
                if (groupAdapter != null) {
                    groupAdapter.resetData(event.getGroups());
                }
            }

        }
    }
}
