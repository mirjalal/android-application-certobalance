package com.certoclav.certoscale.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;


/**
 * A class that creates a layout arranged to fit in a QuickActionMenu
 * 
 * Based on the great work done by Mohd Faruq
 *
 */
public class QuickActionItem extends LinearLayout implements Checkable {
    private boolean mChecked;

    private static final int[] CHECKED_STATE_SET = {
        android.R.attr.state_checked
    };

    /**
     * Creates a new Instance of a QuickActionItem
     * 
     * @param context Context to use, usually your Appication or your Activity
     * @param attrs A collection of attributes, as found associated with a tag in an XML document
     */
    public QuickActionItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }
    
    /**
     * Sets the icon for the view
     * 
     * @param drawable The icon for this item 
     */
    public void setImageResource(int resId) {
    	((ImageView)findViewById(R.id.quickaction_icon)).setImageResource(resId);
    }
    
    public void setBackgroundRes(int resid) {
    	((QuickActionItem)findViewById(R.id.quickaction_item_base)).setBackgroundResource(resid);
    }
    
  
    
    /**
     * Sets a label for the view
     * 
     * @parem text The label for this item
     */
    public void setText(String text) {
    	((TextView)findViewById(R.id.quickaction_text)).setText(text);
    }
    
    public void setTextSize(float scaledPixelUnits){
    	((TextView)findViewById(R.id.quickaction_text)).setTextSize(scaledPixelUnits);
    }

	public void alignRight() {
		setGravity(Gravity.RIGHT);
	}
}