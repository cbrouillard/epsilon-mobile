package com.headbangers.epsilon.v3.activity.category;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractBarChartEpsilonActivity;
import com.headbangers.epsilon.v3.activity.shared.swipeinlist.OperationsListSwipeDeleteListener;
import com.headbangers.epsilon.v3.activity.shared.swipeinlist.OperationsListSwipeMenuCreator;
import com.headbangers.epsilon.v3.adapter.OperationsAdapter;
import com.headbangers.epsilon.v3.async.category.OneCategoryAsyncLoader;
import com.headbangers.epsilon.v3.async.data.ChartOperationsAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Category;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.category_details)
public class CategoryDetailActivity extends AbstractBarChartEpsilonActivity implements Refreshable<Category> {

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

    @Extra("category")
    Category category;

    @AfterViews
    void details() {
        toolbar.setTitle(category.getName());
        toolbar.setSubtitle(category.getDescription());
        toolbar.setBackgroundColor(Color.parseColor(category.getColor()));
        setSupportActionBar(toolbar);
        this.setupDefaultBackNavigationOnToolbar();

        init();
    }

    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    private void init() {
        new OneCategoryAsyncLoader(accessService, this, progressBar).execute(category.getId());
    }

    @Override
    public void refresh(Category result) {
        if (result != null) {
            this.category = result;

            toolbar.setTitle(category.getName());
            toolbar.setBackgroundColor(Color.parseColor(category.getColor()));
            OperationsAdapter operationsAdapter = new OperationsAdapter(this, category.getOperations());
            list.setAdapter(operationsAdapter);

            OperationsListSwipeMenuCreator operationsListSwipeMenuCreator = new OperationsListSwipeMenuCreator(this);
            list.setMenuCreator(operationsListSwipeMenuCreator);
            list.setOnMenuItemClickListener(new OperationsListSwipeDeleteListener(accessService, this, this.progressBar, category.getOperations()));

            if (category.getOperations().isEmpty()) {
                noOperationsWarn.setVisibility(View.VISIBLE);
                listOperationsTitle.setVisibility(View.GONE);
            }

            super.initChart();
        }
    }


    @Override
    protected void startLoadChartData() {
        new ChartOperationsAsyncLoader(accessService, this, progressBar, ChartOperationsAsyncLoader.LoadFor.CATEGORIES)
                .execute(this.category.getId());
    }
}
