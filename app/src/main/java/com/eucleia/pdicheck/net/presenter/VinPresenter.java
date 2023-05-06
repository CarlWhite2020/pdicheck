package com.eucleia.pdicheck.net.presenter;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.eucleia.pdicheck.net.mvpview.AutoVinMvpView;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.pdicheck.bean.constant.Constant;
import com.eucleia.tabscanap.jni.diagnostic.so.Communication;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import java.util.ArrayList;

public class VinPresenter extends BasePresenterImpl<AutoVinMvpView> {
    private static class INSTANCE {
        private static final VinPresenter vinPresenter = new VinPresenter();
    }

    private VinPresenter() {
    }

    public static VinPresenter get() {
        return INSTANCE.vinPresenter;
    }

    private volatile boolean isAutoVining = false;

    private VinAsyncTask vinAsyncTask;

    public void autoVin() {
        ELogUtils.v(isAutoVining);
        if (isAutoVining) {
            return;
        }
        isAutoVining = true;
        vinAsyncTask = new VinAsyncTask();
        vinAsyncTask.execute();
    }

    private class VinAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress(0,1);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            for (int i = 0; i < Constant.Protocols.length; i++) {
                if (!BTConstant.isVciConnect || !isAutoVining) {
                    ELogUtils.d("自动定位取消");
                    return 0;
                }
                publishProgress(i, Constant.Protocols.length);
                for (int j = 0; j < 2; j++) {
                    //每一个协议发两遍读取VIN
                    int Protocol = Constant.Protocols[i];
                    if (Protocol <= 8) {
                        EDiagUtils.get().getModel().vin = Communication.GetObdVinCode(Protocol, 1500);
                    } else {
                        EDiagUtils.get().getModel().vin = Communication.GetObdVinCode(Protocol, 300);
                    }
                }
                if (!TextUtils.isEmpty(EDiagUtils.get().getModel().vin)) {
                    break;
                }
                ELogUtils.d("第" + (i + 1) + "次OBD定位获取VIN码 : " + EDiagUtils.get().getModel().vin);
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress(values[0],values[1]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            isAutoVining = false;
            complete();
        }
    }


    public void progress(int progress, int max) {
        ArrayList<AutoVinMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).onProgress(progress, max);
        }
    }

    public void complete() {
        ArrayList<AutoVinMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).onComplete();
        }
    }

}
