package com.qingfeng.flowlayout_ibrary;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TagAdapter<T> {
    private List<T> mDatas;
    private OnDataChangeListener onDataChangeListener;
    private OnTagClickListener onTagClickListener;

    public TagAdapter(List<T> datas) {
        this.mDatas = datas;
    }

    public TagAdapter(T[] datas) {
        this.mDatas = new ArrayList<>(Arrays.asList(datas));
    }

    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    public OnTagClickListener getOnTagClickListener() {
        return onTagClickListener;
    }

    public void notifyDataChange() {
        if (onDataChangeListener != null)
            onDataChangeListener.onDataChanged();
    }

    public T getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    public abstract View getView(TagFlowLayout tagFlowLayout, int position, T item);

    public interface OnDataChangeListener {
        void onDataChanged();
    }

    public interface OnTagClickListener {
        void onTagClick(TagFlowLayout tagFlowLayout,View view,int position);
    }
}
