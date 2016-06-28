package com.softdesign.devintensive.ui.activities.ui.activities;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class FancyLinearLayoutBehavior
        extends CoordinatorLayout.Behavior<LinearLayout> {

    private final String TAG = "JUST_SCROLL_IT";
    /**
     * Default constructor for instantiating a FancyBehavior in code.
     */
    public FancyLinearLayoutBehavior() {
    }
    /**
     * Default constructor for inflating a FancyBehavior from layout.
     *
     * @param context The {@link Context}.
     * @param attrs The {@link AttributeSet}.
     */
    public FancyLinearLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Extract any custom attributes out
        // preferably prefixed with behavior_ to denote they
        // belong to a behavior
        Log.d(TAG, "Scroll3");

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "Scroll1");
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.d(TAG, "Scroll2");
    }
}