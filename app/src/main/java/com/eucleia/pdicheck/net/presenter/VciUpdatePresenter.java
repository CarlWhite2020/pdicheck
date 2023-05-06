package com.eucleia.pdicheck.net.presenter;

import android.os.AsyncTask;
import android.os.SystemClock;

import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.net.mvpview.VciUpdateMvpView;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.constant.SizeVar;
import com.eucleia.tabscanap.jni.diagnostic.so.Communication;
import com.eucleia.tabscanap.util.BTUtils;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.JNIUtils;
import com.eucleia.tabscanap.util.ResUtils;

import java.io.InputStream;
import java.util.ArrayList;

public class VciUpdatePresenter extends BasePresenterImpl<VciUpdateMvpView> {

    private volatile boolean vciUpdating = false;

    private static class Instance {
        private final static VciUpdatePresenter presenter = new VciUpdatePresenter();
    }

    private VciUpdatePresenter() {
    }

    public static VciUpdatePresenter get() {
        return VciUpdatePresenter.Instance.presenter;
    }

    public void start() {
        if (vciUpdating) return;
        MyVCITask vciTask = new MyVCITask();
        vciTask.execute();
    }

    private class MyVCITask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            vciUpdating = true;
            notifyStart();
        }

        /**
         * @param params
         * @return
         */
        @Override
        protected Integer doInBackground(String... params) {
            try {
                ELogUtils.d("VCI正式开始升级");
                // 先拿到bin文件所带的版本号
                InputStream myInput = Utils.getApp().getAssets().open("firmware/vci.bin");
                byte[] data1 = new byte[64];
                myInput.read(data1);
                // 文件总长度，byte[]数组型
                byte[] TotalSizebyte = new byte[4];
                // 由于是bin文件是大端方式，而通信层是小端小式解析，所以，要调转一下
                TotalSizebyte[0] = data1[35];
                TotalSizebyte[1] = data1[34];
                TotalSizebyte[2] = data1[33];
                TotalSizebyte[3] = data1[32];

                // 较验和
                byte[] checkSumByte = new byte[4];
                // 由于是bin文件是大端方式，而通信层是小端小式解析，所以，要调转一下
                checkSumByte[0] = data1[39];
                checkSumByte[1] = data1[38];
                checkSumByte[2] = data1[37];
                checkSumByte[3] = data1[36];

                int checkSum = JNIUtils.byte2Int(checkSumByte, 0);

                int TotalSize = JNIUtils.byte2Int(TotalSizebyte, 0);

                ELogUtils.d("等待下位机复位");
                if (!Communication.IsVciInBootMode()) {
                    Communication.SetUpdataMode(0);
                }

                //主动将下位机断开标识为断开
                BTConstant.isVciConnect = false;
                for (int icount = 0; icount < 30; icount++) {
                    // 给一点时间给下位机复位
                    SystemClock.sleep(1000);
                    if (BTConstant.isVciConnect && Communication.IsVciInBootMode()) {
                        break;
                    }
                }

                int readSize = 1024;// 每次读取数据的长度
                byte[] buffer = new byte[SizeVar.BYTE_ARR_SIZE];
                byte[] length = JNIUtils.int2Byte(20 + readSize); // 这一包的总长度
                byte[] type = JNIUtils.int2Byte(0);// 类型
                int PackSize = 0;// 这一包发送的“升级数据”的长度
                int backNum = 0;// 发送的是第几包
                int total = 0; // 当前已发送的升级数据总和
                while ((PackSize = myInput.read(buffer)) != -1) {
                    total += PackSize;
                    byte[] sendData = new byte[20 + readSize];

                    byte[] PackSizeByte = JNIUtils.int2Byte(PackSize);
                    byte[] backNumByte = JNIUtils.int2Byte(backNum);

                    System.arraycopy(length, 0, sendData, 0, 4);
                    System.arraycopy(type, 0, sendData, 4, 4);
                    System.arraycopy(TotalSizebyte, 0, sendData, 8, 4);
                    System.arraycopy(PackSizeByte, 0, sendData, 12, 4);
                    System.arraycopy(backNumByte, 0, sendData, 16, 4);
                    System.arraycopy(buffer, 0, sendData, 20, readSize);

                    for (int inum = 0; ; inum++) {
                        if (Communication.Download(sendData) == 0) {
                            publishProgress((int) (total * 100 / TotalSize));
                            break;
                        }
                        ELogUtils.e("VCI升级包数：" + backNum + " -- 尝试传递次数：" + inum);
                        if (inum == 2) {
                            myInput.close();
                            return 0;
                        }
                    }
                    ++backNum;
                }

                int result = Communication.SetUpdateCheckSum(checkSum, TotalSize);
                myInput.close();
                if (result != 0) {
                    ELogUtils.e("较验和异常");
                    return 0;
                }
                Communication.SetUpdataMode(1);

            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            BTConstant.isVciConnect = false;
            BTUtils.get().disConnect();
            SystemClock.sleep(4000);
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int value = values[0];
            ELogUtils.d("VCI升级进度刷新 " + value);
            notifyProgress(value, ResUtils.getString(R.string.vci_updating));
        }

        /**
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {
            vciUpdating = true;
            if (result == 0) {
                ELogUtils.d("下位机升级失败------------");
                Communication.SetUpdataMode(1);
                BTConstant.isVciConnect = false;
                notifyError();
            } else {
                /** 升级完成 发送最后一条消息 */
                ELogUtils.d("VCI升级完成");
                notifySuccess();
            }
        }
    }

    private void notifySuccess() {
        ArrayList<VciUpdateMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).notifySuccess();
        }
    }

    private void notifyError() {
        ArrayList<VciUpdateMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).notifyError();
        }
    }

    private void notifyProgress(int progress, String content) {
        ArrayList<VciUpdateMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).notifyProgress(progress, content);
        }
    }

    private void notifyStart() {
        ArrayList<VciUpdateMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).notifyStart();
        }
    }

}
