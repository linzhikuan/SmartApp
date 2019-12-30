package com.lzk.smartviewgroup;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemTypeManager {
    protected Map<String, ViewHolderManagerGroup> itemTypeNameGroupMap = new HashMap<>();
    protected List<String> itemTypeNames = new ArrayList<>();
    protected List<ViewHolderManager> viewHolderManagers = new ArrayList<>();

    public void register(Class<?> cls, ViewHolderManager manager) {
        register(getTypeName(cls), manager);
    }

    public void register(Class<?> cls, ViewHolderManagerGroup group) {
        ViewHolderManager[] managers = group.getViewHolderManagers();
        for (int i = 0, length = managers.length; i < length; i++) {
            register(getClassNameFromGroup(cls, group, managers[i]), managers[i]);
        }
        itemTypeNameGroupMap.put(getTypeName(cls), group);
    }

    private void register(String typeName, ViewHolderManager manager) {
        if (itemTypeNames.contains(typeName)) {
            viewHolderManagers.set(itemTypeNames.indexOf(typeName), manager);
        } else {
            itemTypeNames.add(typeName);
            viewHolderManagers.add(manager);
        }
    }


    public int getItemType(@NonNull Object itemData) {
        if (itemData instanceof ItemManager) {
            int type = getItemTypeFromItemManager((ItemManager) itemData);
            if (type >= 0) {
                return type;
            }
        }

        String typeName = getTypeName(itemData.getClass());

        if (itemTypeNameGroupMap.containsKey(typeName)) {
            ViewHolderManager manager = itemTypeNameGroupMap.get(typeName).getViewHolderManager(itemData);
            typeName = getClassNameFromGroup(itemData.getClass(), itemTypeNameGroupMap.get(typeName), manager);
        }

        return itemTypeNames.indexOf(typeName);
    }

    private int getItemTypeFromItemManager(ItemManager itemManager) {
        String typeName = itemManager.getItemTypeName();
        int itemType = itemTypeNames.indexOf(typeName);
        if (itemType < 0) {
            ViewHolderManager holderManager = itemManager.getViewHolderManager();
            if (holderManager != null) {
                register(typeName, holderManager);
                itemType = itemTypeNames.size() - 1;
            }
        }
        return itemType;
    }

    public ViewHolderManager getViewHolderManager(int type) {
        if (type < 0 || type > viewHolderManagers.size() - 1) {
            return null;
        }

        return viewHolderManagers.get(type);
    }

    public ViewHolderManager getViewHolderManager(Object itemData) {
        if (itemData == null)
            return null;
        return getViewHolderManager(getItemType(itemData));
    }

    public List<ViewHolderManager> getViewHolderManagers() {
        return viewHolderManagers;
    }

    public List<String> getItemTypeNames() {
        return itemTypeNames;
    }

    private String getTypeName(Class<?> cls) {
        return cls.getName();
    }

    private String getClassNameFromGroup(Class<?> cls, ViewHolderManagerGroup group, ViewHolderManager manager) {
        return getTypeName(cls) + group.getViewHolderManagerTag(manager);
    }
}
