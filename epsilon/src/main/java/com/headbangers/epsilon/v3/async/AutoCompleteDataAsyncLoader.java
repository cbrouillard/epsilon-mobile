package com.headbangers.epsilon.v3.async;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.AutoCompleteData;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;


public class AutoCompleteDataAsyncLoader extends
        GenericAsyncLoader<String, AutoCompleteData> {

    public enum Load {
        CATEGORY_TIERS, CATEGORY_ACCOUNTS;
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
        case CATEGORY_TIERS:
            autoCompleteData.setCategories(data.findCategoriesName(params[1]));
            autoCompleteData.setTiers(data.findTiersName(params[1]));
            break;
        case CATEGORY_ACCOUNTS:
            autoCompleteData.setCategories(data.findCategoriesName(params[1]));
            autoCompleteData.setAccounts(data.findAccounts(params[1]));
            break;
        }
        
        return autoCompleteData;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(AutoCompleteData result) {
        ((Refreshable<AutoCompleteData>)fromContext).refresh(result);
        super.onPostExecute(result);
    }

}
