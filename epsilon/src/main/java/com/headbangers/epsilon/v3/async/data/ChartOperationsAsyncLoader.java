package com.headbangers.epsilon.v3.async.data;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.activity.AbstractBarChartEpsilonActivity;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

public class ChartOperationsAsyncLoader extends
        GenericAsyncLoader<String, ChartData> {

    public enum LoadFor {
        CATEGORIES, TIERSES, BUDGET;
    }

    private LoadFor loadFor;

    public ChartOperationsAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar, LoadFor loadFor) {
        super(dataService, context, progressBar);
        this.loadFor = loadFor;
    }

    @Override
    protected ChartData doInBackground(String... params) {
        switch (this.loadFor) {
            case CATEGORIES:
                return data.retrieveCategoriesOperationChart(params[0]);
            case TIERSES:
                return data.retrieveTiersesOperationChart(params[0]);
            case BUDGET:
                return data.retrieveBudgetOperationChart(params[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ChartData result) {
        if (fromContext != null && fromContext instanceof AbstractBarChartEpsilonActivity) {
            ((AbstractBarChartEpsilonActivity) fromContext).fillChart(result);
        }

        super.onPostExecute(result);
    }
}
