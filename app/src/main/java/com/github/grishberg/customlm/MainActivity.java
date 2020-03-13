package com.github.grishberg.customlm;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;

import com.github.grishberg.customlm.helper.OnStartDragListener;
import com.github.grishberg.customlm.helper.SimpleItemTouchHelperCallback;
import com.github.grishberg.customlm.menu.MenuDimension;
import com.github.grishberg.customlm.rv.CustomLayoutManager;
import com.github.grishberg.customlm.rv.MenuAdapter;
import com.github.grishberg.customlm.rv.MenuItemsDecorator;
import com.github.grishberg.customlm.rv.MenuRecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.github.grishberg.customlm.menu.*;

public class MainActivity extends Activity implements OnStartDragListener {
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        
        MenuRecyclerView rv = findViewById(R.id.rv);
        
        MenuAdapter adapter = new MenuAdapter(LayoutInflater.from(this), this);
		MenuState menuState = new MenuState(adapter);
		
		MenuDimension dimension = new MenuDimension(this, menuState);
		rv.setSizeChangeListener(dimension);
		
		MenuItemsDecorator decorator = new MenuItemsDecorator(menuState, dimension);
        rv.addItemDecoration(decorator);
		
        int menuItemWidth = getResources().getDimensionPixelSize(R.dimen.menuItemWidth);
        CustomLayoutManager lm = new CustomLayoutManager(menuState, menuItemWidth);
        lm.setButtonsCount(4);



        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter, menuState);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv);

        rv.setAdapter(adapter);
        rv.setLayoutManager(lm);
        adapter.updateItems(createItems());

        
    }

    private List<String> createItems() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            items.add(String.format("item %02d", i));
        }
        return items;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
