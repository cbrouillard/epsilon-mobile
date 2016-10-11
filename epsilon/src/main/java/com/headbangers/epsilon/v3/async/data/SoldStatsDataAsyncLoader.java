package com.headbangers.epsilon.v3.async.data;

import android.app.Activity;
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.activity.scheduled.ScheduledsActivity;
import com.headbangers.epsilon.v3.async.GenericAsyncLoader;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import java.util.Map;

public class SoldStatsDataAsyncLoader extends
        GenericAsyncLoader<String, Map<String, Double>> {

    public SoldStatsDataAsyncLoader(EpsilonAccessService dataService, Activity context, ProgressBar progressBar) {
        super(dataService, context, progressBar);
    }

    @Override
    protected Map<String, Double> doInBackground(String... params) {
        return data.retrieveSoldStats();
    }

    @Override
    protected void onPostExecute(Map<String, Double> result) {
        super.onPostExecute(result);

        if (fromContext != null && fromContext instanceof ScheduledsActivity){
            ((ScheduledsActivity)fromContext).refresh(result);
        }
    }
}
