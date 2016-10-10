package com.headbangers.epsilon.v3.async.scheduled;


import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.model.Scheduled;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.List;


public class ScheduledsListAsyncLoader extends
        GenericAsyncLoader<String, List<Scheduled>> {

    public ScheduledsListAsyncLoader(EpsilonAccessService dataService,
                                     Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected List<Scheduled> doInBackground(String... params) {
        // params[0] = selectmode
        return data.findScheduleds();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(List<Scheduled> result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<List<Scheduled>>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }

}
