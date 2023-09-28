package com.example.memory_mate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.google.android.material.tabs.TabLayout;
import android.os.Handler;

public class CustomTabLayout extends TabLayout {

    private OnTabLongClickListener onTabLongClickListener;
    private boolean isBeingLongPressed = false;
    public CustomTabLayout(Context context) {
        super(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // Record the start time of the touch event
            long startTime = System.currentTimeMillis();

            // Create a handler to post a delayed task for detecting long press
            Handler handler = new Handler();
            Runnable longPressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (isBeingLongPressed) {
                        // Get the currently selected tab and dispatch long click event
                        TabLayout.Tab selectedTab = getTabAt(getSelectedTabPosition());
                        if (selectedTab != null && onTabLongClickListener != null) {
                            onTabLongClickListener.onTabLongClick(selectedTab);
                        }
                    }
                }
            };

            // Post the delayed task for detecting long press
            handler.postDelayed(longPressRunnable, 2000); // 2000 milliseconds (2 seconds)

            // Set a flag to indicate that a long press is being detected
            isBeingLongPressed = true;

            // Return false to allow the touch event to continue
            return false;
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            // Reset the flag when the touch event is released or canceled
            isBeingLongPressed = false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public void setOnTabLongClickListener(OnTabLongClickListener listener) {
        this.onTabLongClickListener = listener;
    }

    public interface OnTabLongClickListener {
        void onTabLongClick(Tab tab);
    }
}
