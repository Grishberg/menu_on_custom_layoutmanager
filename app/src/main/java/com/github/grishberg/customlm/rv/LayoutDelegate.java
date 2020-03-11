package com.github.grishberg.customlm.rv;
import android.support.v7.widget.*;
import android.view.*;

public class LayoutDelegate {
	private final RecyclerView.LayoutManager lm;
	private final AddressBarState addressBar = new AddressBarState();
	private final ButtonsRowState buttonsRow = new ButtonsRowState();
	private final MenuItemsState menuItems = new MenuItemsState();
	private State state = addressBar;
	private int screenWidth;
	private int screenHeight;
	private int viewLeft;
	private int viewTop;
	private int buttonsInRowCount;

	public LayoutDelegate(RecyclerView.LayoutManager lm) {
		this.lm = lm;
	}
	
	public void beforeLayout(int offset, int w, int h,
				int buttonsInRow){
		viewTop = offset;
		screenWidth = w;
		screenHeight = h;
		buttonsInRowCount = buttonsInRow;
	}
	
	public void layoutChuld(View child, int w, int h){
		state.layoutChild(child, w, h);
	}
	
	private void layout(View child, int w, int h){
		lm.layoutDecorated(child,
						   viewLeft,
						   viewTop,
						   viewLeft + w,
						   viewTop + h
						   );
	}
	
	private void nextState(State nextState){
		state = nextState;
		state.activateState();
	}
	
	private class AddressBarState implements State {

		@Override
		public void activateState() {
			// TODO: Implement this method
		}

 
		@Override
		public void layoutChild(View child, int w, int h) {
			layout(child, w, h);
			viewTop += h;
			nextState(buttonsRow);
		}
	}
	
	private class ButtonsRowState implements State {
		private int itemIndex;
		private int childWidth;
		
		@Override
		public void activateState() {
			itemIndex = 0;
			viewLeft = 0;
			childWidth
		}
		
		@Override
		public void layoutChild(View child, int w, int h) {
		layout(child, 
			itemIndex++;
			
			if (itemIndex == buttonsInRowCount){
				nextState(menuItems);
			}
		}
	}
	
	private class MenuItemsState implements State{

		@Override
		public void activateState() {
			// TODO: Implement this method
		}

		@Override
		public void layoutChild(View child, int w, int h) {
			// TODO: Implement this method
		}
	}
	
	private interface State {
		void layoutChild(View child, int w, int h);
		void activateState();
	}
}
