package com.github.grishberg.customlm.rv;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.grishberg.customlm.menu.MenuDimension;
import com.github.grishberg.customlm.menu.MenuState;

public class MenuItemsDecorator extends RecyclerView.ItemDecoration {
    private static final String TAG = "Decorator";

    private final MenuDimension menuDimension;
    private final MenuState menuState;

    public MenuItemsDecorator(MenuState state, MenuDimension dimension) {
        menuState = state;
        menuDimension = dimension;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int topOffset = 4;
        int leftOffset = 4;
        int rightOffset = 4;
        int bottomOffset = 4;
        int itemPos = parent.getChildAdapterPosition(view);
        if (menuState.isTopItem(parent.getChildAdapterPosition(view))) {
            Log.d(TAG, "getItemOffset pos=" + itemPos + ", isPreLayout" + state.isPreLayout());
            topOffset = menuDimension.getTopOffset();
        }
        //We can supply forced insets for each item view here in the Rect
        outRect.set(leftOffset, topOffset, rightOffset, bottomOffset);
    }
}
