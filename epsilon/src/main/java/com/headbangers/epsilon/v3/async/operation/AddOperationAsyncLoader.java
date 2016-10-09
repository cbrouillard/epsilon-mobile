package com.headbangers.epsilon.v3.async.operation;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import static com.headbangers.epsilon.v3.activity.operation.AddOperationActivity.OPERATION_ADD_DONE;

public class AddOperationAsyncLoader extends
        GenericAsyncLoader<String, SimpleResult> {

    String errorOperationAdd;
    String operationAdded;

    public AddOperationAsyncLoader(EpsilonAccessService dataService,
                                   Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
        this.errorOperationAdd = context.getString(R.string.error_operation_add);
        this.operationAdded = context.getString(R.string.operation_added);
    }

    @Override
    protected SimpleResult doInBackground(String... params) {
        // params[0] = type
        OperationType type = OperationType.valueOf(params[0]);
        if (type == OperationType.DEPENSE) {
            return data.addDepense(params[1], params[2], params[3], params[4], params[5]);
        } else if (type == OperationType.REVENUE) {
            return data.addRevenue(params[1], params[2], params[3], params[4], params[5]);
        } else if (type == OperationType.VIREMENT) {
            return data.addVirement(params[1], params[2], params[3], params[4], params[5]);
        }
        return null;
    }

    @Override
    @SuppressWarnings("static-access")
    protected void onPostExecute(SimpleResult result) {

        if (result != null && result.isOk()) {
            if (fromContext != null) {
                Toast.makeText(fromContext, operationAdded, Toast.LENGTH_LONG).show();
                fromContext.setResult(OPERATION_ADD_DONE);
                fromContext.finish();
            }
        } else {
            Toast.makeText(fromContext, errorOperationAdd, Toast.LENGTH_LONG).show();
        }

        super.onPostExecute(result);
    }

}