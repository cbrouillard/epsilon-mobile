package com.headbangers.epsilon.v3.async;


import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Budget;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.List;

public class BudgetsListAsyncLoader extends
        GenericAsyncLoader<String, List<Budget>> {

    public BudgetsListAsyncLoader(EpsilonAccessService dataService,
                                  Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected List<Budget> doInBackground(String... params) {
        // params[0] = selectmode
        return data.findBudgets(params[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(List<Budget> result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<List<Budget>>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }

}
