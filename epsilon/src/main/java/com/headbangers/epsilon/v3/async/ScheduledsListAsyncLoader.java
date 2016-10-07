package com.headbangers.epsilon.v3.async;


import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.List;


public class ScheduledsListAsyncLoader extends
        GenericAsyncLoader<String, List<Operation>> {

    public ScheduledsListAsyncLoader(EpsilonAccessService dataService,
                                     Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected List<Operation> doInBackground(String... params) {
        // params[0] = selectmode
        return data.findScheduleds(params[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(List<Operation> result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<List<Operation>>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }

}
