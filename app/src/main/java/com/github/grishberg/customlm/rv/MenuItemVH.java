package com.github.grishberg.customlm.rv;
import android.view.*;
import com.github.grishberg.customlm.*;
import android.widget.*;

public class MenuItemVH extends BaseViewHolder
{
	private final TextView titleView;
	MenuItemVH(View v){
		super(v);
		titleView = v.findViewById(R.id.title);
	}

	@Override
	void updateTitle(String title) {
		titleView.setText(title);
	}
	
	
}
