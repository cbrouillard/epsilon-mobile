package com.headbangers.epsilon.v3.async.wish;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.wish.AddWishActivity;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import static com.headbangers.epsilon.v3.activity.operation.AddOperationActivity.OPERATION_ADD_DONE;
import static com.headbangers.epsilon.v3.activity.wish.AddWishActivity.ADD_WISH_DONE;

public class AddWishAsyncLoader extends GenericAsyncLoader<String, SimpleResult> {
    public AddWishAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected SimpleResult doInBackground(String... params) {
        // name, price, cateogory, accountId, photoPath
        String name = params[0];
        String price = params[1];
        String category = params[2];
        String accountId = params[3];
        String photoPath = params[4];
        return this.data.addWish (accountId, name, price, category, photoPath);
    }

    @Override
    protected void onPostExecute(SimpleResult result) {
        super.onPostExecute(result);
        if (result != null && result.isOk()) {

            if (fromContext != null) {
                Toast.makeText(fromContext, R.string.wish_added, Toast.LENGTH_LONG).show();
                fromContext.setResult(ADD_WISH_DONE);
                fromContext.finish();
            }
        } else {
            Toast.makeText(fromContext, R.string.error_wish_add, Toast.LENGTH_LONG).show();
        }


    }
}
