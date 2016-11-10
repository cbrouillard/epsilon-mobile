package com.headbangers.epsilon.v3.async.tiers;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Tiers;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class OneTiersAsyncLoader extends GenericAsyncLoader<String, Tiers> {

    public OneTiersAsyncLoader(EpsilonAccessService dataService,
                               Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected Tiers doInBackground(String... params) {
        return data.getTiers(params[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(Tiers result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<Tiers>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }
}
