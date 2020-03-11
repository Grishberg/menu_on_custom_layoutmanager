package com.github.grishberg.customlm.rv;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.grishberg.customlm.menu.MenuButtonsDelegate;

public class CustomLayoutManager extends RecyclerView.LayoutManager implements MenuButtonsDelegate {
    private static final String TAG = "CLM";
    private int buttonsInRow = 3;
    private int maxButtons = 6;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new CustomLayoutManager.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
    }

    public void onMoved(int srcPos, int dstPos) {
        if (srcPos <= buttonsInRow && dstPos <= buttonsInRow) {
            return;
        }
        if (srcPos <= buttonsInRow) {
            buttonsInRow--;
        }
        if (dstPos <= buttonsInRow) {
            buttonsInRow++;
        }
        if (buttonsInRow < 1) {
            buttonsInRow = 1;
        }
        if (buttonsInRow > maxButtons) {
            buttonsInRow = maxButtons;
        }
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return super.generateLayoutParams(lp);
    }

    @Override
    public void incButtonsInRow() {
        buttonsInRow++;
    }

    @Override
    public void decButtonsInRow() {
        buttonsInRow--;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d(TAG, "onLayoutChildren");
        if (state.isPreLayout()) {
            Log.d(TAG, "is prelayout");
            for (int i = 0; i < getChildCount(); i++) {
                final View view = getChildAt(i);
                LayoutParams lp = (LayoutParams) view.getLayoutParams();

                if (lp.isItemRemoved()) {
                    Log.d(TAG, "item removed " + i);
                    //Track these view removals as visible
                    //removedCache.put(lp.getViewLayoutPosition(), REMOVE_VISIBLE);
                }
            }
        }
        //Clear all attached views into the recycle bin
        detachAndScrapAttachedViews(recycler);
        fillDown(recycler);
    }

    private void fillDown(RecyclerView.Recycler recycler) {
        int buttonInRowWidth = getWidth() / buttonsInRow;

        int pos = 0;
        boolean fillDown = true;
        int viewLeft = 0;
        int height = getHeight();
        int screenWidth = getWidth();
        int itemCount = getItemCount();
        int viewTop = 0;

        while (fillDown && pos < itemCount) {
            View child = recycler.getViewForPosition(pos);
            addView(child);
            CustomLayoutManager.LayoutParams lp = (LayoutParams) child.getLayoutParams();

            measureChildWithMargins(child, 0, 0);
            final int viewHeight = calculateHeight(getDecoratedMeasuredHeight(child), lp);
            final int viewWidth;

            if (lp.isAddressBar) {
                viewWidth = screenWidth;
            } else if (isButtonsRow(pos)) {
                viewWidth = buttonInRowWidth;
            } else {
                viewWidth = getDecoratedMeasuredWidth(child);
            }

            layoutDecorated(child,
                    viewLeft,
                    viewTop,
                    viewLeft + viewWidth,
                    viewTop + viewHeight
            );

            Log.d(TAG, "layout child pos=" + pos + " ,x=" + viewLeft + ", y=" + viewTop +
                    ", w=" + viewWidth +
                    ", h=" + viewHeight +
                    ", left =" + getDecoratedLeft(child) +
                    ", top =" + getDecoratedTop(child));
            viewLeft += viewWidth;

            if (viewLeft >= screenWidth) {
                viewTop += viewHeight;
                // calculate item width and left offset
                viewLeft = 0;
            }
            pos++;
        }
    }

    private boolean isButtonsRow(int pos) {
        if (pos > 0 && pos <= buttonsInRow) {
            return true;
        }
        return false;
    }

    private int calculateHeight(
            int measuredHeight,
            LayoutParams lp) {
        if (lp.height == LayoutParams.WRAP_CONTENT ||
                lp.height == LayoutParams.MATCH_PARENT
        ) {
            return measuredHeight;
        }
        if (lp.height < measuredHeight) {
            return measuredHeight;
        }
        return lp.height;
    }

    private int updateSpecWithExtra(int spec, int startInset, int endInset) {
        if (startInset == 0 && endInset == 0) {
            return spec;
        }
        final int mode = View.MeasureSpec.getMode(spec);
        if (mode == View.MeasureSpec.AT_MOST || mode == View.MeasureSpec.EXACTLY) {
            return View.MeasureSpec.makeMeasureSpec(
                    View.MeasureSpec.getSize(spec) - startInset - endInset, mode);
        }
        return spec;
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int delta = -dy;
        offsetChildrenVertical(delta);
        return -delta;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {
        private boolean isAddressBar;

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(Context c, AttributeSet a) {
            super(c, a);
        }

        public void setAddressBarState(boolean state) {
            isAddressBar = state;
        }
    }
}
