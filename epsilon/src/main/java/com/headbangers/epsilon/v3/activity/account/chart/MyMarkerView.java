package com.headbangers.epsilon.v3.activity.account.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.chart.ChartData;

public class MyMarkerView extends MarkerView {

    private final ChartData result;
    private TextView amount;
    private TextView day;

    public MyMarkerView(Context context, int layoutResource, ChartData result) {
        super(context, layoutResource);
        amount = (TextView) findViewById(R.id.tvContent);
        day = (TextView) findViewById(R.id.tvDay);
        this.result = result;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof PieEntry){
            day.setText(((PieEntry)e).getLabel());
        }else {
            day.setText(result.getData().get((int) e.getX()).getKey());
        }
        amount.setText(Utils.formatNumber(e.getY(), 2, false) + "€");
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
}
