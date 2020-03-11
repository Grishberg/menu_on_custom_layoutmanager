package com.github.grishberg.customlm.rv;
import android.support.v7.widget.*;
import android.content.*;
import android.util.*;

public class MenuRecyclerView extends RecyclerView
{
	private SizeChangedListener listener;
	
	public MenuRecyclerView(Context c){
		super(c);
	}
	
	public MenuRecyclerView(Context c, AttributeSet attrs){
		super(c, attrs);
	}
	
	public MenuRecyclerView(Context c, AttributeSet a, int s){
		super(c, a, s);
	}
	
	public void setSizeChangeListener(SizeChangedListener l){
		listener = l;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		// TODO: Implement this method
		super.onSizeChanged(w, h, oldw, oldh);
		if(listener != null){
			listener.onSizeChanged(w, h);
		}
	}
	
	public interface SizeChangedListener {
		void onSizeChanged(int w, int h);
	}
}
