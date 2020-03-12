package com.github.grishberg.customlm.menu;
import com.github.grishberg.customlm.rv.*;

public class MenuState
{
	private int barButtonsCount = 2;
	private int maxButtons = 6;

	public MenuState() {
	}
	
	public void onMoved(int srcPos, int dstPos) {
        if (srcPos <= barButtonsCount && dstPos <= barButtonsCount) {
            return;
        }
        if (srcPos <= barButtonsCount) {
            barButtonsCount--;
        }
        if (dstPos <= barButtonsCount) {
            barButtonsCount++;
        }
        if (barButtonsCount < 1) {
            barButtonsCount = 1;
        }
        if (barButtonsCount > maxButtons) {
            barButtonsCount = maxButtons;
        }
    }
	
	public int getDynamicButtonsCount() {
		return barButtonsCount;
	}
	
	public boolean isTopItem(int pos){
		if(isTwoRowMode()){
			return pos == 0;
		}
		
		return pos <= barButtonsCount;
	}
	
	public boolean isTwoRowMode() {
		return barButtonsCount > 2;
	}
}
