package com.headbangers.epsilon.v3.async;


import android.app.Activity;

import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.List;

public class OperationsListAsyncLoader extends
        GenericAsyncLoader<String, List<Operation>> {

    public OperationsListAsyncLoader(EpsilonAccessService dataService,
            Activity context) {
        super(dataService, context);
    }

    @Override
    protected List<Operation> doInBackground(String... params) {
        // params[0] = selectmode
        OperationsSelectMode mode = OperationsSelectMode.valueOf(params[0]);
        switch (mode){
        case BYMONTH:
            return data.findMonthOperations(params[1], params[2]);
        case BYCATEGORY:
            return data.findCategoriesOperations(params[1], params[2]);
        case BYTIERS:
            return data.findTiersOperations(params[1], params[2]);
        }
        
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(List<Operation> result) {
         ((Refreshable<List<Operation>>) fromContext).refresh(result);
        super.onPostExecute(result);
    }

}
