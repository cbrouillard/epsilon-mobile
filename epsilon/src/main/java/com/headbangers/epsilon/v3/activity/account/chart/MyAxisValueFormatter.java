package com.headbangers.epsilon.v3.activity.account.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.headbangers.epsilon.v3.model.chart.ChartData;

public class MyAxisValueFormatter implements AxisValueFormatter {

    private final ChartData result;

    public MyAxisValueFormatter(ChartData result) {
        this.result = result;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        return result.getData().get(index).getKey();
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
