package com.github.grishberg.customlm.rv;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;

abstract class BaseViewHolder extends RecyclerView.ViewHolder
{
	BaseViewHolder(View v){
		super(v);
	}
	
	void updateTitle(String title){}
	
	void setOnTouchListener(View.OnTouchListener listener){}
}
