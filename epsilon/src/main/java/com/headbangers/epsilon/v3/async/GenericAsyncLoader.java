package com.headbangers.epsilon.v3.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public abstract class GenericAsyncLoader<P, R> extends AsyncTask<P, Void, R> {

    protected EpsilonAccessService data;
    protected Activity fromContext;

    protected ProgressBar progressBar;

    private boolean manageProgressBar = true;

    public GenericAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        this.data = dataService;
        this.fromContext = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {

        if (manageProgressBar) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostExecute(R result) {
        if (manageProgressBar) {
            //dialog.dismiss();
            progressBar.setVisibility(View.GONE);
        }
    };

    protected void setManageProgressBar(boolean manageProgressBar){
        this.manageProgressBar = manageProgressBar;
    }

}
