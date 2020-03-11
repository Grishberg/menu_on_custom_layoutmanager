package com.github.grishberg.customlm.rv;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class LayoutDelegate {
    private final RecyclerView.LayoutManager lm;
    private final AddressBarState addressBar = new AddressBarState();
    private final ButtonsRowState buttonsRow = new ButtonsRowState();
    private final MenuItemsState menuItems = new MenuItemsState();
    private State state = addressBar;
    private int screenWidth;
    private int screenHeight;
    private int viewLeft;
    private int viewTop;
    private int buttonsInDynamicRowCount;
    private int buttonsInRowCount;

    LayoutDelegate(RecyclerView.LayoutManager lm) {
        this.lm = lm;
    }

    void beforeLayout(int offset, int w, int h,
                      int buttonsInDynamicRowCount,
                      int buttonsInRowCount) {
        viewTop = offset;
        viewLeft = 0;
        screenWidth = w;
        screenHeight = h;
        this.buttonsInDynamicRowCount = buttonsInDynamicRowCount;
        this.buttonsInRowCount = buttonsInRowCount;
        state = addressBar;
    }

    void layoutChild(View child, int w, int h) {
        state.layoutChild(child, w, h);
    }

    private void layout(View child, int w, int h) {
        lm.layoutDecorated(child,
                viewLeft,
                viewTop,
                viewLeft + w,
                viewTop + h
        );
    }

    private void nextState(State nextState) {
        state = nextState;
        state.activateState();
    }

    private class AddressBarState implements State {

        @Override
        public void activateState() {
            viewLeft = 0;
        }

        @Override
        public void layoutChild(View child, int w, int h) {
            layout(child, w, h);
            viewTop += h;
            nextState(buttonsRow);
        }
    }

    private class ButtonsRowState implements State {
        private int itemIndex;
        private int itemWidth;

        @Override
        public void activateState() {
            itemIndex = 0;
            viewLeft = 0;
            itemWidth = screenWidth / buttonsInDynamicRowCount;
        }

        @Override
        public void layoutChild(View child, int w, int h) {
            layout(child, itemWidth, h);
            viewLeft += itemWidth;

            itemIndex++;
            if (itemIndex == buttonsInDynamicRowCount) {
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
            viewLeft = 0;
            itemIndex = 0;
            itemWidth = screenWidth / buttonsInRowCount;
        }

        @Override
        public void layoutChild(View child, int w, int h) {
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
        void layoutChild(View child, int w, int h);

        void activateState();
    }
}
