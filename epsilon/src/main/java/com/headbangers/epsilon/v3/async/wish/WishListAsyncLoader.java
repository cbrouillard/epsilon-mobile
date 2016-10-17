package com.headbangers.epsilon.v3.async.wish;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Wish;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.List;

public class WishListAsyncLoader extends GenericAsyncLoader<String, List<Wish>>{

    public WishListAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected List<Wish> doInBackground(String... params) {
        return this.data.findWishes();
    }

    @Override
    protected void onPostExecute(List<Wish> result) {
        if (fromContext != null && fromContext instanceof Refreshable){
            ((Refreshable) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }
}
