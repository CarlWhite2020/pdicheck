package com.eucleia.pdicheck.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.ItemCheckGroupBinding;
import com.eucleia.pdicheck.bean.normal.PdiGroup;
import com.eucleia.pdicheck.bean.normal.PdiItem;
import com.eucleia.tabscanap.util.ELogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 检测组
 */
public class CheckGroupAdapter extends RecyclerView.Adapter<CheckGroupAdapter.CheckGroupHolder> {


    private List<PdiGroup> groups;

    @NonNull
    @Override
    public CheckGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckGroupHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_check_group, parent, false));
    }

    @Override
    public int getItemCount() {
        return groups != null ? groups.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckGroupHolder holder, int position) {
        PdiGroup group = groups.get(position);

        boolean isShowGroup = group.isShowGroup() && !TextUtils.isEmpty(group.getGroupName());
        if (isShowGroup) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.binding.group.setVisibility(View.VISIBLE);
            holder.binding.groupName.setText(group.getGroupName());
            holder.binding.groupArrow.setImageResource(group.isShowChild() ? R.drawable.ic_arrow_up1 : R.drawable.ic_arrow_down1);
            if (group.getResult() > 0) {
                holder.binding.groupStatus.setVisibility(View.VISIBLE);
                if (group.getResult() == 3) {
                    holder.binding.groupStatus.setImageResource(R.drawable.ic_good);
                } else {
                    holder.binding.groupStatus.setImageResource(R.drawable.ic_warn);
                }
            } else {
                holder.binding.groupStatus.setVisibility(View.GONE);
            }

        } else {
            holder.binding.group.setVisibility(View.GONE);
            if (!group.isShowChild()) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }
        holder.binding.group.setClickable(isShowGroup);
        holder.binding.group.setOnClickListener(v -> {
            group.setShowChild(!group.isShowChild());
            notifyItemChanged(position);
        });


        List<PdiItem> items = group.getItems();
        int size = Math.min(items.size(), 2);

        holder.binding.itemRecycler.setVisibility(group.isShowChild() && size > 0 ? View.VISIBLE : View.GONE);
        if (size > 0 && group.isShowChild()) {
            CheckItemAdapter itemAdapter = new CheckItemAdapter(items);
            holder.binding.itemRecycler.setLayoutManager(new StaggeredGridLayoutManager(size, RecyclerView.VERTICAL));
            holder.binding.itemRecycler.setAdapter(itemAdapter);
        }


    }

    public void resetData(Stack<PdiGroup> groups) {
        this.groups = groups;
        for (int i = 0; i < groups.size(); i++) {
            PdiGroup pdiGroup = groups.get(i);
            if (pdiGroup.isNeedUpdate()) {
                pdiGroup.setNeedUpdate(false);
                notifyItemChanged(i);
            }
        }
    }

    public class CheckGroupHolder extends RecyclerView.ViewHolder {
        ItemCheckGroupBinding binding;

        public CheckGroupHolder(@NonNull ItemCheckGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
