package com.headbangers.epsilon.v3.async.operation;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.OperationEditable;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class EditOperationAsyncLoader extends GenericAsyncLoader<String, SimpleResult> {

    String editOperation;

    public EditOperationAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
        this.editOperation = context.getString(R.string.edit_operation);
    }

    @Override
    protected SimpleResult doInBackground(String... params) {
        // 0:id, 2:category, 3:tiers, 4:amount
        return data.editOperation(params[0], params[1], params[2], params[3]);
    }

    @Override
    protected void onPostExecute(SimpleResult result) {
        super.onPostExecute(result);
        if (result != null) {
            Toast.makeText(fromContext, editOperation + " " + result.getCode(), Toast.LENGTH_LONG).show();

            if (fromContext != null && fromContext instanceof OperationEditable){
                ((OperationEditable)fromContext).afterOperationEdition();
            }
        }
    }
}
