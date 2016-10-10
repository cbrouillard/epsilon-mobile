package com.headbangers.epsilon.v3.async.data;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.AutoCompleteData;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;


public class AutoCompleteDataAsyncLoader extends
        GenericAsyncLoader<String, AutoCompleteData> {

    public enum Load {
        ALL, CATEGORY_ONLY, TIERS_ONLY, ACCOUNT_ONLY, CATEGORY_TIERS, CATEGORY_ACCOUNTS;
    }

    public AutoCompleteDataAsyncLoader(EpsilonAccessService dataService,
                                       Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected AutoCompleteData doInBackground(String... params) {

        Load toLoad = Load.valueOf(params[0]);
        AutoCompleteData autoCompleteData = new AutoCompleteData();
        switch (toLoad) {
            case CATEGORY_ONLY:
                autoCompleteData.setCategories(data.findCategoriesName());
                break;
            case TIERS_ONLY:
                autoCompleteData.setTiers(data.findTiersName());
                break;
            case ACCOUNT_ONLY:
                autoCompleteData.setAccounts(data.findAccounts());
                break;
            case CATEGORY_TIERS:
                autoCompleteData.setCategories(data.findCategoriesName());
                autoCompleteData.setTiers(data.findTiersName());
                break;
            case CATEGORY_ACCOUNTS:
                autoCompleteData.setCategories(data.findCategoriesName());
                autoCompleteData.setAccounts(data.findAccounts());
                break;
            case ALL:
                autoCompleteData.setCategories(data.findCategoriesName());
                autoCompleteData.setTiers(data.findTiersName());
                autoCompleteData.setAccounts(data.findAccounts());
                break;
        }

        return autoCompleteData;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(AutoCompleteData result) {
        if (fromContext != null && fromContext instanceof Refreshable) {
            ((Refreshable<AutoCompleteData>) fromContext).refresh(result);
        }
        super.onPostExecute(result);
    }

}
