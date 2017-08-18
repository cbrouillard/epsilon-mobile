package com.headbangers.epsilon.v3.activity.tiers;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractBarChartEpsilonActivity;
import com.headbangers.epsilon.v3.swipeinlist.operations.OperationsListSwipeListener;
import com.headbangers.epsilon.v3.swipeinlist.operations.OperationsListSwipeCreator;
import com.headbangers.epsilon.v3.adapter.OperationsAdapter;
import com.headbangers.epsilon.v3.async.data.ChartOperationsAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.tiers.OneTiersAsyncLoader;
import com.headbangers.epsilon.v3.model.Tiers;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.tiers_details)
public class TiersDetailActivity extends AbstractBarChartEpsilonActivity implements Refreshable<Tiers> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    SwipeMenuListView list;

    @ViewById(R.id.noOperationsWarn)
    TextView noOperationsWarn;

    @ViewById(R.id.listOperationsTitle)
    TextView listOperationsTitle;

    @Extra("tiers")
    Tiers tiers;

    @AfterViews
    void details() {
        toolbar.setTitle(tiers.getName());
        toolbar.setSubtitle(tiers.getDescription());
        toolbar.setBackgroundColor(Color.parseColor(tiers.getColor()));
        setSupportActionBar(toolbar);
        this.setupDefaultBackNavigationOnToolbar();

        init();
    }

    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    private void init() {
        new OneTiersAsyncLoader(accessService, this, progressBar).execute(tiers.getId());
    }

    @Override
    public void refresh(Tiers result) {
        if (result != null) {
            this.tiers = result;

            toolbar.setTitle(tiers.getName());
            toolbar.setBackgroundColor(Color.parseColor(tiers.getColor()));
            OperationsAdapter operationsAdapter = new OperationsAdapter(this, tiers.getOperations());
            list.setAdapter(operationsAdapter);

            OperationsListSwipeCreator operationsListSwipeCreator = new OperationsListSwipeCreator(this);
            list.setMenuCreator(operationsListSwipeCreator);
            list.setOnMenuItemClickListener(new OperationsListSwipeListener(accessService, this, this.progressBar, tiers.getOperations()));

            if (tiers.getOperations().isEmpty()) {
                noOperationsWarn.setVisibility(View.VISIBLE);
                listOperationsTitle.setVisibility(View.GONE);
            }

            super.initChart();
        }
    }

    @Override
    protected void startLoadChartData() {
        new ChartOperationsAsyncLoader(accessService, this, progressBar, ChartOperationsAsyncLoader.LoadFor.TIERSES)
                .execute(this.tiers.getId());
    }
}