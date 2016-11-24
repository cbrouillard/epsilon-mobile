package com.headbangers.epsilon.v3.activity.operation;

import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.headbangers.epsilon.v3.tool.GPSTracker;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.async.data.AutoCompleteDataAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.operation.AddOperationAsyncLoader;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.AutoCompleteData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;

@EActivity(R.layout.add_operation)
@OptionsMenu(R.menu.menu_ok)
public class AddOperationActivity extends AbstractEpsilonActivity implements Refreshable<AutoCompleteData> {

    public static final int OPERATION_ADD_DONE = 100;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.sold)
    TextView sold;

    @ViewById(R.id.category)
    AutoCompleteTextView category;

    @ViewById(R.id.tiers)
    AutoCompleteTextView tiers;

    @ViewById(R.id.amount)
    EditText amount;

    @Extra("account")
    Account account;

    @Extra("operationType")
    OperationType type;

    private GPSTracker gpsTracker;

    @AfterViews
    void showDetails() {
        toolbar.setTitle(account.getName());
        switch (type) {
            case DEPENSE:
                toolbar.setSubtitle(R.string.add_depense);
                break;
            case REVENUE:
                toolbar.setSubtitle(R.string.add_revenue);
                break;
        }

        setSupportActionBar(toolbar);
        this.setupDefaultBackNavigationOnToolbar();

        sold.setText(df.format(account.getSold()) + "€");
        colorizeAmount (this.sold, account.getSold(), 0D);

        init();

        gpsTracker = new GPSTracker(this);
    }

    private void init() {
        new AutoCompleteDataAsyncLoader(accessService, this, progressBar).execute(
                AutoCompleteDataAsyncLoader.Load.CATEGORY_TIERS.name());
    }

    @Override
    public void refresh(AutoCompleteData result) {
        if (result != null) {
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_item, result.getCategories());
            this.category.setAdapter(categoryAdapter);

            ArrayAdapter<String> tiersAdapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_item, result.getTiers());
            this.tiers.setAdapter(tiersAdapter);
        }
    }

    @EditorAction(R.id.tiers)
    void tiersOk() {
        category.requestFocus();
    }

    @EditorAction(R.id.category)
    void categoryOk() {
        amount.requestFocus();
    }

    @OptionsItem(R.id.action_ok)
    @EditorAction(R.id.amount)
    void ok() {

        if (validateForm()) {
            String amount = this.amount.getText().toString();
            String category = this.category.getText().toString();
            String tiers = this.tiers.getText().toString();

            String latitude = null;
            String longitude = null;
            if (gpsTracker.canGetLocation()){
                DecimalFormat gpsDf = new DecimalFormat("0.0000000");
                latitude = gpsDf.format(gpsTracker.getLatitude());
                longitude = gpsDf.format(gpsTracker.getLongitude());
            }

            new AddOperationAsyncLoader(accessService, this, progressBar, gpsTracker).execute(type.name(),
                    account.getId(), amount, category, tiers, latitude, longitude);
        }
    }

    private boolean validateForm() {
        String amount = this.amount.getText().toString();
        String category = this.category.getText().toString();
        String tiers = this.tiers.getText().toString();

        if (tiers == null || tiers.isEmpty()) {
            this.tiers.setError(errorFormTiers);
        }

        if (category == null || category.isEmpty()) {
            this.category.setError(errorFormCategory);
        }

        if (amount == null || amount.isEmpty()) {
            this.amount.setError(errorFormAmount);
        }

        return amount != null && !amount.isEmpty()
                && category != null && !category.isEmpty()
                && tiers != null && !tiers.isEmpty();
    }
}
