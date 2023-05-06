package com.eucleia.pdicheck.net.mvpview;

import com.eucleia.pdicheck.room.entity.PdiReport;

public interface PDFStatusMvpView extends BaseMvpView {

    void beginCreate();

    void errorCreate();

    void cancelUpload();

    void beginUpload();

    void errorUpload(PdiReport report);

    void completeUpload();

}
