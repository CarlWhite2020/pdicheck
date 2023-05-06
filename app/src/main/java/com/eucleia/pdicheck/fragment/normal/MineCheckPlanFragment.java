package com.eucleia.pdicheck.fragment.normal;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.normal.CheckPlanActivity;
import com.eucleia.pdicheck.adapter.CheckPlanModelAdapter;
import com.eucleia.pdicheck.bean.constant.IntentKey;
import com.eucleia.pdicheck.databinding.FragMineCheckPlanBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadNormalBinding;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.pdicheck.listener.RecyclerItemListener;
import com.eucleia.tabscanap.util.ResUtils;
import com.eucleia.tabscanap.util.RoomUtils;

public class MineCheckPlanFragment extends BaseFragment {

    private FragMineCheckPlanBinding binding;
    private LayoutHeadNormalBinding headNormal;
    private String[] models;

    private final RecyclerItemListener itemListener = new RecyclerItemListener() {
        @Override
        public void atItemClick(final int position) {
            String model = models[position];
            Intent intent = new Intent(getContext(), CheckPlanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(IntentKey.CHECK_PLAN_MODEL, model);
            startActivity(intent);
        }
    };

    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragMineCheckPlanBinding.inflate(inflater, container, false);
            headNormal = binding.headNormal;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_mine_check_plan;
    }

    @Override
    protected void initView() {
        headNormal.setHead(ResUtils.getString(R.string.check_plan));
        models = RoomUtils.getPdiDB().getCheckPlanDao().loadModel();
        CheckPlanModelAdapter adapter = new CheckPlanModelAdapter(models, itemListener);
        binding.modelList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.modelList.setAdapter(adapter);
    }
}
