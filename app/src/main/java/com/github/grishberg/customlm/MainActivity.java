package com.github.grishberg.customlm;

import android.app.*;
import android.os.*;
import android.support.v7.widget.*;
import java.util.*;
import com.github.grishberg.customlm.rv.*;
import android.view.*;
import com.github.grishberg.customlm.helper.*;
import android.support.v7.widget.RecyclerView.*;
import android.support.v7.widget.helper.*;
import com.github.grishberg.customlm.menu.*;

public class MainActivity extends Activity implements OnStartDragListener
{
	private ItemTouchHelper itemTouchHelper;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		MenuDimension dimension = new MenuDimension(this);
		
		MenuRecyclerView rv = findViewById(R.id.rv);
		rv.setSizeChangeListener(dimension);
		
		CustomLayoutManager lm = new CustomLayoutManager();
		
		MenuAdapter adapter = new MenuAdapter(LayoutInflater.from(this), this);
		
		ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter, lm);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv);
		
		rv.setAdapter(adapter);
		rv.setLayoutManager(lm);
		adapter.updateItems(createItems());
		
		
		MenuItemsDecorator decorator = new MenuItemsDecorator(dimension);
		rv.addItemDecoration(decorator);
    }
	
	private List<String> createItems(){
		ArrayList<String> items = new ArrayList<>();
		for(int i = 0; i < 20; i++){
			items.add(String.format("item %02d", i));
		}
		return items;
	}

	@Override
	public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
		itemTouchHelper.startDrag(viewHolder);
	}
}
