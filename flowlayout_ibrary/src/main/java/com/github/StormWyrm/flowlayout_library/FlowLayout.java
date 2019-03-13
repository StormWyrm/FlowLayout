package com.github.StormWyrm.flowlayout_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";

    private ArrayList<ArrayList<View>> mAllViews = new ArrayList<>();
    private ArrayList<View> mLineViews = new ArrayList<>();
    private ArrayList<Integer> mLineWidths = new ArrayList<>();
    private ArrayList<Integer> mLineHeights = new ArrayList<>();
    private int lineMargin;
    private int itemMagin;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout, defStyleAttr, 0);
        lineMargin = (int) ta.getDimension(R.styleable.FlowLayout_line_margin, 0);
        itemMagin = (int) ta.getDimension(R.styleable.FlowLayout_item_margin, 0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mAllViews.clear();
        mLineViews.clear();
        mLineWidths.clear();
        mLineHeights.clear();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0, height = 0;//测量的Flawlyout的宽和高
        int lineWidth = 0, lineHeight = 0;//记录当前行的宽和高

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE)
                continue;

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin + itemMagin;
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin + lineMargin;

            if (lineWidth + childWidth <= widthSize - getPaddingLeft() - getPaddingRight()) {
                mLineViews.add(child);
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            } else {
                //新的一行
                lineWidth -= itemMagin;//去除最后一列的item margin

                mAllViews.add(mLineViews);
                mLineWidths.add(lineWidth);
                mLineHeights.add(lineHeight);

                width = Math.max(width, lineWidth);//记录最大的width
                height += lineHeight;//记录当前高度

                mLineViews = new ArrayList<>();
                mLineViews.add(child);

                lineWidth = childWidth;
                lineHeight = childHeight;
            }

            //当View为最后一个时，保存最后一行
            if (i == childCount - 1) {
                lineWidth -= itemMagin;//去除最后一列的item margin
                lineHeight -= lineMargin;//去除最后行 line margin

                mAllViews.add(mLineViews);
                mLineWidths.add(lineWidth);
                mLineHeights.add(lineHeight);

                width = Math.max(width, lineWidth);//记录最大的width
                height += lineHeight;
            }
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : width + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? heightSize : height + getPaddingTop() + getPaddingBottom()
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int curLeft = getPaddingLeft();
        int curTop = getPaddingTop();

        for (int i = 0; i < mAllViews.size(); i++) {
            ArrayList<View> lineViews = mAllViews.get(i);
            int lineHeight = mLineHeights.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View view = lineViews.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();

                int cl = curLeft + layoutParams.leftMargin;
                int ct = curTop + layoutParams.topMargin;
                int cr = cl + view.getMeasuredWidth();
                int cb = ct + view.getMeasuredHeight();

                view.layout(cl, ct, cr, cb);

                curLeft += view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin + itemMagin;
            }
            curLeft = getPaddingLeft();
            curTop += lineHeight;
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }


}
