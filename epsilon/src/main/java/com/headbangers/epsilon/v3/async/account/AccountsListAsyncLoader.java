package com.headbangers.epsilon.v3.async.account;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.List;

public class AccountsListAsyncLoader extends
        GenericAsyncLoader<String, List<Account>> {

    public AccountsListAsyncLoader(EpsilonAccessService dataService,
                                   Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected List<Account> doInBackground(String... params) {
        return data.findAccounts();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(List<Account> result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<List<Account>>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }

}
