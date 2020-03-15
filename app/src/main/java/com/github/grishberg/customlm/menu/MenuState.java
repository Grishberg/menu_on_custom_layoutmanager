package com.github.grishberg.customlm.menu;

import com.github.grishberg.customlm.rv.MenuAdapter;
import android.util.*;

public class MenuState {
	private static final String TAG = MenuState.class.getSimpleName();
	
    private static final int SINGLE_ROW_MODE_BTN_COUNT = 2;
	
    private final MenuAdapter adapter;
    private int barButtonsCount = 2;
    private int maxButtons = 6;
    private boolean isTwoRowMode = barButtonsCount > SINGLE_ROW_MODE_BTN_COUNT;

    public MenuState(MenuAdapter adapter) {
        this.adapter = adapter;
    }

    public void onMoved(int srcPos, int dstPos) {
        if (srcPos <= barButtonsCount && dstPos <= barButtonsCount) {
            return;
        }
        if (srcPos <= barButtonsCount) {
            barButtonsCount--;
        }
        if (dstPos <= barButtonsCount) {
            barButtonsCount++;
        }
        if (barButtonsCount < 1) {
            barButtonsCount = 1;
        }
        if (barButtonsCount > maxButtons) {
            barButtonsCount = maxButtons;
        }

        boolean newTwoRowModeState = isTwoRowMode();
        if (newTwoRowModeState == isTwoRowMode) {
            return;
        }

        if (newTwoRowModeState) {
            onTwoRowModeEnabled();
        } else {
            onSingleRowModeEnabled();
        }
    }

    private void onTwoRowModeEnabled() {
		Log.d(TAG, "------ onTwoRowModeEnabled");
        isTwoRowMode = true;
        //adapter.notifyItemRangeChanged(0, SINGLE_ROW_MODE_BTN_COUNT + 2);
    }

    private void onSingleRowModeEnabled() {
		Log.d(TAG, "------ onSingleRowModeEnabled");
        isTwoRowMode = false;
        //adapter.notifyItemRangeChanged(0, SINGLE_ROW_MODE_BTN_COUNT + 1);
    }

    public int getDynamicButtonsCount() {
        return barButtonsCount;
    }

    public boolean isTopItem(int pos) {
        if (isTwoRowMode()) {
            return pos == 0;
        }

        return pos <= barButtonsCount;
    }

    public boolean isTwoRowMode() {
        return barButtonsCount > SINGLE_ROW_MODE_BTN_COUNT;
    }
}
