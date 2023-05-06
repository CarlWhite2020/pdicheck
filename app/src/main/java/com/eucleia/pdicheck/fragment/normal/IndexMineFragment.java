package com.eucleia.pdicheck.fragment.normal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.ActivityUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.activity.normal.LoginActivity;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.bean.normal.ChangeMine;
import com.eucleia.pdicheck.bean.normal.MineType;
import com.eucleia.pdicheck.databinding.FragIndexMineBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadNormalBinding;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.pdicheck.listener.IndexMineFunListener;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.database.PDIDatabase;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.dialog.BaseDialog;
import com.eucleia.tabscanap.util.AnalyseLogUtils;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.ResUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 我的
 */
public class IndexMineFragment extends BaseFragment {
    private FragIndexMineBinding binding;
    private LayoutHeadNormalBinding headNormal;

    private boolean isLoadViewPager;

    private BaseDialog exitDialog;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private IndexMineFunListener listener = new IndexMineFunListener() {

        @Override
        public void resetPwd(View view) {
            initFragment();
            changeFragment(0);
            binding.setIndex(MineType.RESETPWD);
        }

        @Override
        public void connectVci(View view) {
            if (needVci()) {
                binding.setIndex(MineType.NONE);
                BlueBaseActivity activity = (BlueBaseActivity) getActivity();
                if (activity != null) {
                    BTConstant.isShowList = true;
                    activity.openBluetooth();
                }
            } else {
                initFragment();
                changeFragment(1);
                binding.setIndex(MineType.CONNECT);
            }
        }


        @Override
        public void analyze(View view) {
            initFragment();
            changeFragment(2);
            binding.setIndex(MineType.ANALYSE);
        }

        @Override
        public void checkPlan(View view) {
            initFragment();
            changeFragment(6);
            binding.setIndex(MineType.CHECKPLAN);
//            binding.setIndex(0);
//            startActivity(CheckPlanActivity.class);
        }

        @Override
        public void transMode(View view) {
            binding.setIndex(MineType.NONE);
            Integer tranMode = PDIDatabase.get().getCheckPlanDao().getTranMode();
            boolean hasTrans = tranMode != 3;
            binding.transModeSwitch.setChecked(hasTrans);
            PDIDatabase.get().getCheckPlanDao().disTranMode();
            if (hasTrans) {
                PDIDatabase.get().getCheckPlanDao().openTranMode();
            } else {
                PDIDatabase.get().getCheckPlanDao().closeTranMode();
            }
        }

        @Override
        public void about(View view) {
            initFragment();
            changeFragment(3);
            binding.setIndex(MineType.ABOUT);
        }

        @Override
        public void update(View view) {
            initFragment();
            changeFragment(4);
            binding.setIndex(MineType.UPDATE);
        }

        @Override
        public void exit(View view) {

            if (exitDialog == null) {
                exitDialog = new BaseDialog(getContext());
            }
            exitDialog.hideTitle();
            exitDialog.setMsg(ResUtils.getString(R.string.logout_hint));
            exitDialog.setCancelable(false);
            exitDialog.setRightButton(ResUtils.getString(R.string.define), () -> {
                DSUtils.get().putBoolean(SPKEY.IS_LOGIN, false);
                AppDatabase.get().getUserDao().resetUser();
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityUtils.startActivity(loginIntent);
            });
            exitDialog.show();

        }


    };
    private int index = -1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isOpenBus = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (exitDialog != null) {
            exitDialog.dismiss();
        }
    }

    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragIndexMineBinding.inflate(inflater, container, false);
            headNormal = binding.headNormal;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_index_mine;
    }

    @Override
    protected void initLayout() {
        headNormal.setHead(ResUtils.getString(R.string.mine_title));
        binding.setClickListener(listener);

        Integer tranMode = PDIDatabase.get().getCheckPlanDao().getTranMode();
        boolean hasTrans = tranMode == 3;
        binding.transModeSwitch.setChecked(hasTrans);
        binding.setIndex(MineType.NONE);

        //是否显示诊断日志开关
        if (AnalyseLogUtils.isDef()) {
            binding.analyseItem.setVisibility(View.VISIBLE);
            binding.analyseLine.setVisibility(View.VISIBLE);
            binding.connectItem.setBackgroundResource(R.drawable.block_white3);
        } else {
            binding.analyseItem.setVisibility(View.GONE);
            binding.analyseLine.setVisibility(View.GONE);
            binding.connectItem.setBackgroundResource(R.drawable.block_white2);
        }
    }

    private void initFragment() {
        if (isLoadViewPager) {
            return;
        }
        fragments.add(new MineResetPwdFragment());
        fragments.add(new MineDeviceFragment());
        fragments.add(new MineAnalyzeFragment());
        fragments.add(new MineAboutFragment());
        fragments.add(new MineUpdateFragment());
        fragments.add(new MineVciFragment());
        fragments.add(new MineCheckPlanFragment());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        for (Fragment fragment : fragments) {
            fragmentTransaction.add(R.id.child_frag, fragment);
            fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.CREATED);
        }
        fragmentTransaction.commitAllowingStateLoss();
        isLoadViewPager = true;
    }


    private void changeFragment(final int cp) {
        if (cp == index) {
            return;
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        if (index >= 0) {
            fragmentTransaction.hide(fragments.get(index));
            fragmentTransaction.setMaxLifecycle(fragments.get(cp), Lifecycle.State.CREATED);
        }
        fragmentTransaction.setMaxLifecycle(fragments.get(cp), Lifecycle.State.RESUMED);
        fragmentTransaction.show(fragments.get(cp));
        fragmentTransaction.commitAllowingStateLoss();
        index = cp;

    }

    @Subscribe
    public void changeFragment(final ChangeMine changeMine) {
        int cp = changeMine.getIndex();
        if (cp >= 0) {
            initFragment();
            changeFragment(cp);
        } else {
            binding.setIndex(MineType.NONE);
        }
    }

}
