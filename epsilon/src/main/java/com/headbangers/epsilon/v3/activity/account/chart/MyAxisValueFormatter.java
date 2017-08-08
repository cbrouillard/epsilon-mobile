package com.headbangers.epsilon.v3.activity.account.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.headbangers.epsilon.v3.model.chart.ChartData;

public class MyAxisValueFormatter implements IAxisValueFormatter {

    private final ChartData result;

    public MyAxisValueFormatter(ChartData result) {
        this.result = result;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        try {
            return result.getData().get(index).getKey();
        } catch (IndexOutOfBoundsException e){
            // why this exception ??
            return "";
        }
    }

}
