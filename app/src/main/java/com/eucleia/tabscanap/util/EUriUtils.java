package com.eucleia.tabscanap.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.blankj.utilcode.util.Utils;

public final class EUriUtils {

    private EUriUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * @param path
     * @return
     */
    public static Uri getUri(String path) {
        Uri mediaUri = MediaStore.Files.getContentUri("external");
        ContentResolver cr = Utils.getApp().getContentResolver();
        Cursor ca = cr.query(mediaUri, new String[]{MediaStore.MediaColumns._ID}, MediaStore.MediaColumns.DATA + "=?", new String[]{path}, null);
        if (ca != null && ca.moveToFirst()) {
            int _id = ca.getColumnIndex(MediaStore.MediaColumns._ID);
            int rowid = ca.getInt(_id);
            ca.close();
            return Uri.withAppendedPath(mediaUri, "" + rowid);
        } else {
            if (ca != null) {
                ca.close();
            }
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, path);
            return cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

    }

}
