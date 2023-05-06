package com.eucleia.pdicheck.net.presenter;

import com.eucleia.pdicheck.net.FileCallBack;
import com.eucleia.pdicheck.net.NetAPI;
import com.eucleia.pdicheck.net.mvpview.PDFStatusMvpView;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.tabscanap.util.ELogUtils;
import com.itextpdf.text.pdf.codec.Base64;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.ArrayList;

/**
 * PDF上传
 */
public class FileUploadPresenter extends BasePresenterImpl<PDFStatusMvpView> {
    private static class Instance {
        private final static FileUploadPresenter presenter = new FileUploadPresenter();
    }

    private FileUploadPresenter() {
    }

    public static FileUploadPresenter get() {
        return FileUploadPresenter.Instance.presenter;
    }


    public void upload(PdiReport report) {
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
                        FileUploadPresenter.get().completeUpload();
                    }

                    @Override
                    public void onError(String error) {
                        ELogUtils.e(error);
                        report.uploadStatus = 2;
                        AppDatabase.get().getReportDao().updateReport(report);
                        FileUploadPresenter.get().errorUpload(report);
                    }
                });
    }


    public void beginCreate() {
        ArrayList<PDFStatusMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).beginCreate();
        }

    }

    public void errorCreate() {
        ArrayList<PDFStatusMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).errorCreate();
        }
    }

    public void cancelUpload() {
        ArrayList<PDFStatusMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).cancelUpload();
        }
    }

    public void beginUpload() {
        ArrayList<PDFStatusMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).beginUpload();
        }

    }

    public void errorUpload(PdiReport report) {
        ArrayList<PDFStatusMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).errorUpload(report);
        }

    }

    public void completeUpload() {
        ArrayList<PDFStatusMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).completeUpload();
        }

    }


}
