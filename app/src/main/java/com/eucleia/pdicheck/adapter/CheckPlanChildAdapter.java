package com.eucleia.pdicheck.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.ItemCheckPlanChildBinding;
import com.eucleia.pdicheck.listener.RecyclerItemListener;
import com.eucleia.pdicheck.room.entity.CheckPlan;


public class CheckPlanChildAdapter extends RecyclerView.Adapter<CheckPlanChildAdapter.ChildViewHolder> {

    private CheckPlan[] childs;
    private RecyclerItemListener childClickListener;

    public CheckPlanChildAdapter(CheckPlan[] childs, RecyclerItemListener childClickListener) {
        this.childs = childs;
        this.childClickListener = childClickListener;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCheckPlanChildBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_check_plan_child, parent, false);
        return new ChildViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        CheckPlan plan = childs[position];
        holder.binding.childText.setText(plan.funName);

        if (plan.enable == 0) {
            holder.binding.checkBox.setImageResource(R.drawable.ic_uncheck);
        } else {
            holder.binding.checkBox.setImageResource(R.drawable.ic_checked);
        }

        holder.itemView.setOnClickListener(v -> {
            childClickListener.atItemClick(position);
        });


    }

    @Override
    public int getItemCount() {
        return childs.length;
    }

    public void updatePlans(CheckPlan[] plans) {
        this.childs = plans;
        notifyDataSetChanged();
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        ItemCheckPlanChildBinding binding;

        public ChildViewHolder(ItemCheckPlanChildBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
