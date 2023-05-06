package com.eucleia.pdicheck.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.eucleia.pdicheck.listener.RecyclerItemListener;

public class ReportModelAdapter extends BaseAdapter {

    private RecyclerItemListener listener;

    public ReportModelAdapter(RecyclerItemListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
