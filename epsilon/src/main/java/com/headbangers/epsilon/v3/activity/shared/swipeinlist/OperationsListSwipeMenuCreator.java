package com.headbangers.epsilon.v3.activity.shared.swipeinlist;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.headbangers.epsilon.v3.R;

public class OperationsListSwipeMenuCreator implements SwipeMenuCreator {

    public static final int DELETE_BTN_POSITION = 0;

    private Activity context;

    public OperationsListSwipeMenuCreator(Activity context) {
        this.context = context;
    }

    @Override
    public void create(SwipeMenu menu) {

        // create "delete" item
        SwipeMenuItem deleteItem = new SwipeMenuItem(context);
        // set item background
        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
        // set item width
        deleteItem.setWidth(dp2px(50));
        // set a icon
        deleteItem.setIcon(R.drawable.ic_delete);
        // add to menu
        menu.addMenuItem(deleteItem);

    }

    private static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }
}
