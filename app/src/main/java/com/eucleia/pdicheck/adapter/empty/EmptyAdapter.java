package com.eucleia.pdicheck.adapter.empty;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eucleia.pdicheck.R;
import com.eucleia.tabscanap.util.ResUtils;

public abstract class EmptyAdapter extends RecyclerView.Adapter<EmptyHolder> {

    private static final int NORMAL = 0;
    private static final int EMPTY = 1;

    protected boolean emptyType() {
        return false;
    }

    protected String getEmptyText() {
        return ResUtils.getString(R.string.empty_hint);
    }

    @Override
    public int getItemViewType(int position) {
        return emptyType() ? EMPTY : NORMAL;
    }

    @NonNull
    @Override
    public EmptyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmptyHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_empty, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmptyHolder holder, int position) {
        if (emptyType()) {
            holder.binding.emptyHint.setText(getEmptyText());
        }
    }


    @Override
    public int getItemCount() {
        return emptyType() ? EMPTY : getItemSize();
    }

   protected abstract int getItemSize();
}
