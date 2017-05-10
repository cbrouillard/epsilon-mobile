package com.headbangers.epsilon.v3.activity.account;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.activity.account.chart.MyAxisValueFormatter;
import com.headbangers.epsilon.v3.activity.account.chart.MyMarkerView;
import com.headbangers.epsilon.v3.activity.operation.AddOperationActivity_;
import com.headbangers.epsilon.v3.activity.operation.AddVirementActivity_;
import com.headbangers.epsilon.v3.activity.operation.DialogEditOperationFragment;
import com.headbangers.epsilon.v3.activity.operation.DialogEditOperationFragment_;
import com.headbangers.epsilon.v3.adapter.OperationsAdapter;
import com.headbangers.epsilon.v3.async.account.OneAccountAsyncLoader;
import com.headbangers.epsilon.v3.async.account.SetDefaultAsyncLoader;
import com.headbangers.epsilon.v3.async.data.ChartAccountFutureAsyncLoader;
import com.headbangers.epsilon.v3.async.operation.OperationsListAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.async.enums.OperationsSelectMode;
import com.headbangers.epsilon.v3.async.interfaces.OperationEditable;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.interfaces.Reloadable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.model.chart.GraphData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static com.headbangers.epsilon.v3.activity.operation.AddOperationActivity.OPERATION_ADD_DONE;

@EActivity(R.layout.account_detail)
@OptionsMenu(R.menu.menu_account_detail)
public class AccountDetailActivity extends AbstractEpsilonActivity
        implements Refreshable<List<Operation>>, Reloadable<Account>, OperationEditable {

    public static final int FROM_DETAILS_ACTIVITY = 200;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.sold)
    TextView sold;

    @ViewById(R.id.operations)
    ListView list;

    @ViewById(R.id.chart)
    LineChart chart;

    @ViewById(R.id.link)
    TextView link;

    @Extra("account")
    Account account;

    @AfterViews
    void showDetails() {
        toolbar.setTitle(account.getName());
        toolbar.setSubtitle(openedAt + " " + account.getFormatedDateOpened());
        setSupportActionBar(toolbar);
        this.setupDefaultBackNavigationOnToolbar();

        init();
    }

    void init() {
        sold.setText(df.format(account.getSold()) + "€");
        colorizeAmount(this.sold, account.getSold(), 0D);

        if (account.getUrl() !=null){
            link.setVisibility(View.VISIBLE);
            link.setText("Web: " + account.getUrl());
            link.setMovementMethod(LinkMovementMethod.getInstance());
        }else {
            this.link.setVisibility(View.GONE);
        }

        OperationsAdapter fiveLastOperations = new OperationsAdapter(this, account.getLastFiveOperations());
        list.setAdapter(fiveLastOperations);

        // charge les opérations du mois
        new OperationsListAsyncLoader(accessService, this, progressBar).execute(
                OperationsSelectMode.BYMONTH.name(), account.getId());

        initChart();
    }

    @OptionsMenuItem(R.id.menuSetDefault)
    MenuItem setDefaultItem;

    @OptionsMenuItem(R.id.menuSetDefault)
    void initMenuDefault(MenuItem setDefault) {
        setDefault.setChecked(account.isMobileDefault());
    }

    @OptionsItem(R.id.menuSetDefault)
    void setDefault() {
        setDefaultItem.setChecked(!setDefaultItem.isChecked());
        new SetDefaultAsyncLoader(accessService, this, progressBar).execute(account.getId(), Boolean.toString(setDefaultItem.isChecked()));
    }


    @Override
    @OnActivityResult(OPERATION_ADD_DONE)
    public void afterOperationEdition() {
        new OneAccountAsyncLoader(accessService, this, progressBar).execute(account.getId());
    }

    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    @Click(R.id.addFacture)
    @OptionsItem(R.id.menuAddDepense)
    void addDepense() {
        AddOperationActivity_.intent(this)
                .extra("account", account)
                .extra("operationType", OperationType.DEPENSE)
                .startForResult(OPERATION_ADD_DONE);
    }

    @OptionsItem(R.id.menuAddRevenue)
    void addRevenue() {
        AddOperationActivity_.intent(this)
                .extra("account", account)
                .extra("operationType", OperationType.REVENUE)
                .startForResult(OPERATION_ADD_DONE);
    }

    @OptionsItem(R.id.menuAddVirement)
    void addVirement() {
        AddVirementActivity_.intent(this)
                .extra("account", account)
                .startForResult(OPERATION_ADD_DONE);
    }

    @ItemClick(R.id.operations)
    void listClick(Operation operation) {
        DialogEditOperationFragment fragment = new DialogEditOperationFragment_();
        fragment.setOperation(operation);
        fragment.setProgressBar(progressBar);
        fragment.show(this.getFragmentManager(), "EDITOPERATION");
    }

    @Override
    public void refresh(List<Operation> result) {
        if (result != null && result.size() > 5) {
            OperationsAdapter allOperations = new OperationsAdapter(this, result);
            list.setAdapter(allOperations);
        }
    }

    @Override
    public void reload(Account obj) {
        if (obj != null) {
            this.account = obj;
            init();
        }
    }

    private void initChart() {

        chart.setDescription("");
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);
        chart.setExtraOffsets(0, 0, 20f, 0);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);

        new ChartAccountFutureAsyncLoader(accessService, this, progressBar).execute(this.account.getId());
    }

    public void fillChartWithData(ChartData result) {

        if (result != null && result.getData() != null && !result.getData().isEmpty()) {

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(7f);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new MyAxisValueFormatter(result));

            MarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view, result);
            chart.setMarkerView(mv);

            int color = Color.parseColor(result.getColors().get(0));

            List<Entry> entries = new ArrayList<>();
            for (GraphData oneData : result.getData()) {
                entries.add(new Entry(oneData.getIndex(), oneData.getValue().floatValue()));
            }

            LineDataSet dataSet = new LineDataSet(entries, "");
            dataSet.setColor(color);
            dataSet.setCircleColor(color);
            dataSet.setLineWidth(2f);
            dataSet.setCircleRadius(3f);
            dataSet.setDrawCircleHole(false);
            dataSet.setDrawValues(false);
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(Color.GREEN);

            List<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            chart.setData(new LineData(dataSets));

            chart.getLegend().setEnabled(false);

            chart.setVisibility(View.VISIBLE);
            chart.invalidate();
            chart.animateY(800, Easing.EasingOption.EaseInOutQuad);
        }

    }
}
