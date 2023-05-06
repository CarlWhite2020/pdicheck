package com.eucleia.pdicheck.net.presenter;

import android.os.SystemClock;

import com.blankj.utilcode.util.FileUtils;
import com.eucleia.BaseApp;
import com.eucleia.pdicheck.listener.BaseSingleListener;
import com.eucleia.pdicheck.net.FileCallBack;
import com.eucleia.pdicheck.net.NetAPI;
import com.eucleia.pdicheck.net.presenter.FileUploadPresenter;
import com.eucleia.pdicheck.room.dao.ReportDao;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.tabscanap.util.ArraysUtils;
import com.eucleia.tabscanap.util.BaseObserver;
import com.eucleia.tabscanap.util.ELogUtils;
import com.itextpdf.text.pdf.codec.Base64;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PdiReportPresenter extends BasePresenterImpl {
    public static volatile boolean isUploadError;
    private static volatile boolean isStart;

    public static void clearByTime(long time) {
        Observable.create(subscriber -> {
            ReportDao reportDao = AppDatabase.get().getReportDao();
            List<String> paths = reportDao.queryByTime(time);
            if (!ArraysUtils.isEmpty(paths)) {
                for (String path : paths) {
                    FileUtils.delete(path);
                }
            }
            reportDao.clearByTime(time);

        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();

    }

    public static void upLoad(PdiReport errorReport, BaseSingleListener listener) {
        Observable.create(subscriber -> {
            isUploadError = true;
            isStart = true;
            upload(errorReport);
            while (isStart) {
                SystemClock.sleep(1000);
            }
            subscriber.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isUploadError = false;
                        if (listener != null) {
                            listener.complete();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        isUploadError = false;
                        if (listener != null) {
                            listener.complete();
                        }
                    }
                });
    }

    public static void upLoad(List<PdiReport> errorReports, BaseSingleListener listener) {
        Observable.create(subscriber -> {
            isUploadError = true;
            for (PdiReport report : errorReports) {
                isStart = true;
                upload(report);
                while (isStart) {
                    SystemClock.sleep(1000);
                }
            }
            subscriber.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isUploadError = false;
                        if (listener != null) {
                            listener.complete();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        isUploadError = false;
                        if (listener != null) {
                            listener.complete();
                        }
                    }
                });
    }

    private static void upload(PdiReport report) {
        HttpParams params = new HttpParams();
        String vin = report.vin;
        params.put("vin", vin);
        params.put("fileName", report.fileName);
        params.put("file", Base64.encodeFromFile(report.path));
        OkGo.post(NetAPI.getAPI(NetAPI.getPdfUpload()))
                .params(params)
                .isMultipart(true)
                .execute(new FileCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        ELogUtils.d(result);
                        report.uploadStatus = 1;
                        AppDatabase.get().getReportDao().updateReport(report);
                        isStart = false;
                    }

                    @Override
                    public void onError(String error) {
                        ELogUtils.e(error);
                        report.uploadStatus = 2;
                        AppDatabase.get().getReportDao().updateReport(report);
                        isStart = false;
                    }
                });
    }
}
