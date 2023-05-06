package com.eucleia.pdicheck.adapter;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.ItemCheckItemBinding;
import com.eucleia.pdicheck.bean.normal.PdiItem;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.util.ArraysUtils;
import com.eucleia.tabscanap.util.ResUtils;

import java.util.List;

/**
 * 检测项
 */
public class CheckItemAdapter extends RecyclerView.Adapter<CheckItemAdapter.CheckItemHolder> {

    private List<PdiItem> pdiItems;

    public CheckItemAdapter(List<PdiItem> pdiItems) {
        this.pdiItems = pdiItems;
    }

    @NonNull
    @Override
    public CheckItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_check_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return pdiItems != null ? pdiItems.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckItemHolder holder, int position) {
        PdiItem pdiItem = pdiItems.get(position);

        holder.binding.item.setClickable(pdiItem.isShowGroup());
        holder.binding.itemArrow.setVisibility(pdiItem.isShowGroup() ? View.VISIBLE : View.GONE);
        holder.binding.itemName.setText(pdiItem.getItemName());

        holder.binding.itemArrow.setImageResource(pdiItem.isShowDetail() ? R.drawable.ic_arrow_up1 : R.drawable.ic_arrow_down1);

        holder.binding.line.setVisibility(getItemCount() > 1 ? View.VISIBLE : View.GONE);

        if (pdiItem.isGroupBold()) {
            holder.binding.itemName.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.binding.itemName.setTypeface(Typeface.DEFAULT);
        }

        if (pdiItem.getResult() > 4) {
            holder.binding.itemValue.setVisibility(View.VISIBLE);
            holder.binding.itemValue.setText(String.valueOf(pdiItem.getResult() - 4));
            if (!ArraysUtils.isEmpty(pdiItem.getItemHisDtc())) {
                holder.binding.itemValue.setTextColor(ResUtils.getColor(R.color.error));
            } else if (!ArraysUtils.isEmpty(pdiItem.getItemCurDtc())) {
                holder.binding.itemValue.setTextColor(ResUtils.getColor(R.color.warn));
            }
        } else if (!TextUtils.isEmpty(pdiItem.getItemValue())) {
            holder.binding.itemValue.setVisibility(View.VISIBLE);
            holder.binding.itemValue.setText(pdiItem.getItemValue());
            holder.binding.itemValue.setTextColor(ResUtils.getColor(R.color.text_main));
        } else {
            holder.binding.itemValue.setVisibility(View.GONE);
        }

        if (pdiItem.getResult() == 3) {
            holder.binding.itemStatus.setVisibility(View.VISIBLE);
            holder.binding.itemStatus.setImageResource(R.drawable.ic_good);
        } else if (pdiItem.getResult() > 4) {
            holder.binding.itemStatus.setVisibility(View.GONE);
        } else if (pdiItem.getResult() > 0) {
            holder.binding.itemStatus.setVisibility(View.VISIBLE);
            holder.binding.itemStatus.setImageResource(R.drawable.ic_error);
        } else {
            holder.binding.itemStatus.setVisibility(View.GONE);
        }

        holder.binding.item.setClickable(pdiItem.isShowGroup());
        holder.binding.item.setOnClickListener(v -> {
            pdiItem.setShowDetail(!pdiItem.isShowDetail());
            pdiItem.setNeedUpdate(true);
            updateItem();
        });

        if (pdiItem.isShowGroup()) {
            holder.binding.itemDetail.setVisibility(pdiItem.isShowDetail() ? View.VISIBLE : View.GONE);
            if (pdiItem.isShowDetail()) {
                StringBuilder line1 = new StringBuilder();
                if (!TextUtils.isEmpty(pdiItem.getItemPartNumber())) {
                    line1.append(ResUtils.getString(R.string.part_num));
                    line1.append(pdiItem.getItemPartNumber());
                    line1.append(CharVar.SPACE);
                }
                if (!TextUtils.isEmpty(pdiItem.getItemVersionNumber())) {
                    line1.append(ResUtils.getString(R.string.ver_num));
                    line1.append(pdiItem.getItemVersionNumber());
                }
                if (!TextUtils.isEmpty(line1)) {
                    holder.binding.itemPartVer.setVisibility(View.VISIBLE);
                    holder.binding.itemPartVer.setText(line1.toString());
                } else {
                    holder.binding.itemPartVer.setVisibility(View.GONE);
                }

                if (!ArraysUtils.isEmpty(pdiItem.getItemCurDtc())) {
                    holder.binding.itemCur.setVisibility(View.VISIBLE);
                    StringBuilder line2 = new StringBuilder();
                    line2.append(ResUtils.getString(R.string.cur_dtc));
                    line2.append("\n");
                    for (String cur : pdiItem.getItemCurDtc()) {
                        line2.append(cur);
                        line2.append(CharVar.COMMA);
                    }
                    line2.deleteCharAt(line2.lastIndexOf(CharVar.COMMA));
                    holder.binding.itemCur.setText(line2.toString());
                } else {
                    holder.binding.itemCur.setVisibility(View.GONE);
                }

                if (!ArraysUtils.isEmpty(pdiItem.getItemHisDtc())) {
                    holder.binding.itemHis.setVisibility(View.VISIBLE);
                    StringBuilder line3 = new StringBuilder();
                    line3.append(ResUtils.getString(R.string.his_dtc));
                    line3.append("\n");
                    for (String his : pdiItem.getItemHisDtc()) {
                        line3.append(his);
                        line3.append(CharVar.COMMA);
                    }
                    line3.deleteCharAt(line3.lastIndexOf(CharVar.COMMA));
                    holder.binding.itemHis.setText(line3.toString());
                } else {
                    holder.binding.itemHis.setVisibility(View.GONE);
                }

            }
        }


    }

    public void updateItem() {
        for (int i = 0; i < pdiItems.size(); i++) {
            PdiItem pdiItem = pdiItems.get(i);
            if (pdiItem.isNeedUpdate()) {
                pdiItem.setNeedUpdate(false);
                notifyItemChanged(i);
            }
        }
    }


    public class CheckItemHolder extends RecyclerView.ViewHolder {

        ItemCheckItemBinding binding;

        public CheckItemHolder(@NonNull ItemCheckItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
