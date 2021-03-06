package com.headbangers.epsilon.v3.async.operation;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.OperationEditable;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class DeleteOperationAsyncLoader extends GenericAsyncLoader<String, SimpleResult> {

    String suppressOperation;

    public DeleteOperationAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
        this.suppressOperation = context.getString(R.string.suppress_operation);
    }

    @Override
    protected SimpleResult doInBackground(String... params) {
        // 0::id
        return data.deleteOperation(params[0]);
    }

    @Override
    protected void onPostExecute(SimpleResult result) {
        super.onPostExecute(result);
        if (result != null) {
            Toast.makeText(fromContext, suppressOperation + " " + result.getCode(), Toast.LENGTH_LONG).show();

            if (fromContext != null && fromContext instanceof OperationEditable){
                ((OperationEditable)fromContext).afterOperationEdition();
            }
        }
    }
}
