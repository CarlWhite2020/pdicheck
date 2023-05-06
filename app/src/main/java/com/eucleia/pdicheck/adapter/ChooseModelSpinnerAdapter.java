package com.eucleia.pdicheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eucleia.pdicheck.R;

import java.util.ArrayList;

public class ChooseModelSpinnerAdapter extends BaseAdapter {

    private ArrayList models;

    public ChooseModelSpinnerAdapter(ArrayList models) {
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ModelViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_model_spinner, parent, false);
            holder = new ModelViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ModelViewHolder) view.getTag();
        }
        holder.model.setText(String.valueOf(models.get(position)));
        return view;
    }

    class ModelViewHolder {
        TextView model;
        public ModelViewHolder(View view) {
            model = view.findViewById(R.id.model);
        }
    }

}
