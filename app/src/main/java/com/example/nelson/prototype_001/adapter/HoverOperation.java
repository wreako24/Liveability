package com.example.nelson.prototype_001.adapter;

/**
 * Created by Nelson on 26/10/2017.
 */


import android.graphics.Rect;
import android.view.View;

public interface HoverOperation {
    abstract void hoverEnded(DynamicRecyclingView dynamicListView, long stableID, int currentPosition, int originalPosition, Rect hoverCellBounds, Rect viewBounds);

    abstract void hoverPosition(DynamicRecyclingView dynamicListView, long stableID, int currentPosition, int originalPosition, Rect hoverCellBounds, Rect viewBounds);

    abstract void viewSwitched(DynamicRecyclingView dynamicListView, long stableID, int position, View oldView, View newView);
}