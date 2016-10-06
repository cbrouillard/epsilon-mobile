package com.headbangers.epsilon.v3.activity;

import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.AddOperationAsyncLoader;
import com.headbangers.epsilon.v3.async.AutoCompleteDataAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.AutoCompleteData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

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

        sold.setText(df.format(account.getSold()) + "€");

        init();
    }

    private void init() {
        new AutoCompleteDataAsyncLoader(accessService, this, progressBar).execute(
                AutoCompleteDataAsyncLoader.Load.CATEGORY_TIERS.name(), token());
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

    @OptionsItem(R.id.action_ok)
    @EditorAction(R.id.amount)
    void ok() {

        if (validateForm()) {
            String amount = this.amount.getText().toString();
            String category = this.category.getText().toString();
            String tiers = this.tiers.getText().toString();

            new AddOperationAsyncLoader(accessService, this, progressBar).execute(type.name(), token(),
                    account.getId(), amount, category, tiers);
        }
    }

    @OnActivityResult(OPERATION_ADD_DONE)
    void addDone() {
        setResult(OPERATION_ADD_DONE);
        finish();
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
