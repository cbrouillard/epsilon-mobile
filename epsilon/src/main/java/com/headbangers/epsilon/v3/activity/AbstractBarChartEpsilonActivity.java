package com.headbangers.epsilon.v3.activity;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.account.chart.MyAxisValueFormatter;
import com.headbangers.epsilon.v3.activity.account.chart.MyMarkerView;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.model.chart.GraphData;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity
public abstract class AbstractBarChartEpsilonActivity extends AbstractEpsilonActivity {

    @ViewById(R.id.chart)
    protected CombinedChart chart;

    protected void initChart() {
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(true);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.getLegend().setEnabled(false);
        //chart.setFitBars(true);
        chart.setMinOffset(0f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);


        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });


        startLoadChartData();
    }

    protected abstract void startLoadChartData();

    public void fillChart(ChartData result) {
        if (result != null && result.getData() != null && !result.getData().isEmpty()) {

            List<BarEntry> entries = new ArrayList<>();
            List<Entry> avgEntries = new ArrayList<>();
            Double sumEntries = 0D;
            for (GraphData oneData : result.getData()) {
                entries.add(new BarEntry(oneData.getIndex(), oneData.getValue().floatValue()));
                sumEntries += oneData.getValue().floatValue();
            }
            Double avgValue = (sumEntries / entries.size());
            for (GraphData oneData : result.getData()) {
                avgEntries.add(new Entry(oneData.getIndex(), avgValue.floatValue()));
            }

            ArrayList<Integer> colors = new ArrayList<>();
            for (String oneColor : result.getColors()) {
                colors.add(Color.parseColor(oneColor));
            }

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(7f);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new MyAxisValueFormatter(result));

            MarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view, result);
            ((MyMarkerView) mv).setAverageLine(true);
            chart.setMarker(mv);

            BarDataSet dataSet = new BarDataSet(entries, "");
            dataSet.setColors(colors);
            BarData dataBar = new BarData(dataSet);
            dataBar.setBarWidth(0.9f);
            dataBar.setDrawValues(false);

            LineDataSet lineDataSet = new LineDataSet(avgEntries, "");
            lineDataSet.setColor(Color.parseColor("#555555"));
            lineDataSet.setCircleColor(Color.parseColor("#aaaaaa"));
            lineDataSet.setLineWidth(2.0f);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setDrawCircles(true);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setFillAlpha(128);
            lineDataSet.setFillColor(Color.WHITE);
            LineData dataLine = new LineData(lineDataSet);
            dataLine.setDrawValues(false);

            CombinedData data = new CombinedData();
            data.setData(dataBar);
            data.setData(dataLine);

            chart.setData(data);

            chart.highlightValues(null);
            chart.setVisibility(View.VISIBLE);
            chart.invalidate();
            chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        }
    }
}
