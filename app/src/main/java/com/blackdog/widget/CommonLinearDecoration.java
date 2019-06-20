package com.blackdog.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blackdog.App;
import com.blackdog.util.ScreenUtils;

public class CommonLinearDecoration extends RecyclerView.ItemDecoration {
    private int mColor = Color.TRANSPARENT;
    private int mWidth = ScreenUtils.dip2px(App.getInstance(), 2);



    public int getColor() {
        return mColor;
    }

    public CommonLinearDecoration setColor(int color) {
        mColor = color;
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public CommonLinearDecoration setWidth(int width) {
        mWidth = width;
        return this;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = mWidth;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int top = child.getBottom() + params.bottomMargin;
            int right = child.getRight() + params.rightMargin;
            int bottom = top + mWidth;
            Paint paint = new Paint();
            paint.setColor(mColor);
            c.drawRect(left, top, right, bottom, paint);
        }
    }
}
