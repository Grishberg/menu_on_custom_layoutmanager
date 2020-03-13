package com.github.grishberg.customlm.menu;

import android.content.Context;
import android.util.Log;

import com.github.grishberg.customlm.R;
import com.github.grishberg.customlm.rv.MenuRecyclerView;

public class MenuDimension implements MenuRecyclerView.SizeChangedListener {
    private static final String TAG = "MD";
    private final Context ctx;
	private final MenuState menuState;
    private int screenHeight;

    public MenuDimension(Context ctx, MenuState state) {
        this.ctx = ctx;
		menuState = state;
    }

    public int getBarSize() {
		int menuItemHeight = ctx.getResources().getDimensionPixelSize(R.dimen.menuItemHeight);
		int menuItemTitleHeight = ctx.getResources().getDimensionPixelSize(R.dimen.menuItemTitleHeight);
        int barHeight = ctx.getResources().getDimensionPixelSize(R.dimen.barHeight);
		if(menuState.isTwoRowMode()){
			return menuItemHeight + menuItemTitleHeight + barHeight;
		}
		return barHeight;
    }

    public int getTopOffset() {
        int offset = screenHeight - getBarSize();
        Log.d(TAG, "get offset =" + offset);
        return offset;
    }

    @Override
    public void onSizeChanged(int w, int h) {
        screenHeight = h;
        Log.d(TAG, "on size changed " + h);
    }
}
