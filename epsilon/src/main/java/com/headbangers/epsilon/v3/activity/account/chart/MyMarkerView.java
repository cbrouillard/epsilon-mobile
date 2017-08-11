package com.headbangers.epsilon.v3.activity.account.chart;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.chart.ChartData;

public class MyMarkerView extends MarkerView {

    private final ChartData result;
    private TextView amount;
    private TextView day;

    private boolean isAverageLine = false;

    private static DisplayMetrics metrics = new DisplayMetrics();

    public MyMarkerView(Context context, int layoutResource, ChartData result) {
        super(context, layoutResource);
        amount = (TextView) findViewById(R.id.tvContent);
        day = (TextView) findViewById(R.id.tvDay);
        this.result = result;

        if (context instanceof Activity) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
    }

    public void setAverageLine(boolean averageLine) {
        isAverageLine = averageLine;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof PieEntry) {
            day.setText(((PieEntry) e).getLabel());
        } else if (e instanceof BarEntry) {
            day.setText(result.getData().get((int) e.getX()).getKey());
        } else {
            if (isAverageLine) {
                day.setText("Moyenne");
            } else {
                day.setText(result.getData().get((int) e.getX()).getKey());
            }
        }
        amount.setText(Utils.formatNumber(e.getY(), 2, false) + "€");
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        return new MPPointF(getXOffset(posX), getYOffset(posY));
    }

    public float getXOffset(float xpos) {
        float threshold = (metrics.widthPixels / 4);
        if (xpos > (metrics.widthPixels - threshold)) {
            return -getWidth();
        }

        if (xpos < threshold) {
            return 0;
        }

        return -(getWidth() / 2);
    }

    public int getYOffset(float ypos) {
        return -(getHeight() / 2);
    }
}
