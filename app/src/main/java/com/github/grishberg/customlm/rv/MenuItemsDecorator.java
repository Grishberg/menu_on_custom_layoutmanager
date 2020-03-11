package com.github.grishberg.customlm.rv;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.grishberg.customlm.menu.MenuDimension;

public class MenuItemsDecorator extends RecyclerView.ItemDecoration {
    private final MenuDimension menuState;

    public MenuItemsDecorator(MenuDimension menuState) {
        this.menuState = menuState;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int topOffset = 4;
        int leftOffset = 4;
        int rightOffset = 4;
        int bottomOffset = 4;
        if (parent.getChildAdapterPosition(view) == 0) {
            topOffset = menuState.getTopOffset();
        }
        //We can supply forced insets for each item view here in the Rect
        outRect.set(leftOffset, topOffset, rightOffset, bottomOffset);
    }
}
