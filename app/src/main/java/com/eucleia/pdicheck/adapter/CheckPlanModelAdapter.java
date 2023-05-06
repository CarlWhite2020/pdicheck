package com.eucleia.pdicheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.ItemMineCheckPlanModelBinding;
import com.eucleia.pdicheck.listener.RecyclerItemListener;

public class CheckPlanModelAdapter extends RecyclerView.Adapter<CheckPlanModelAdapter.ModelHolder> {
    private String[] models;
    private RecyclerItemListener itemListener;

    public CheckPlanModelAdapter(String[] models, RecyclerItemListener itemListener) {
        this.models = models;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ModelHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_mine_check_plan_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModelHolder holder, int position) {
        holder.binding.setModelName(models[position]);
        holder.binding.splitLine.setVisibility(position == getItemCount() - 1
                ? View.GONE : View.VISIBLE);
        holder.itemView.setOnClickListener(v -> {
            if (itemListener != null) {
                itemListener.atItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models != null ? models.length : 0;
    }

    class ModelHolder extends RecyclerView.ViewHolder {

        ItemMineCheckPlanModelBinding binding;

        public ModelHolder(@NonNull ItemMineCheckPlanModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
