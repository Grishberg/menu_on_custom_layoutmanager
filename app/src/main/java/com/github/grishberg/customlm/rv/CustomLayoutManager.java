package com.github.grishberg.customlm.rv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.grishberg.customlm.menu.MenuButtonsDelegate;
import com.github.grishberg.customlm.menu.*;

public class CustomLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "CLM";
    
    private int scrollOffset;
    private final LayoutDelegate delegate;
    private int buttonsCount;

    public CustomLayoutManager(MenuState menuState, int itemWidth) {
        delegate = new LayoutDelegate(menuState, this, itemWidth);
    }

    public void setButtonsCount(int count) {
        buttonsCount = count;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new CustomLayoutManager.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
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
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d(TAG, "====== onLayoutChildren: iPreLayout="+state.isPreLayout());
        if (state.isPreLayout()) {
            for (int i = 0; i < getChildCount(); i++) {
                final View view = getChildAt(i);
                LayoutParams lp = (LayoutParams) view.getLayoutParams();

                if (lp.isItemRemoved()) {
                    Log.d(TAG, "    item removed " + i);
                    //Track these view removals as visible
                    //removedCache.put(lp.getViewLayoutPosition(), REMOVE_VISIBLE);
                }
				
				if(lp.isItemChanged()){
					Log.d(TAG, "    item changed " + i);
				}
            }
        }
        //Clear all attached views into the recycle bin
        detachAndScrapAttachedViews(recycler);
        fillDown(recycler, state);
    }

    private void fillDown(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int pos = 0;
        boolean fillDown = true;
        int height = getHeight();
        int screenWidth = getWidth();
        int itemCount = getItemCount();
        delegate.beforeLayout(scrollOffset, screenWidth, height, buttonsCount);

        while (fillDown && pos < itemCount) {
            View child = recycler.getViewForPosition(pos);
            addView(child);
            CustomLayoutManager.LayoutParams lp = (LayoutParams) child.getLayoutParams();

            measureChildWithMargins(child, 0, 0);
            delegate.layoutChild(child, state.isPreLayout());
            // TODO: exit when thera are no visible items
            pos++;
        }
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int delta = scrollVerticallyInternal(dy);
        offsetChildrenVertical(-delta);
        return delta;
    }

    private int scrollVerticallyInternal(int dy) {
        int childCount = getChildCount();
        int itemCount = getItemCount();

        if (childCount == 0) {
            return 0;
        }

        final View topView = getChildAt(0);
        final View bottomView = getChildAt(childCount - 1);

        if (areAllItemsPlacedOnScreen(topView, bottomView)) {
            return 0;
        }
        int delta;
        if (dy < 0) {
            delta = calculateDeltaWhenScrollDown(dy, topView);
        } else {
            delta = calculateDeltaWhenScrollUp(dy, bottomView, itemCount);
        }
        scrollOffset -= delta;
        return delta;
    }

    private boolean areAllItemsPlacedOnScreen(View topView, View bottomView) {
        int viewSpan = getDecoratedBottom(bottomView) - getDecoratedTop(topView);
        return viewSpan <= getHeight();
    }

    private int calculateDeltaWhenScrollDown(int dy, View topView) {
        if (isViewTheFirst(topView)) {
            int viewTop = getDecoratedTop(topView);
            return Math.max(viewTop, dy);
        } else {
            return dy;
        }
    }

    private boolean isViewTheFirst(View v) {
        return getPosition(v) == 0;
    }

    private int calculateDeltaWhenScrollUp(int dy, View bottomView, int itemCount) {
        if (isViewTheLast(bottomView, itemCount)) {
            int viewBottom = getDecoratedBottom(bottomView);
            int parentBottom = getHeight();
            int delta = Math.min(viewBottom - parentBottom, dy);
            return delta;
        }
        return dy;
    }

    private boolean isViewTheLast(View v, int itemCount) {
        int adapterPos = getPosition(v);
        return adapterPos == itemCount - 1;
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
