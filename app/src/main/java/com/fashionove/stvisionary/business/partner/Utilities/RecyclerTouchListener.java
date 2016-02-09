package com.fashionove.stvisionary.business.partner.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.fashionove.stvisionary.business.partner.Interface.ClickListener;

/**
 * Created by developer on 03/02/16.
 */
public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private ClickListener clickListener;
    private GestureDetector gestureDetector;

    public RecyclerTouchListener(Context context, RecyclerView recyclerView, final ClickListener storeClickListener)
    {
        this.clickListener = storeClickListener;
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if(child!=null && clickListener !=null && gestureDetector.onTouchEvent(motionEvent))
        {
            clickListener.onClick(child,recyclerView.getChildPosition(child));

        }

        return false;

    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
