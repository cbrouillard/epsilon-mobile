package com.headbangers.epsilon.v3.swipeinlist.accounts;

import android.app.Activity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.headbangers.epsilon.v3.activity.operation.AddOperationActivity_;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.model.Account;

import java.util.List;

import static com.headbangers.epsilon.v3.activity.operation.AddOperationActivity.OPERATION_ADD_DONE;

public class AccountsListSwipeListener implements SwipeMenuListView.OnMenuItemClickListener {

    private Activity context;
    private List<Account> accounts;

    public AccountsListSwipeListener (Activity context, List<Account> accounts){
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                AddOperationActivity_.intent(this.context)
                        .extra("account", this.accounts.get(position))
                        .extra("operationType", OperationType.DEPENSE)
                        .startForResult(OPERATION_ADD_DONE);
                break;
        }

        return false;
    }
}
