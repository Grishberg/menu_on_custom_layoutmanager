package com.github.grishberg.customlm.rv;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.grishberg.customlm.R;
import com.github.grishberg.customlm.helper.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int TYPE_ADDRESSBAR = 0;
    private static final int TYPE_ITEM = 1;

    private final ArrayList<String> items = new ArrayList<>();
    private final LayoutInflater inflater;
    private final OnStartDragListener dragStartListener;

    public MenuAdapter(LayoutInflater inflater, OnStartDragListener dragListener) {
        this.inflater = inflater;
        dragStartListener = dragListener;
    }

    public void updateItems(List<String> newItems) {
        items.clear();
        items.addAll(newItems);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ADDRESSBAR;
        }

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder vh, int pos) {
        CustomLayoutManager.LayoutParams lp = (CustomLayoutManager.LayoutParams) vh.itemView.getLayoutParams();
        if (getItemViewType(pos) == TYPE_ADDRESSBAR) {
            lp.setAddressBarState(true);
            return;
        }
        lp.setAddressBarState(false);
        vh.updateTitle(items.get(pos));
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        if (type == TYPE_ADDRESSBAR) {
            return createAddressBar(parent);
        }
        return createMenuItem(parent);
    }

    private BaseViewHolder createAddressBar(ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_address_bar, parent, false);
        return new AddressBarVH(v);
    }

    private BaseViewHolder createMenuItem(ViewGroup parent) {

        View v = inflater.inflate(R.layout.item_menu_button, parent, false);
        final BaseViewHolder holder = new MenuItemVH(v);
        // Start a drag whenever the handle view it touched
        holder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
        return holder;
    }

    public void onItemMove(int srcPos, int targetPos) {
        Collections.swap(items, srcPos, targetPos);
        notifyItemMoved(srcPos, targetPos);
    }

    public void onItemDismiss(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
    }
}
