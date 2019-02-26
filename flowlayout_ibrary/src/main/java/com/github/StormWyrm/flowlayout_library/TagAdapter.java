package com.github.StormWyrm.flowlayout_library;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TagAdapter<T> {
    private List<T> mDatas;
    private HashSet<Integer> mCheckedPosList = new HashSet<>();

    private OnDataChangeListener onDataChangeListener;
    private OnTagClickListener onTagClickListener;
    private OnTagSelectListener onTagSelectListener;

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

    public void setOnTagSelectListener(OnTagSelectListener onTagSelectListener) {
        this.onTagSelectListener = onTagSelectListener;
    }

    public OnTagSelectListener getOnTagSelectListener() {
        return onTagSelectListener;
    }


    public void setDatas(List<T> datas) {
        this.mDatas = datas;
        notifyDataChanged();
    }

    public void setSelectedList(int... poses) {
        Set<Integer> set = new HashSet<>();
        for (int pos : poses) {
            set.add(pos);
        }
        setSelectedList(set);
    }


    public void setSelectedList(Set<Integer> set) {
        mCheckedPosList.clear();
        if (set != null) {
            mCheckedPosList.addAll(set);
        }
        notifyDataChanged();
    }

    public HashSet<Integer> getPreCheckedList() {
        return mCheckedPosList;
    }

    public T getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    public void notifyDataChanged() {
        if (onDataChangeListener != null)
            onDataChangeListener.onDataChanged();
    }

    /**
     * 当View被选择时候调用，可以在这里设置View被选中的状态
     *
     * @param position
     * @param view
     */
    public void onSelected(int position, View view) {

    }

    /**
     * 当View不被选择时候调用，可以在这里设置view的默认状态
     *
     * @param position 位置
     * @param view     被选择的View
     */
    public void unSelected(int position, View view) {

    }

    public abstract View getView(TagFlowLayout tagFlowLayout, int position, T item);

    public interface OnDataChangeListener {
        void onDataChanged();
    }

    public interface OnTagClickListener {
        boolean onTagClick(TagFlowLayout tagFlowLayout, View view, int position);
    }

    public interface OnTagSelectListener {
        void onTagSelect(Set<Integer> selectViews);
    }
}
