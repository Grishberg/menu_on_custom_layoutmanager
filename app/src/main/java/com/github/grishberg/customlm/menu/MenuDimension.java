package com.github.grishberg.customlm.menu;

import android.content.Context;
import android.util.Log;

import com.github.grishberg.customlm.R;
import com.github.grishberg.customlm.rv.MenuRecyclerView;

public class MenuDimension implements MenuRecyclerView.SizeChangedListener {
    private static final String TAG = "MD";
    private final Context ctx;
    private int screenHeight;

    public MenuDimension(Context ctx) {
        this.ctx = ctx;
    }

    public int getBarSize() {
        return ctx.getResources().getDimensionPixelSize(R.dimen.barHeight);
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
