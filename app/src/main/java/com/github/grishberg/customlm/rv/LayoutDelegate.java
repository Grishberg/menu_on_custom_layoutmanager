package com.github.grishberg.customlm.rv;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.grishberg.customlm.menu.MenuState;

public class LayoutDelegate {
    private static final String TAG = "LD";

    private final RecyclerView.LayoutManager lm;
    private final MenuState menuState;
    private final AddressBarState addressBar = new AddressBarState();
    private final AddressBarAndItemsState addressBarAndItems = new AddressBarAndItemsState();
    private final ButtonsRowState buttonsRow = new ButtonsRowState();
    private final MenuItemsState menuItems = new MenuItemsState();
    private State state = addressBar;
    private int screenWidth;
    private int screenHeight;
    private int viewLeft;
    private int viewTop;
    private int buttonsInRowCount;
    private final int minItemWidth;
    private boolean prevTwoRowState;

    LayoutDelegate(MenuState menuState, RecyclerView.LayoutManager lm, int minItemWidth) {
        this.menuState = menuState;
        this.lm = lm;
        this.minItemWidth = minItemWidth;
        prevTwoRowState = menuState.isTwoRowMode();
    }

    void beforeLayout(int offset, int w, int h,
                      int buttonsInRowCount,
                      boolean isPrePayout) {
        viewTop = offset;
        viewLeft = 0;
        screenWidth = w;
        screenHeight = h;
        this.buttonsInRowCount = buttonsInRowCount;
        nextState(addressBar);
        Log.d(TAG, "|    beforeLayout viewTop =" + viewTop);
        if (!isPrePayout) {
            prevTwoRowState = menuState.isTwoRowMode();
        }
    }

    void layoutChild(View child, boolean isPrePayout) {
        state.layoutChild(child, isPrePayout);
    }

    private void layout(View child, int w, int h) {
        lm.layoutDecorated(child,
                viewLeft,
                viewTop,
                viewLeft + w,
                viewTop + h
        );
        Log.d(TAG, "|    layout " + child + ", l=" + viewLeft + ", t=" + viewTop +
                ", w=" + w + ", h=" + h);
    }

    private void nextState(State nextState) {
        state = nextState;
        state.activateState();
    }

    private class AddressBarState implements State {

        @Override
        public void activateState() {
            Log.d(TAG, "    state=AddressBarState");
            viewLeft = 0;
            if (!prevTwoRowState) {
                nextState(addressBarAndItems);
            }
        }

        @Override
        public void layoutChild(View child, boolean isPreLayout) {
            int w = screenWidth;
            int h = lm.getDecoratedMeasuredHeight(child);
            layout(child, w, h);
            viewTop += h;
            nextState(buttonsRow);
        }
    }

    private class AddressBarAndItemsState implements State {
        private int addressBarWidth;
        private int itemIndex;
        private int barHeight;

        @Override
        public void activateState() {
            Log.d(TAG, "    state=AddressBarAndItemsState");
            itemIndex = 0;
            viewLeft = 0;
            addressBarWidth = screenWidth - menuState.getDynamicButtonsCount() * minItemWidth;
            Log.d(TAG, "    activate ab&items top=" + viewTop);
        }

        @Override
        public void layoutChild(View child, boolean isPreLayout) {
            int h = lm.getDecoratedMeasuredHeight(child);
            if (itemIndex == 0) {
                layout(child, addressBarWidth, h);
                viewLeft += addressBarWidth;
                barHeight = h;
            } else {
                layout(child, minItemWidth, barHeight);
                viewLeft += minItemWidth;
            }

            itemIndex++;
            if (itemIndex == menuState.getDynamicButtonsCount() + 1) {
                nextState(menuItems);
                viewTop += barHeight;
            }
        }
    }

    private class ButtonsRowState implements State {
        private int itemIndex;
        private int itemWidth;

        @Override
        public void activateState() {
            Log.d(TAG, "    state=ButtonsRowState");
            itemIndex = 0;
            viewLeft = 0;
            itemWidth = screenWidth / menuState.getDynamicButtonsCount();
        }

        @Override
        public void layoutChild(View child, boolean isPreLayout) {
            int h = lm.getDecoratedMeasuredHeight(child);
            layout(child, itemWidth, h);
            viewLeft += itemWidth;

            itemIndex++;
            if (itemIndex == menuState.getDynamicButtonsCount()) {
                viewTop += h;
                nextState(menuItems);
            }
        }
    }

    private class MenuItemsState implements State {
        private int itemIndex;
        private int itemWidth;

        @Override
        public void activateState() {
            Log.d(TAG, "    state=MenuItemsState");

            viewLeft = 0;
            itemIndex = 0;
            itemWidth = screenWidth / buttonsInRowCount;
        }

        @Override
        public void layoutChild(View child, boolean isPreLayout) {
            int h = lm.getDecoratedMeasuredHeight(child);
            layout(child, itemWidth, h);
            viewLeft += itemWidth;

            itemIndex++;
            if (itemIndex == buttonsInRowCount) {
                itemIndex = 0;
                viewLeft = 0;
                viewTop += h;
            }
        }
    }

    private interface State {
        void layoutChild(View child, boolean isPreLayout);

        void activateState();
    }
}
