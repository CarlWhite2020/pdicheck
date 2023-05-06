package com.eucleia.pdicheck.adapter.empty;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.eucleia.pdicheck.databinding.LayoutEmptyBinding;

public class EmptyHolder extends RecyclerView.ViewHolder {

    public LayoutEmptyBinding binding;

    public EmptyHolder(@NonNull View itemView) {
        super(itemView);
    }

    public EmptyHolder(@NonNull ViewDataBinding binding) {
        super(binding.getRoot());
    }

    public EmptyHolder(@NonNull LayoutEmptyBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}