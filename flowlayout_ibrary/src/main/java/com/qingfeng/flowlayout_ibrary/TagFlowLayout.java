package com.qingfeng.flowlayout_ibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class TagFlowLayout extends FlowLayout implements TagAdapter.OnDataChangeListener {
    private TagAdapter mTagAdapter;

    public TagFlowLayout(Context context) {
        super(context);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDataChanged() {
        changeAdapter();
    }

    public void setAdapter(TagAdapter tagAdapter) {
        mTagAdapter = tagAdapter;
        tagAdapter.setOnDataChangeListener(this);
        changeAdapter();
    }

    private void changeAdapter() {
        removeAllViews();
        final TagAdapter tagAdapter = mTagAdapter;

        int count = tagAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View tagView = tagAdapter.getView(this, i, tagAdapter.getItem(i));
            if (tagView.getLayoutParams() == null) {
                tagView.setLayoutParams(generateDefaultLayoutParams());
            }
            final int position = i;
            tagView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TagAdapter.OnTagClickListener onTagClickListener = tagAdapter.getOnTagClickListener();
                    if(onTagClickListener != null){
                        onTagClickListener.onTagClick(TagFlowLayout.this,v,position);
                    }
                }
            });
            addView(tagView);
        }
    }


}
