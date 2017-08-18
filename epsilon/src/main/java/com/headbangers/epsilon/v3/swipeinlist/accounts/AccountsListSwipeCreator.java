package com.headbangers.epsilon.v3.swipeinlist.accounts;


import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.headbangers.epsilon.v3.R;

public class AccountsListSwipeCreator implements SwipeMenuCreator {

    private Activity context;

    public AccountsListSwipeCreator(Activity context) {
        this.context = context;
    }

    @Override
    public void create(SwipeMenu menu) {
        SwipeMenuItem addDepenseItem = new SwipeMenuItem(context);
        addDepenseItem.setBackground(R.color.colorPrimary);
        addDepenseItem.setWidth(dp2px(50));
        addDepenseItem.setIcon(R.drawable.adddepense_mini);
        menu.addMenuItem(addDepenseItem);
    }

    private static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }
}
