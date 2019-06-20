package com.blackdog.module.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.blackdog.R;
import com.blackdog.module.main.adapter.model.ChannelMenuItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class ChannelMenuAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public ChannelMenuAdapter(List<MultiItemEntity> data) {
        super(data);
    }


    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HolderType.TYPE_CHANNEL:
                return new BaseViewHolder(getItemView(R.layout.item_menu_channel, parent));
            case HolderType.TYPE_TOP:
                return new BaseViewHolder(getItemView(R.layout.item_menu_top, parent));
            default:
                throw new IllegalArgumentException("undefined type");
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        if (item instanceof ChannelMenuItem) {
            ChannelMenuItem menuItem = (ChannelMenuItem) item;
            helper.setImageResource(R.id.iv_channel_menu, menuItem.getRes());
            helper.setText(R.id.tv_channel_menu, menuItem.getName());
        }
    }
}
