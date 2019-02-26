package com.github.StormWyrm.flowlayout_library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

/**
 * 真正布局外包的一层布局，让我们的布局能够改变状态
 */
public class TagView extends FlowLayout implements Checkable {
    private static final int[] STATE_SET = {android.R.attr.state_checked};

    private boolean isChecked;

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if (isChecked != checked) {
            isChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    public View getTagView() {
        return getChildAt(0);
    }
}
