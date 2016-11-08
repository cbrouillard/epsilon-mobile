package com.headbangers.epsilon.v3.async.tiers;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Category;
import com.headbangers.epsilon.v3.model.Tiers;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.List;

public class TiersesListAsyncLoader extends
        GenericAsyncLoader<String, List<Tiers>> {

    public TiersesListAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected List<Tiers> doInBackground(String... params) {
        return data.findTiers();
    }

    @Override
    protected void onPostExecute(List<Tiers> result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<List<Tiers>>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }
}
