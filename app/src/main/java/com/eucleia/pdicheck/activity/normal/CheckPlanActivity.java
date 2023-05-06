package com.eucleia.pdicheck.activity.normal;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.adapter.CheckPlanChildAdapter;
import com.eucleia.pdicheck.adapter.CheckPlanParentAdapter;
import com.eucleia.pdicheck.bean.constant.IntentKey;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.databinding.ActCheckPlanBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadBaseBinding;
import com.eucleia.pdicheck.listener.CheckPlanParentFunListener;
import com.eucleia.pdicheck.listener.RecyclerItemListener;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.room.database.PDIDatabase;
import com.eucleia.pdicheck.room.entity.CheckPlan;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.util.ResUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检测计划
 */
public class CheckPlanActivity extends BlueBaseActivity {

    private ActCheckPlanBinding binding;
    private LayoutHeadBaseBinding headBase;
    private String[] parents;
    private CheckPlan[][] plans;
    private int[][] sizeArr;
    private int parentPosition;


    private CheckPlanChildAdapter childAdapter;
    private CheckPlanParentAdapter parentAdapter;

    private final CheckPlanParentFunListener parentClickListener = new CheckPlanParentFunListener() {

        @Override
        public void atItemClick(final int position) {
            parentPosition = position;
            binding.childFun.setText(parents[position]);
            if (childAdapter == null) {
                childAdapter = new CheckPlanChildAdapter(plans[position], childClickListener);
                binding.checkChild.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.checkChild.setAdapter(childAdapter);
            } else {
                childAdapter.updatePlans(plans[position]);
            }
        }

        @Override
        public void clearChild(final int p) {
            for (CheckPlan checkPlan : plans[p]) {
                checkPlan.enable = 0;
            }
            updateTotal();
        }

        @Override
        public void allChild(final int p) {
            for (CheckPlan checkPlan : plans[p]) {
                checkPlan.enable = 1;
            }
            updateTotal();
        }
    };
    private final RecyclerItemListener childClickListener = new RecyclerItemListener() {
        @Override
        public void atItemClick(final int position) {
            CheckPlan plan = plans[parentPosition][position];
            plan.enable = plan.enable == 0 ? 1 : 0;
            parentAdapter.notifyItemChanged(parentPosition);
            childAdapter.notifyItemChanged(position);
            updateTotal();
        }
    };

    /**
     * 保存诊断计划
     */
    private final SingleClickListener savePlanListener = new SingleClickListener() {
        @Override
        public void singleClick(View view) {
            List<CheckPlan> list = new ArrayList<>();
            for (CheckPlan[] plan : plans) {
                Collections.addAll(list, plan);
            }
            PDIDatabase.get().getCheckPlanDao().update(list);
            finish();
        }
    };

    @Override
    protected ViewDataBinding bindAct() {
        binding = ActCheckPlanBinding.inflate(getLayoutInflater());
        headBase = binding.headBase;
        return binding;
    }


    @Override
    protected void initView() {
        String model = getIntent().getStringExtra(IntentKey.CHECK_PLAN_MODEL);

        base.setBackText(ResUtils.getString(R.string.back));
        base.setVciStatus(AppVM.get().getVciStatus());
        base.setTitle(model + ResUtils.getString(R.string.check_plan));
        base.setBackListener(headFunListener);
        base.setRightListener(rightFunListener);
        headBase.setHead(base);
        binding.setListener(savePlanListener);


        PDIDatabase pdiDB = PDIDatabase.get();
        parents = pdiDB.getCheckPlanDao().loadPlanItems(model);
        List<CheckPlan> checkPlans = pdiDB.getCheckPlanDao().loadPlanSettings(model);
        Map<String, List<CheckPlan>> map = new HashMap<>(parents.length);
        for (CheckPlan plan : checkPlans) {
            List<CheckPlan> cs = map.get(plan.funBlock);
            if (cs == null) {
                cs = new ArrayList<>();
            }
            cs.add(plan);
            map.put(plan.funBlock, cs);
        }
        plans = new CheckPlan[parents.length][checkPlans.size()];
        sizeArr = new int[parents.length][2];
        for (int i = 0; i < parents.length; i++) {
            List<CheckPlan> planList = map.get(parents[i]);
            plans[i] = planList.toArray(new CheckPlan[planList.size()]);
        }
        updateTotal();

        parentAdapter = new CheckPlanParentAdapter(parents, sizeArr, parentClickListener);
        binding.checkParent.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.checkParent.setAdapter(parentAdapter);

    }

    /**
     * 计算已选值
     */
    private void calculateSize() {
        for (int i = 0; i < plans.length; i++) {
            int select = 0;
            sizeArr[i][1] = plans[i].length;
            for (int j = 0; j < plans[i].length; j++) {
                if (plans[i][j].enable == 1) {
                    select++;
                }
            }
            sizeArr[i][0] = select;
        }
    }

    /**
     * 更新已选
     */
    private void updateTotal() {
        calculateSize();
        int select = 0, total = 0;
        for (int i = 0; i < sizeArr.length; i++) {
            select += sizeArr[i][0];
            total += sizeArr[i][1];
        }
        binding.total.setText(String.format(ResUtils.getString(R.string.total_choose), select, total));
    }

    @Override
    protected void vciChange(VciStatus vciStatus) {
        base.setVciStatus(vciStatus);
        headBase.setHead(base);
    }

    @Override
    protected void OnBackClick() {
        finish();
    }
}
