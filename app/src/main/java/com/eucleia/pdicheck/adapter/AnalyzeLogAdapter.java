package com.eucleia.pdicheck.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.adapter.empty.EmptyAdapter;
import com.eucleia.pdicheck.adapter.empty.EmptyHolder;
import com.eucleia.pdicheck.databinding.ItemLogAnalyzeBinding;
import com.eucleia.pdicheck.listener.RecyclerItemListener;
import com.eucleia.pdicheck.room.entity.AnalyzeLog;
import com.eucleia.tabscanap.util.ArraysUtils;

import java.util.List;

public class AnalyzeLogAdapter extends EmptyAdapter {

    private List<AnalyzeLog> logs;
    private RecyclerItemListener listener;
    private volatile int selectIndex = -1;


    public AnalyzeLogAdapter(List<AnalyzeLog> logs, RecyclerItemListener listener) {
        this.logs = logs;
        this.listener = listener;
    }

    protected boolean emptyType() {
        return ArraysUtils.isEmpty(logs);
    }


    @NonNull
    @Override
    public EmptyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (!emptyType()) {
            return new LogHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_log_analyze, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull EmptyHolder holder, int position) {
        final int p = position;
        if (emptyType()) {
            super.onBindViewHolder(holder, p);
            return;
        }
        AnalyzeLog log = logs.get(p);
        LogHolder logHolder = (LogHolder) holder;
        logHolder.binding.number.setText(String.valueOf(log.id));
        logHolder.binding.name.setText(log.fileName);
        holder.itemView.setSelected(selectIndex == p);
        holder.itemView.setOnClickListener(v -> {
            selectIndex = p;
            if (listener != null) {
                listener.atItemClick(p);
            }
        });

    }

    @Override
    protected int getItemSize() {
        return logs.size();
    }

    public void setIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        notifyDataSetChanged();
    }


    public class LogHolder extends EmptyHolder {
        ItemLogAnalyzeBinding binding;

        public LogHolder(@NonNull ItemLogAnalyzeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
