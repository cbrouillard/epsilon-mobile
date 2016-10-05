package com.headbangers.epsilon.v3.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public abstract class GenericAsyncLoader<P, R> extends AsyncTask<P, Void, R> {

    protected EpsilonAccessService data;
    protected Activity fromContext;

    private ProgressDialog dialog;
    protected String dialogText = null;

    private boolean showDialog = false;

    public GenericAsyncLoader(EpsilonAccessService dataService, Activity context) {
        this.data = dataService;
        this.fromContext = context;
    }

    public GenericAsyncLoader(EpsilonAccessService dataService, Activity context,
            boolean showDialog) {
        this(dataService, context);
        this.showDialog = showDialog;
    }

    @Override
    protected void onPreExecute() {
        if (showDialog) {
            dialog = new ProgressDialog(fromContext);
            dialog.setIndeterminate(true);
            dialog.setMessage(dialogText != null ? dialogText
                    : "En cours de chargement");
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(R result) {
        if (showDialog) {
            dialog.dismiss();
        }
    };

}
