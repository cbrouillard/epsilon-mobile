package com.headbangers.epsilon.v3.activity;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.adapter.AccountsAdapter;
import com.headbangers.epsilon.v3.adapter.OperationsAdapter;
import com.headbangers.epsilon.v3.async.OperationsListAsyncLoader;
import com.headbangers.epsilon.v3.async.OperationsSelectMode;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Operation;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.util.List;

@EActivity(R.layout.account_detail)
public class AccountDetailActivity extends AbstractEpsilonActivity implements Refreshable<List<Operation>> {

    private static DecimalFormat df = new DecimalFormat("0.00");

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.sold)
    TextView sold;

    @ViewById(R.id.operations)
    ListView list;

    @Extra("account")
    Account account;

    @AfterViews
    void showDetails() {
        toolbar.setTitle(account.getName());
        setSupportActionBar(toolbar);

        sold.setText(df.format(account.getSold()) + "€");

        OperationsAdapter fiveLastOperations = new OperationsAdapter(this, account.getLastFiveOperations());
        list.setAdapter(fiveLastOperations);

        init();
    }

    void init() {
        // charge les opérations du mois
        new OperationsListAsyncLoader(accessService, this, progressBar).execute(
                OperationsSelectMode.BYMONTH.name(), token(), account.getId());
    }

    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    @Override
    public void refresh(List<Operation> result) {
        if (result != null && result.size() > 5) {
            OperationsAdapter allOperations = new OperationsAdapter(this, result);
            list.setAdapter(allOperations);
        }
    }
}
