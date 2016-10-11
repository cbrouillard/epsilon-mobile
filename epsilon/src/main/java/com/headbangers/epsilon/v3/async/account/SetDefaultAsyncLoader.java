package com.headbangers.epsilon.v3.async.account;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class SetDefaultAsyncLoader extends GenericAsyncLoader<String, SimpleResult> {

    String message;

    public SetDefaultAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
        message = context.getString(R.string.account_set_default);
    }

    @Override
    protected SimpleResult doInBackground(String... params) {
        return data.setAccountDefault(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(SimpleResult result) {
        super.onPostExecute(result);
        if (fromContext != null && result != null) {
            Toast.makeText(fromContext, message + " " + result.getCode(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(fromContext, R.string.error_loading, Toast.LENGTH_LONG).show();
        }
    }
}
