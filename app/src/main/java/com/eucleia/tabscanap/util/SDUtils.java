package com.eucleia.tabscanap.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.Utils;
import com.eucleia.BaseApp;
import com.eucleia.tabscanap.constant.Constant;
import com.eucleia.tabscanap.constant.FileVar;
import com.eucleia.tabscanap.constant.PathVar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;

/**
 * Created by xiehuigang on 2017/8/3.
 * 操作SD卡工具类
 */

public class SDUtils {

    public static boolean isFormating = false;


    /**
     * 获取内置外置SD绝对路径  is_removale:false 内置SD  is_removale：true 外置SD
     *
     * @param mContext
     * @param is_removale
     * @return
     */
    public static String getStoragePath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public static String getLocalPath() {
        return getLocalFile().getPath() + File.separator;
    }


    public static File getLocalFile() {
        return new File(Utils.getApp().getExternalCacheDir().getParentFile(), PathVar.tabScan);
    }

    /**
     * 拷贝SO文件至/data/data/com.eucleia.tabscanap/下
     *
     * @param
     * @return libDiag.so
     */
    public static String CopySoToInner(String oldPath, String folder, String model) {

        File libSdr = Utils.getApp().getDir(folder, Context.MODE_PRIVATE);
        String newPath = libSdr.getPath() + File.separator + model;
        try {
            File file = new File(newPath);
            if (!file.exists()) {
                file.mkdir();
            }
            if (oldPath.endsWith(FileVar.LIBDIAG_SO)) {
                File oldFile = new File(oldPath);
                File newFile = new File(newPath, FileVar.LIBDIAG_SO);
                copyFileUsingFileChannels(oldFile, newFile);
            } else {
                File oldFile = new File(oldPath, FileVar.LIBDIAG_SO);
                File newFile = new File(newPath, FileVar.LIBDIAG_SO);
                copyFileUsingFileChannels(oldFile, newFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newPath;
    }


    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            CloseUtils.closeIO(inputChannel);
            CloseUtils.closeIO(outputChannel);
        }
    }

}
