package com.headbangers.epsilon.v3.async;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Budget;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class OneBudgetAsyncLoader extends GenericAsyncLoader<String, Budget> {

    public OneBudgetAsyncLoader(EpsilonAccessService dataService,
                                Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected Budget doInBackground(String... params) {
        return data.getBudget(params[0], params[1]);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(Budget result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<Budget>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }

}
