package com.eucleia.pdicheck.activity.normal;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.FileUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.adapter.AnalyzeLogAdapter;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.databinding.ActAnalyzeLogBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadBaseBinding;
import com.eucleia.pdicheck.listener.RecyclerItemListener;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.entity.AnalyzeLog;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.util.ResUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 日志采集列表
 */
public class AnalyzeLogActivity extends BlueBaseActivity {

    private ActAnalyzeLogBinding binding;
    private LayoutHeadBaseBinding headBase;
    private List<AnalyzeLog> logs;
    private int selectIndex = -1;
    private AnalyzeLogAdapter adapter;

    private final SingleClickListener deleteListener = new SingleClickListener() {
        @Override
        public void singleClick(View view) {
            long id = logs.get(selectIndex).id;
            logs.remove(selectIndex);
            selectIndex = -1;
            adapter.setIndex(selectIndex);
            notifyDetail();
            AppDatabase.get().getLogDao().deleteLog(id);
        }
    };
    private final RecyclerItemListener itemListener = new RecyclerItemListener() {
        @Override
        public void atItemClick(int position) {
            selectIndex = position;
            notifyDetail();
        }
    };


    @Override
    protected ViewDataBinding bindAct() {
        binding = ActAnalyzeLogBinding.inflate(getLayoutInflater());
        headBase = binding.headBase;
        return binding;
    }



    @Override
    public void initView() {
        base.setBackText(ResUtils.getString(R.string.back));
        base.setVciStatus(AppVM.get().getVciStatus());
        base.setTitle(ResUtils.getString(R.string.diag_logs));
        base.setBackListener(headFunListener);
        base.setRightListener(rightFunListener);
        headBase.setHead(base);

        binding.setListener(deleteListener);

        logs = AppDatabase.get().getLogDao().loadAll();
        adapter = new AnalyzeLogAdapter(logs, itemListener);
        binding.recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycle.setAdapter(adapter);
        notifyDetail();
    }

    private void notifyDetail() {
        if (selectIndex >= 0) {
            binding.logDetail.setVisibility(View.VISIBLE);
            AnalyzeLog log = logs.get(selectIndex);
            binding.time.setText(ResUtils.getString(R.string.diag_time) + SimpleDateFormat.getDateTimeInstance().format(log.startTime));
            binding.size.setText(ResUtils.getString(R.string.diag_size) + FileUtils.getSize(log.zipPath));
            binding.path.setText(ResUtils.getString(R.string.diag_path) + log.zipPath);
        } else {
            binding.logDetail.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void vciChange(VciStatus vciStatus) {
        base.setVciStatus(vciStatus);
        headBase.setHead(base);
    }

    @Override
    protected void OnBackClick() {
        super.OnBackClick();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
