package com.headbangers.epsilon.v3.async;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;


public class RegisterAsyncLoader extends GenericAsyncLoader<String, SimpleResult> {

    public RegisterAsyncLoader(EpsilonAccessService dataService,
                               Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected SimpleResult doInBackground(String... params) {
        // server, login, password
        return data.register(params[0], params[1], params[2]);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(SimpleResult result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<SimpleResult>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }

}
