package com.headbangers.epsilon.v3.async.operation;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;
import com.headbangers.epsilon.v3.tool.GPSTracker;

import static com.headbangers.epsilon.v3.activity.operation.AddOperationActivity.OPERATION_ADD_DONE;

public class AddOperationAsyncLoader extends
        GenericAsyncLoader<String, SimpleResult> {

    private final GPSTracker gps;
    String errorOperationAdd;
    String operationAdded;

    public AddOperationAsyncLoader(EpsilonAccessService dataService,
                                   Activity context, ProgressBar progressBar, GPSTracker gps) {
        super(dataService, context, progressBar);
        this.errorOperationAdd = context.getString(R.string.error_operation_add);
        this.operationAdded = context.getString(R.string.operation_added);
        this.gps = gps;
    }

    @Override
    protected SimpleResult doInBackground(String... params) {
        // params[0] = type, 5: lattitude, 6:longitude pour la dépense
        OperationType type = OperationType.valueOf(params[0]);
        if (type == OperationType.DEPENSE) {
            return data.addDepense(params[1], params[2], params[3], params[4], params[5], params[6]);
        } else if (type == OperationType.REVENUE) {
            return data.addRevenue(params[1], params[2], params[3], params[4]);
        } else if (type == OperationType.VIREMENT) {
            return data.addVirement(params[1], params[2], params[3], params[4]);
        }
        return null;
    }

    @Override
    @SuppressWarnings("static-access")
    protected void onPostExecute(SimpleResult result) {
        super.onPostExecute(result);

        if (result != null && result.isOk()) {
            if (fromContext != null) {
                if (gps != null) {
                    gps.stopUsingGPS();
                }
                Toast.makeText(fromContext, operationAdded, Toast.LENGTH_LONG).show();
                fromContext.setResult(OPERATION_ADD_DONE);
                fromContext.finish();
            }
        } else {
            Toast.makeText(fromContext, errorOperationAdd, Toast.LENGTH_LONG).show();
        }

    }

}
