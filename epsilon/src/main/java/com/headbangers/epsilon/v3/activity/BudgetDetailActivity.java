package com.headbangers.epsilon.v3.activity;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.adapter.OperationsAdapter;
import com.headbangers.epsilon.v3.async.OneAccountAsyncLoader;
import com.headbangers.epsilon.v3.async.OneBudgetAsyncLoader;
import com.headbangers.epsilon.v3.async.OperationsListAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.async.enums.OperationsSelectMode;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.interfaces.Reloadable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Budget;
import com.headbangers.epsilon.v3.model.Operation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static com.headbangers.epsilon.v3.activity.AddOperationActivity.OPERATION_ADD_DONE;

@EActivity(R.layout.budget_detail)
public class BudgetDetailActivity extends AbstractEpsilonActivity implements Refreshable<Budget> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.sold)
    TextView sold;

    @ViewById(R.id.operations)
    ListView list;

    @Extra("budget")
    Budget budget;

    @AfterViews
    void showDetails() {
        setSupportActionBar(toolbar);
        init();
    }

    void init() {
        toolbar.setTitle(budget.getName());
        toolbar.setSubtitle(budget.getNote());
        sold.setText(df.format(budget.getUsedAmound()) + " / " + df.format(budget.getMaxAmount()) + "€");

        OperationsAdapter budgetOperations = new OperationsAdapter(this, budget.getOperations());
        list.setAdapter(budgetOperations);
    }

    @Click(R.id.refresh)
    void refreshButton() {
        new OneBudgetAsyncLoader(accessService, this, progressBar).execute(token(), budget.getId());
    }

    @Override
    public void refresh(Budget result) {
        this.budget = result;
        init();
    }
}
