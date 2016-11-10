package com.headbangers.epsilon.v3.async.category;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Category;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class OneCategoryAsyncLoader extends GenericAsyncLoader<String, Category> {

    public OneCategoryAsyncLoader(EpsilonAccessService dataService,
                                 Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected Category doInBackground(String... params) {
        return data.getCategory(params[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(Category result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<Category>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }
}
