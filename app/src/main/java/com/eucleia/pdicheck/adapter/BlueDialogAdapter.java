package com.eucleia.pdicheck.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.ItemBlueDialogBinding;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.tabscanap.bean.model.BlueModel;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.ResUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙列表适配器
 */
public class BlueDialogAdapter extends RecyclerView.Adapter<BlueDialogAdapter.BlueDailogViewHolder> {

    private List<BlueModel> blueModels;
    private int lastIndex = -1;

    public BlueDialogAdapter() {
        this.blueModels = new ArrayList<>(BTConstant.devices);
    }

    public void update(BlueModel blueModel) {
        int last = this.blueModels.size();
        this.blueModels.add(blueModel);
        int now = this.blueModels.size();
        if (BTConstant.devices.size() == now) {
            notifyItemRangeChanged(last, now);
        } else {
            blueModels = new ArrayList<>(BTConstant.devices);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public BlueDailogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBlueDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_blue_dialog, parent, false);
        return new BlueDailogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BlueDailogViewHolder holder, int p) {
        final int position = p;
        BlueModel model = blueModels.get(position);
        holder.binding.setModel(model);
        if (TextUtils.equals(model.getBtMac(), BTConstant.btAddress)) {
            if (BTConstant.isVciConnect) {
                holder.binding.btStatus.setText(ResUtils.getString(R.string.blue_connected));
            } else {
                holder.binding.btStatus.setText(ResUtils.getString(R.string.blue_connecting));
            }
        } else {
            holder.binding.btStatus.setText(ResUtils.getString(R.string.blue_disconnect));
        }
        holder.itemView.setOnClickListener(v -> {
            if (TextUtils.equals(BTConstant.btAddress, model.getBtMac())) {
                BTConstant.btAddress = null;
                BTConstant.btName = null;
            } else {
                BTConstant.btAddress = model.getBtMac();
                BTConstant.btName = model.getBtName();
            }
            DSUtils.get().putString(SPKEY.BtName, BTConstant.btName);
            DSUtils.get().putString(SPKEY.BtMac, BTConstant.btAddress);
            notifyItemChanged(position);
            if (lastIndex > 0 && lastIndex != position) {
                notifyItemChanged(lastIndex);
            }
            lastIndex = position;
        });
    }


    @Override
    public int getItemCount() {
        return blueModels.size();
    }


    class BlueDailogViewHolder extends RecyclerView.ViewHolder {
        ItemBlueDialogBinding binding;

        public BlueDailogViewHolder(@NonNull ItemBlueDialogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
