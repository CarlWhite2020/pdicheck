package com.eucleia.pdicheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.ItemCheckPlanParentBinding;
import com.eucleia.pdicheck.listener.CheckPlanParentFunListener;
import com.eucleia.pdicheck.listener.RecyclerItemListener;
import com.eucleia.tabscanap.util.ResUtils;


public class CheckPlanParentAdapter extends RecyclerView.Adapter<CheckPlanParentAdapter.ParentViewHolder> {

    private String[] parents;
    private CheckPlanParentFunListener listener;
    private int[][] sizeArr;
    private int selectIndex = -1;

    public CheckPlanParentAdapter(String[] parents, int[][] sizeArr, CheckPlanParentFunListener listener) {
        this.parents = parents;
        this.sizeArr = sizeArr;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCheckPlanParentBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_check_plan_parent, parent, false);
        return new ParentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int p) {
        final int position = p;
        holder.binding.parentText.setText(parents[position]);
        holder.binding.checkNumber.setText(String.format(ResUtils.getString(R.string.choosed),
                sizeArr[position][0], sizeArr[position][1]));
        if (sizeArr[position][0] == 0) {
            holder.binding.checkBox.setImageResource(R.drawable.ic_uncheck);
        } else if (sizeArr[position][0] == sizeArr[position][1]) {
            holder.binding.checkBox.setImageResource(R.drawable.ic_checked);
        } else {
            holder.binding.checkBox.setImageResource(R.drawable.ic_halfcheck);
        }
        holder.itemView.setOnClickListener(v -> {
            int last = selectIndex;
            selectIndex = position;
            notifyItemChanged(last);
            notifyItemChanged(position);
            listener.atItemClick(position);
        });

        holder.binding.checkBox.setOnClickListener(v -> {
            if (sizeArr[position][0] == sizeArr[position][1]) {
                listener.clearChild(position);
            } else {
                listener.allChild(position);
            }
            holder.itemView.performClick();
        });

        if (selectIndex == position) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }

        if (position == parents.length - 1) {
            holder.binding.bottomLine.setVisibility(View.GONE);
        } else {
            holder.binding.bottomLine.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return parents.length;
    }

    class ParentViewHolder extends RecyclerView.ViewHolder {

        ItemCheckPlanParentBinding binding;

        public ParentViewHolder(ItemCheckPlanParentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
