package com.eucleia.pdicheck.fragment.normal;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.AppUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.FragMineUpdateBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadNormalBinding;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.pdicheck.listener.MineUpdateFunListener;
import com.eucleia.pdicheck.net.presenter.UpdateAppPresenter;
import com.eucleia.pdicheck.bean.normal.ChangeMine;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.jni.diagnostic.so.Communication;
import com.eucleia.tabscanap.util.BaseObserver;
import com.eucleia.tabscanap.util.ResUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 我的更新
 */
public class MineUpdateFragment extends BaseFragment {

    private FragMineUpdateBinding binding;
    private LayoutHeadNormalBinding headNormal;

    private final Observer vciObserver = new BaseObserver<String>() {
        @Override
        public void onNext(String version) {
            if (!TextUtils.isEmpty(version)) {
                String[] splits = version.split(CharVar.MINUS);
                // 固件版本
                String vciVersion = "---";
                if (splits.length >= 2) {
                    vciVersion = splits[1];
                }
                // 固件版本
                binding.vciVer.setText(vciVersion);
            }
        }
    };


    private final MineUpdateFunListener listener = new MineUpdateFunListener() {
        @Override
        public void updateApp(View view) {
            showLoadingDialog();
            UpdateAppPresenter.get().updateApk();
        }

        @Override
        public void updateVci(View view) {
            EventBus.getDefault().post(new ChangeMine(5));
        }
    };

    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragMineUpdateBinding.inflate(inflater, container, false);
            headNormal = binding.headNormal;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_mine_update;
    }

    @Override
    protected void initLayout() {
        binding.setClickListener(listener);
        headNormal.setHead(ResUtils.getString(R.string.update));
        binding.appVer.setText(CharVar.VERSION + AppUtils.getAppVersionName());
        showVciVer();
    }


    @Override
    protected void reLoadFrag() {
        super.reLoadFrag();
        showVciVer();
    }

    private void showVciVer() {
        Observable.create(subscriber -> {
            subscriber.onNext(Communication.Version());
            subscriber.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vciObserver);
    }
}
