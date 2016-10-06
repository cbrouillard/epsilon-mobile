package com.headbangers.epsilon.v3.activity;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.adapter.OperationsAdapter;
import com.headbangers.epsilon.v3.async.OneAccountAsyncLoader;
import com.headbangers.epsilon.v3.async.OperationsListAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.async.enums.OperationsSelectMode;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.interfaces.Reloadable;
import com.headbangers.epsilon.v3.model.Account;
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

@EActivity(R.layout.account_detail)
@OptionsMenu(R.menu.menu_add_operations)
public class AccountDetailActivity extends AbstractEpsilonActivity implements Refreshable<List<Operation>>, Reloadable<Account> {

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
        toolbar.setSubtitle(openedAt + account.getFormatedDateOpened());
        setSupportActionBar(toolbar);

        init();
    }

    void init() {
        sold.setText(df.format(account.getSold()) + "€");

        OperationsAdapter fiveLastOperations = new OperationsAdapter(this, account.getLastFiveOperations());
        list.setAdapter(fiveLastOperations);

        // charge les opérations du mois
        new OperationsListAsyncLoader(accessService, this, progressBar).execute(
                OperationsSelectMode.BYMONTH.name(), token(), account.getId());
    }

    @OnActivityResult(OPERATION_ADD_DONE)
    void addDone() {
        Toast.makeText(this, operationAdded, Toast.LENGTH_LONG).show();
        // reload account
        new OneAccountAsyncLoader(accessService, this, progressBar).execute(token(), account.getId());
    }


    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    @Click(R.id.addFacture)
    @OptionsItem(R.id.menuAddDepense)
    void addDepense() {
        AddOperationActivity_.intent(this).extra("account", account).extra("operationType", OperationType.DEPENSE).start();
    }

    @OptionsItem(R.id.menuAddRevenue)
    void addRevenue() {
        AddOperationActivity_.intent(this).extra("account", account).extra("operationType", OperationType.REVENUE).start();
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
}
