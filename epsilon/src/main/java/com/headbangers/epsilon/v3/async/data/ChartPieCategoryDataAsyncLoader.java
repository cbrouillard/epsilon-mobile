package com.headbangers.epsilon.v3.async.data;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.activity.account.AccountsActivity;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class ChartPieCategoryDataAsyncLoader extends
        GenericAsyncLoader<String, ChartData> {

    public ChartPieCategoryDataAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected ChartData doInBackground(String... params) {
        return data.retrieveChartByCategoryData();
    }

    @Override
    protected void onPostExecute(ChartData result) {
        super.onPostExecute(result);

        if (fromContext!=null && fromContext instanceof AccountsActivity){
            ((AccountsActivity)fromContext).fillChartWithData(result);
        }
    }
}
