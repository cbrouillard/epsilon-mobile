package com.headbangers.epsilon.v3.async.category;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Category;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.List;

public class CategoriesListAsyncLoader extends
        GenericAsyncLoader<String, List<Category>> {

    public CategoriesListAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected List<Category> doInBackground(String... params) {
        return data.findCategories();
    }

    @Override
    protected void onPostExecute(List<Category> result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<List<Category>>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }
}
