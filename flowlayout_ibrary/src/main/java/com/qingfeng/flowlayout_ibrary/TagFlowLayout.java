package com.qingfeng.flowlayout_ibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TagFlowLayout extends FlowLayout
        implements TagAdapter.OnDataChangeListener {

    private TagAdapter mTagAdapter;
    private HashSet<Integer> selectViews = new HashSet<>();
    private int maxSelect;

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout, defStyleAttr, 0);
        maxSelect = typedArray.getInt(R.styleable.FlowLayout_max_select, 0);
        typedArray.recycle();
    }

    @Override
    public void onDataChanged() {
        selectViews.clear();
        changeAdapter();
    }

    public void setMaxSelect(int maxSelect) {
        if (maxSelect < 0)
            maxSelect = -1;

        this.maxSelect = maxSelect;
    }

    public void setAdapter(TagAdapter tagAdapter) {
        mTagAdapter = tagAdapter;
        tagAdapter.setOnDataChangeListener(this);
        selectViews.clear();
        changeAdapter();
    }

    public Set<Integer> getSelectList() {
        return selectViews;
    }

    private void changeAdapter() {
        removeAllViews();
        final TagAdapter tagAdapter = mTagAdapter;
        HashSet preCheckedList = fixPreCheckedList(tagAdapter.getPreCheckedList());
        TagView tagViewContainer = null;

        int count = tagAdapter.getCount();
        for (int i = 0; i < count; i++) {
            final View tagView = tagAdapter.getView(this, i, tagAdapter.getItem(i));
            tagView.setDuplicateParentStateEnabled(true);//将父View的状态传递给子View
            tagView.setClickable(false);//让父控件来响应触摸事件

            tagViewContainer = new TagView(getContext());

            if (tagView.getLayoutParams() == null) {
                tagViewContainer.setLayoutParams(generateDefaultLayoutParams());
            } else {
                tagViewContainer.setLayoutParams(tagView.getLayoutParams());
            }

            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            tagView.setLayoutParams(lp);
            tagViewContainer.addView(tagView);
            addView(tagViewContainer);

            final int position = i;
            final TagView finalTagViewContainer = tagViewContainer;

            if (preCheckedList != null && preCheckedList.contains(i)) {
                setChildChecked(finalTagViewContainer, position);
                selectViews.add(position);
            }

            tagViewContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSelect(finalTagViewContainer, position);
                    TagAdapter.OnTagClickListener onTagClickListener = tagAdapter.getOnTagClickListener();
                    TagAdapter.OnTagSelectListener onTagSelectListener = tagAdapter.getOnTagSelectListener();

                    if (onTagClickListener != null) {
                        boolean b = onTagClickListener.onTagClick(TagFlowLayout.this, v, position);
                        if (!b && onTagSelectListener != null) {
                            onTagSelectListener.onTagSelect(new HashSet<Integer>(selectViews));
                        }
                    } else if (onTagSelectListener != null) {
                        onTagSelectListener.onTagSelect(new HashSet<Integer>(selectViews));
                    }
                }
            });
        }
    }

    private HashSet fixPreCheckedList(HashSet<Integer> preCheckedList) {
        HashSet<Integer> hashSet = new HashSet<>();
        if (preCheckedList == null || preCheckedList.size() == 0 || maxSelect == 0)
            return hashSet;
        int tagCount = mTagAdapter.getCount();
        int selectableCount = maxSelect < 0 ? tagCount : maxSelect;
        Iterator<Integer> iterator = preCheckedList.iterator();

        for (int i = 0; i < selectableCount; i++) {
            if (!iterator.hasNext()) {
                break;
            }
            Integer index = iterator.next();
            if (index < tagCount) {
                hashSet.add(index);
            }
        }
        return hashSet;
    }

    private void doSelect(TagView tagView, int position) {
        //不能选择时，什么都不做
        if (maxSelect == 0)
            return;

        if (!tagView.isChecked()) {
            if (maxSelect == 1 && selectViews.size() == 1) {
                Iterator<Integer> iterator = selectViews.iterator();
                Integer preSelectIndex = iterator.next();
                TagView preSelectView = (TagView) getChildAt(preSelectIndex);

                setChildUnchecked(preSelectView, preSelectIndex);
                setChildChecked(tagView, position);

                selectViews.remove(preSelectIndex);
                selectViews.add(position);
            } else {
                if (maxSelect > 0 && selectViews.size() >= maxSelect)
                    return;

                //当maxSelect为负数时 或者 没有超过限制数
                setChildChecked(tagView, position);
                selectViews.add(position);
            }
        } else {
            setChildUnchecked(tagView, position);
            selectViews.remove(position);
        }
    }

    private void setChildChecked(TagView tagView, int position) {
        tagView.setChecked(true);
        mTagAdapter.onSelected(position, tagView.getTagView());
    }

    private void setChildUnchecked(TagView tagView, int position) {
        tagView.setChecked(false);
        mTagAdapter.unSelected(position, tagView.getTagView());
    }
}
