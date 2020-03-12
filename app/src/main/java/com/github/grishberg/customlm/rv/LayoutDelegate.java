package com.github.grishberg.customlm.rv;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.util.*;
import com.github.grishberg.customlm.menu.*;

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

    LayoutDelegate(MenuState menuState, RecyclerView.LayoutManager lm, int minItemWidth) {
		this.menuState = menuState;
        this.lm = lm;
		this.minItemWidth = minItemWidth;
    }

    void beforeLayout(int offset, int w, int h,
                      int buttonsInRowCount) {
        viewTop = offset;
        viewLeft = 0;
        screenWidth = w;
        screenHeight = h;
        this.buttonsInRowCount = buttonsInRowCount;
        nextState(addressBar);
		Log.d(TAG, "beforeLayout viewTop ="+viewTop);
    }

    void layoutChild(View child) {
        state.layoutChild(child);
    }

    private void layout(View child, int w, int h) {
        lm.layoutDecorated(child,
                viewLeft,
                viewTop,
                viewLeft + w,
                viewTop + h
        );
		Log.d(TAG, "layout l="+viewLeft +", t="+viewTop+
				", w="+w+", h="+h);
    }

    private void nextState(State nextState) {
        state = nextState;
        state.activateState();
    }

    private class AddressBarState implements State {

        @Override
        public void activateState() {
            viewLeft = 0;
			if (!menuState.isTwoRowMode()){
				nextState(addressBarAndItems);
			}
        }

        @Override
        public void layoutChild(View child) {
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
			itemIndex = 0;
            viewLeft = 0;
			addressBarWidth = screenWidth - menuState.getDynamicButtonsCount() * minItemWidth;
			Log.d(TAG, "activate ab&items top="+viewTop);
        }

        @Override
        public void layoutChild(View child) {
			int h = lm.getDecoratedMeasuredHeight(child);
			if(itemIndex == 0) {
				layout(child, addressBarWidth, h);
				viewLeft += addressBarWidth;
				barHeight = h;
			} else {
            	layout(child, minItemWidth, barHeight);
				viewLeft += minItemWidth;
			}
			
			itemIndex ++;
			if(itemIndex == menuState.getDynamicButtonsCount() + 1) {
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
            itemIndex = 0;
            viewLeft = 0;
            itemWidth = screenWidth / menuState.getDynamicButtonsCount();
        }

        @Override
        public void layoutChild(View child) {
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
            viewLeft = 0;
            itemIndex = 0;
            itemWidth = screenWidth / buttonsInRowCount;
        }

        @Override
        public void layoutChild(View child) {
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
        void layoutChild(View child);

        void activateState();
    }
}
