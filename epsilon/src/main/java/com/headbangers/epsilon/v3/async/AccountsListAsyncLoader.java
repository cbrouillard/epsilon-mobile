package com.headbangers.epsilon.v3.async;

import android.app.Activity;

import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.List;

public class AccountsListAsyncLoader extends
        GenericAsyncLoader<String, List<Account>> {

    public AccountsListAsyncLoader(EpsilonAccessService dataService,
            Activity context) {
        super(dataService, context);
    }

    @Override
    protected List<Account> doInBackground(String... params) {
        return data.findAccounts(params[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(List<Account> result) {
         ((Refreshable<List<Account>>) fromContext).refresh(result);
        super.onPostExecute(result);
    }

}
