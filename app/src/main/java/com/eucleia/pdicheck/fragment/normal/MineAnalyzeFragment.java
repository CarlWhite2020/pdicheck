package com.eucleia.pdicheck.fragment.normal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.AppUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.normal.AnalyzeLogActivity;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.databinding.FragMineAnalyzeBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadNormalBinding;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.pdicheck.listener.CollectFunListener;
import com.eucleia.tabscanap.util.AnalyseLogUtils;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.ResUtils;

/**
 * 分析与改进
 */
public class MineAnalyzeFragment extends BaseFragment {

    private FragMineAnalyzeBinding binding;
    private LayoutHeadNormalBinding headNormal;

    private final CollectFunListener listener = new CollectFunListener() {
        @Override
        public void switchCollect(View view) {
            boolean isCollect = !DSUtils.get().getBoolean(SPKEY.COLLECT_LOG, AnalyseLogUtils.isDef());
            DSUtils.get().putBoolean(SPKEY.COLLECT_LOG, isCollect);
            binding.setIsCollect(isCollect);
        }

        @Override
        public void toLogList(View view) {
            startActivity(AnalyzeLogActivity.class);
        }
    };

    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragMineAnalyzeBinding.inflate(inflater, container, false);
            headNormal = binding.headNormal;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_mine_analyze;
    }

    @Override
    protected void initLayout() {
        binding.analyse1.setText(String.format(ResUtils.getString(R.string.analyse_1), AppUtils.getAppName()));
        headNormal.setHead(ResUtils.getString(R.string.analyze));

        binding.setIsCollect(DSUtils.get().getBoolean(SPKEY.COLLECT_LOG, AnalyseLogUtils.isDef()));
        binding.setListener(listener);
    }
}
