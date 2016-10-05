package com.headbangers.epsilon.v3.async;

import android.app.Activity;

import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;


public class RegisterAsyncLoader extends GenericAsyncLoader<String, SimpleResult>{
    
    public RegisterAsyncLoader(EpsilonAccessService dataService,
            Activity context) {
        super(dataService, context);
    }

    @Override
    protected SimpleResult doInBackground(String... params) {
        // server, login, password
        return data.register(params[0], params[1], params[2]);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(SimpleResult result) {
        ((Refreshable<SimpleResult>) fromContext).refresh(result);
        super.onPostExecute(result);
    }
    
}
