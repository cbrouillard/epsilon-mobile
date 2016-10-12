package com.headbangers.epsilon.v3.async.data;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.activity.account.AccountDetailActivity;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class ChartAccountFutureAsyncLoader extends
        GenericAsyncLoader<String, ChartData> {

    public ChartAccountFutureAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected ChartData doInBackground(String... params) {
        // 0: accountId
        return data.retrieveAccountFutureData(params[0]);
    }

    @Override
    protected void onPostExecute(ChartData result) {
        super.onPostExecute(result);

        if (fromContext != null && fromContext instanceof AccountDetailActivity){
            ((AccountDetailActivity)fromContext).fillChartWithData (result);
        }
    }
}
