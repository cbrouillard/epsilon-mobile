package com.headbangers.epsilon.v3.async;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class AddOperationAsyncLoader extends
        GenericAsyncLoader<String, SimpleResult> {

    public AddOperationAsyncLoader(EpsilonAccessService dataService,
            Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected SimpleResult doInBackground(String... params) {
        // params[0] = type
        /*OperationType type = OperationType.valueOf(params[0]);
        if (type==OperationType.DEPENSE){
            return data.addDepense(params[1], params[2], params[3], params[4], params[5]);
        } else if (type==OperationType.REVENUE){
            return data.addRevenue(params[1], params[2], params[3], params[4], params[5]);
        } else if (type == OperationType.VIREMENT){
            return data.addVirement(params[1], params[2], params[3], params[4], params[5]);
        }*/
        return null;
    }

    @Override
    @SuppressWarnings("static-access")
    protected void onPostExecute(SimpleResult result) {

        if (result != null && result.isOk()) {
            fromContext.setResult(fromContext.RESULT_OK);
            fromContext.finish();
        } else {
            Toast.makeText(fromContext,
                    "Impossible d'ajouter l'opération. Reessayez!", Toast.LENGTH_LONG)
                    .show();
        }

        super.onPostExecute(result);
    }

}
