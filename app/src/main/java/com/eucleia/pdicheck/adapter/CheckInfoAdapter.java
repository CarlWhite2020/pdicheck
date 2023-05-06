package com.eucleia.pdicheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.eucleia.pdicheck.R;
import com.eucleia.tabscanap.bean.diag.CDispPdiCheckBeanEvent;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ResUtils;

public class CheckInfoAdapter extends BaseAdapter {

    private CDispPdiCheckBeanEvent event;

    public CheckInfoAdapter() {
    }

    @Override
    public int getCount() {
        return event != null ? 4 : 0;
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
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_check, parent, false);
        }
        AppCompatTextView name = view.findViewById(R.id.name);
        AppCompatTextView value = view.findViewById(R.id.value);

        switch (position) {
            case 0:
                name.setText(ResUtils.getString(R.string.brand));
                value.setText(event.getBrand());
                break;

            case 1:
                name.setText(ResUtils.getString(R.string.carModels));
                value.setText(event.getModel());
                break;

            case 2:
                name.setText(ResUtils.getString(R.string.year));
                value.setText(event.getYear());
                break;

            case 3:
                name.setText(ResUtils.getString(R.string.vin));
                value.setText(event.getVin());
                break;
        }

        return view;
    }

    public void setEvent(CDispPdiCheckBeanEvent event) {
        if (this.event == null) {
            this.event = event;
            notifyDataSetChanged();
        }
    }
}
