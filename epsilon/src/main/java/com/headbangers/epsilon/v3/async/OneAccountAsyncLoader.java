package com.headbangers.epsilon.v3.async;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.interfaces.Reloadable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;


public class OneAccountAsyncLoader extends GenericAsyncLoader<String, Account> {

    public OneAccountAsyncLoader(EpsilonAccessService dataService,
                                 Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected Account doInBackground(String... params) {
        return data.getAccount(params[0], params[1]);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(Account result) {
        if (fromContext != null && fromContext instanceof Reloadable) {
            ((Reloadable<Account>) fromContext).reload(result);
        }
        super.onPostExecute(result);
    }

}
