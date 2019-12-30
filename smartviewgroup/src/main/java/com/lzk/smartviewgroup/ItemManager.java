package com.lzk.smartviewgroup;


import androidx.annotation.NonNull;


public interface ItemManager {
    @NonNull
    String getItemTypeName();

    ViewHolderManager getViewHolderManager();
}
