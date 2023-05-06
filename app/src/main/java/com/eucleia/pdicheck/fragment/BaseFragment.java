package com.eucleia.pdicheck.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.ActivityUtils;
import com.eucleia.pdicheck.activity.BaseActivity;
import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.bean.normal.HeadBase;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.dialog.BaseDialog;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * frag 基类
 */
public abstract class BaseFragment extends Fragment {

    public Context mContext;
    public Activity mActivity;
    protected boolean isAgainLoad;
    /**
     * Fragment当前状态是否可见
     */
    public boolean isVisible;
    protected boolean isOpenBus = false;

    protected HeadBase base = new HeadBase();


    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        setModel();
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding binding = bindFrag(inflater, container);
        if (isOpenBus) {
            EventBus.getDefault().register(this);
        }
        if (!isAgainLoad) {
            isAgainLoad = true;
            initLayout();
        } else {
            reLoadFrag();
        }
        return binding.getRoot();
    }

    protected abstract ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onResume() {
        super.onResume();
        if (closeDialog()) {
            dismissDialog();
        }
    }

    protected void setModel() {
        AppVM.get().getVciStatusLiveData().observe(this, new Observer<VciStatus>() {
            @Override
            public void onChanged(VciStatus vciStatus) {
                vciChange(vciStatus);
            }
        });
    }

    protected void vciChange(VciStatus vciStatus) {

    }

    protected boolean closeDialog() {
        return false;
    }

    /**
     * 再次初始化布局（非第一次）
     */
    protected void reLoadFrag() {
        ELogUtils.v(this.toString());
    }


    /**
     * 第一次初始化布局
     */
    protected void initLayout() {
        ELogUtils.v(toString());
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initView();
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    public void onVisible() {
        ELogUtils.v(toString());
    }


    /**
     * 不可见
     */
    public void onInvisible() {
        ELogUtils.v(toString());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isOpenBus) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void startActivity(Class clazz) {
        startActivity(clazz, false);
    }

    //跳转到新的activity
    public void startActivity(Class clazz, boolean finish) {
        ActivityUtils.startActivity(clazz);
        if (finish) {
            mActivity.finish();
        }
    }


    public abstract int getLayoutId();

    protected void initView() {

    }

    protected boolean needVci() {
        if (!BTConstant.isVciConnect) {
            BlueBaseActivity activity = (BlueBaseActivity) getActivity();
            if (activity != null) {
                BTConstant.isShowList = true;
                activity.openBluetooth();
            }
            return true;
        }
        return false;
    }


    protected BaseDialog getBaseDialog() {
        BaseActivity topActivity = (BaseActivity) ActivityUtils.getTopActivity();
        if (topActivity != null) {
            return topActivity.createBaseDialog();
        }
        return null;
    }


    /**
     * 显示普通加载弹窗
     */
    protected void showLoadingDialog() {
        BaseActivity topActivity = (BaseActivity) ActivityUtils.getTopActivity();
        if (topActivity != null) {
            topActivity.showLoading();
        }
    }


    /**
     * 隐藏普通加载弹窗
     */
    protected void dismissDialog() {
        BaseActivity topActivity = (BaseActivity) ActivityUtils.getTopActivity();
        if (topActivity != null) {
            topActivity.dismissLoading();
        }
    }

}