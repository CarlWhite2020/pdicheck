package com.eucleia.tabscanap.util;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.Utils;
import com.eucleia.tabscanap.constant.SizeVar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Assets 工具类.
 */
public final class AssetsUtils {

    private AssetsUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 把Assets文件拷贝到本机.
     *
     * @param assetsDir Assets目录
     * @param localPath 本地目录
     */
    public static void copyAssetsFile(final String assetsDir,
                                      final String localPath) {
        InputStream mIs = null;
        FileOutputStream fos = null;
        try {
            String[] dirs = Utils.getApp().getAssets().list(assetsDir);
            if (dirs == null || dirs.length == 0) {
                mIs = Utils.getApp().getAssets().open(assetsDir);
                byte[] mByte = new byte[SizeVar.BYTE_ARR_SIZE];
                int bt;
                File file = new File(localPath);
                boolean newFile = file.createNewFile();
                fos = new FileOutputStream(file);
                while ((bt = mIs.read(mByte)) != -1) {
                    fos.write(mByte, 0, bt);
                }
                fos.flush();
            } else {
                File file = new File(localPath);
                if (!file.exists()) {
                    boolean mkdirs = file.mkdirs();
                }
                for (String childDir : dirs) {
                    copyAssetsFile(assetsDir + File.separator + childDir,
                            localPath + File.separator + childDir);
                }
            }
        } catch (Exception e) {
            ELogUtils.v(e.getMessage());
        } finally {
            CloseUtils.closeIO(mIs);
            CloseUtils.closeIO(fos);
        }
    }
}
