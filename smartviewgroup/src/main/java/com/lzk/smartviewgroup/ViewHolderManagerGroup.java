package com.lzk.smartviewgroup;

public abstract class ViewHolderManagerGroup<T> {
    private ViewHolderManager[] viewHolderManagers;

    public ViewHolderManagerGroup(ViewHolderManager... viewHolderManagers) {
        if (viewHolderManagers == null || viewHolderManagers.length == 0) {
            throw new IllegalArgumentException("viewHolderManagers can not be null");
        }
        this.viewHolderManagers = viewHolderManagers;
    }

    public abstract int getViewHolderManagerIndex(T itemData);

    public ViewHolderManager getViewHolderManager(T itemData) {
        int index = getViewHolderManagerIndex(itemData);
        if (index < 0 || index > viewHolderManagers.length - 1) {
            throw new IllegalArgumentException("ViewHolderManagerGroup中的getViewHolderManagerIndex方法返回的index必须在有效范围内");
        }
        return viewHolderManagers[index];
    }

    public String getViewHolderManagerTag(ViewHolderManager viewHolderManager) {
        return viewHolderManager.getClass().getName();
    }

    public ViewHolderManager[] getViewHolderManagers() {
        return viewHolderManagers;
    }
}
