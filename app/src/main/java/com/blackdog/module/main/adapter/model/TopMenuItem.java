package com.blackdog.module.main.adapter.model;

import com.blackdog.module.main.adapter.HolderType;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class TopMenuItem implements MultiItemEntity {
    @Override
    public int getItemType() {
        return HolderType.TYPE_TOP;
    }
}
