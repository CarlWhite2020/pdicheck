package com.eucleia.pdicheck.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.dialog.BlueDialog;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.constant.RequestCode;
import com.eucleia.tabscanap.dialog.BaseDialog;
import com.eucleia.tabscanap.listener.BluetoothStatusListener;
import com.eucleia.tabscanap.util.BTUtils;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.PermissionPageUtils;
import com.eucleia.tabscanap.util.ResUtils;

public abstract class BlueBaseActivity extends BaseActivity implements BluetoothStatusListener {

    private static final int Time = 120;
    private BlueDialog blueDialog;
    protected SingleClickListener rightFunListener = view -> OnRightClick();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BTUtils.get().addBluetoothStatusListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (blueDialog != null) {
            blueDialog.dismiss();
        }
        BTUtils.get().removeBluetoothStatusListener(this);
    }

    protected void OnRightClick() {
        if (needVci()) return;
    }


    protected boolean needVci() {
        if (!BTConstant.isVciConnect) {
            BTConstant.isShowList = true;
            openBluetooth();
            return true;
        }
        return false;
    }

    /**
     * 请求打开蓝牙
     */
    public void openBluetooth() {
        BTUtils.get().register();
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            turnOnBluetooth();
        } else {
            openBlueSuccess();
        }
    }

    /**
     * 打开蓝牙
     */
    private void turnOnBluetooth() {
        Intent requestBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestBluetoothOn.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        requestBluetoothOn.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, Time);
        startActivityForResult(requestBluetoothOn, RequestCode.SEARCH_BLUE);
    }


    /**
     * 打开蓝牙成功
     */
    protected void openBlueSuccess() {
        if (BTConstant.isShowList) {
            beginSearch();
        } else {
            dismissLoading();
        }
    }

    /**
     * 打开蓝牙失败
     */
    protected void openBlueFail() {
        dismissLoading();
    }


    /**
     * 开始搜索蓝牙
     */
    protected void beginSearch() {
        BTUtils.get().startSearch();
        dismissLoading();
        if (getLifecycle().getCurrentState() != Lifecycle.State.DESTROYED
                && getLifecycle().getCurrentState() != Lifecycle.State.INITIALIZED
                && BTConstant.isShowList) {
            if (blueDialog == null) {
                blueDialog = new BlueDialog(getContext());
            }
            blueDialog.show();
        }
    }

    @Override
    public void btStatus(int status) {
        if (status == BluetoothAdapter.STATE_ON) {
            openBlueSuccess();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RequestCode.SEARCH_BLUE == requestCode) {
            if (resultCode == RESULT_CANCELED) {
                openBlueFail();
            } else {
                openBlueSuccess();
            }

        } else if (RequestCode.LOCATION == requestCode) {
            if (resultCode != RESULT_OK) {
                openBlueFail();
            } else {
                dialogPermission(ResUtils.getString(R.string.permission_location));
            }
        }
    }

    /**
     * 检测搜索权限
     */
    private void searchBlueTooth() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            searchBt(Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            searchBt(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }

    /**
     * 需要位置权限
     *
     * @param permission
     */
    private void searchBt(String... permission) {
        requestPermissions(permission, RequestCode.LOCATION);
    }


    /**
     * @param permission
     */
    private void dialogPermission(String permission) {
        dismissLoading();
        new BaseDialog(getContext())
                .setMsg(permission)
                .setRightButton(getString(R.string.define), () -> {
                    try {
                        PermissionPageUtils permissionPageUtils = new PermissionPageUtils(getContext());
                        Intent intent = permissionPageUtils.jumpPermissionPage();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ELogUtils.e(getString(R.string.permission_location));
                    }

                }).show();
    }


    private boolean checkPermission() {
        if (!isLocationEnabled()) {
            dismissLoading();
            new BaseDialog(getContext())
                    .setMsg(ResUtils.getString(R.string.service_location))
                    .setRightButton(ResUtils.getString(R.string.define), () -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }).show();
            return false;
        }
        return true;
    }

    public boolean isLocationEnabled() {
        int locationMode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }
        return true;
    }

    @Override
    protected void vciChange(VciStatus vciStatus) {
        super.vciChange(vciStatus);
        if (blueDialog != null && blueDialog.isShowing()
                && BTConstant.isVciConnect) {
            blueDialog.dismiss();
        }
    }
}
