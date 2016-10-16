package com.headbangers.epsilon.v3.activity.budget;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.activity.operation.DialogEditOperationFragment;
import com.headbangers.epsilon.v3.activity.operation.DialogEditOperationFragment_;
import com.headbangers.epsilon.v3.adapter.OperationsAdapter;
import com.headbangers.epsilon.v3.async.budget.OneBudgetAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.OperationEditable;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Budget;
import com.headbangers.epsilon.v3.model.Operation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.budget_detail)
public class BudgetDetailActivity extends AbstractEpsilonActivity
        implements Refreshable<Budget>, OperationEditable {

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
        colorizeAmount(this.sold, budget.getMaxAmount(), budget.getUsedAmound());

        OperationsAdapter budgetOperations = new OperationsAdapter(this, budget.getOperations());
        list.setAdapter(budgetOperations);
    }

    @Click(R.id.refresh)
    void refreshButton() {
        new OneBudgetAsyncLoader(accessService, this, progressBar).execute(budget.getId());
    }

    @Override
    public void refresh(Budget result) {
        this.budget = result;
        init();
    }

    @Override
    public void afterOperationEdition() {
        refreshButton();
    }

    @ItemClick(R.id.operations)
    void listClick(Operation operation) {
        DialogEditOperationFragment fragment = new DialogEditOperationFragment_();
        fragment.setOperation(operation);
        fragment.setProgressBar(progressBar);
        fragment.show(this.getFragmentManager(), "EDITOPERATION");
    }
}
