package com.headbangers.epsilon.v3.activity.shared.swipeinlist;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.headbangers.epsilon.v3.async.operation.DeleteOperationAsyncLoader;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.service.impl.EpsilonAccessServiceImpl;

import java.util.List;

public class OperationsListSwipeDeleteListener implements SwipeMenuListView.OnMenuItemClickListener {

    private EpsilonAccessServiceImpl accessService;
    private Activity context;
    private ProgressBar progressBar;
    private List<Operation> operations;

    public OperationsListSwipeDeleteListener(EpsilonAccessServiceImpl accessService, Activity context, ProgressBar progressBar, List<Operation> operations) {
        this.accessService = accessService;
        this.context = context;
        this.progressBar = progressBar;
        this.operations = operations;
    }

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
        switch (index) {
            case OperationsListSwipeMenuCreator.DELETE_BTN_POSITION:

                new AlertDialog.Builder(this.context)
                        .setTitle("Suppression")
                        .setMessage("Confirmer la suppression de l'opération ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                new DeleteOperationAsyncLoader(accessService, context, progressBar).execute(operations.get(position).getId());
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                break;
        }

        return false; // close menu with false
    }
}
