package io.github.coffeegerm.materiallogbook.ui;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by David Yarzebinski on 7/14/2017.
 * <p>
 * Class dedicated to create the animation for FAB to slide below screen.
 */

class fabBehavior extends FloatingActionButton.Behavior {

    // Constructor for construction's sake
    fabBehavior() {
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, floatingActionButton, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        //child -> Floating Action Button
        if (dyConsumed > 0) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
            int fab_bottomMargin = layoutParams.bottomMargin;
            floatingActionButton.animate().translationY(floatingActionButton.getHeight() + fab_bottomMargin).setInterpolator(new LinearInterpolator()).start();
        } else if (dyConsumed < 0) {
            floatingActionButton.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

}
