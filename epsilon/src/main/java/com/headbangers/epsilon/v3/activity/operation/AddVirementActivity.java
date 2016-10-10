package com.headbangers.epsilon.v3.activity.operation;

import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.async.operation.AddOperationAsyncLoader;
import com.headbangers.epsilon.v3.async.data.AutoCompleteDataAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.AutoCompleteData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.add_virement)
@OptionsMenu(R.menu.menu_ok)
public class AddVirementActivity extends AbstractEpsilonActivity implements Refreshable<AutoCompleteData> {

    public static final int OPERATION_ADD_DONE = 100;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.sold)
    TextView sold;

    @ViewById(R.id.accountFrom)
    Spinner accountFrom;

    @ViewById(R.id.accountTo)
    Spinner accountTo;

    @ViewById(R.id.category)
    AutoCompleteTextView category;

    @ViewById(R.id.amount)
    EditText amount;

    @Extra("account")
    Account account;

    // loaded accounts
    private List<Account> accounts;

    @AfterViews
    void showDetails() {
        toolbar.setTitle(account.getName());
        toolbar.setSubtitle(R.string.add_virement);
        setSupportActionBar(toolbar);

        sold.setText(df.format(account.getSold()) + "€");

        init();
    }

    private void init() {
        new AutoCompleteDataAsyncLoader(accessService, this, progressBar).execute(
                AutoCompleteDataAsyncLoader.Load.CATEGORY_ACCOUNTS.name());
    }

    @Override
    public void refresh(AutoCompleteData result) {
        if (result != null) {
            this.accounts = result.getAccounts();

            List<String> accountsName = new ArrayList<>();
            for (Account account : accounts) {
                accountsName.add(account.getName() + " - "
                        + df.format(account.getSold()) + "€");
            }

            ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item,
                    accountsName);
            accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.accountFrom.setAdapter(accountAdapter);
            this.accountTo.setAdapter(accountAdapter);

            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_item, result.getCategories());
            this.category.setAdapter(categoryAdapter);
        }
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
            Account accountTo = this.accounts.get(this.accountTo.getSelectedItemPosition());
            Account accountFrom = this.accounts.get(this.accountFrom.getSelectedItemPosition());

            new AddOperationAsyncLoader(accessService, this, progressBar).execute(OperationType.VIREMENT.name(),
                    accountTo.getId(), accountFrom.getId(), amount, category);
        }
    }

    private boolean validateForm() {
        String amount = this.amount.getText().toString();
        String category = this.category.getText().toString();

        if (category == null || category.isEmpty()) {
            this.category.setError(errorFormCategory);
        }

        if (amount == null || amount.isEmpty()) {
            this.amount.setError(errorFormAmount);
        }

        return amount != null && !amount.isEmpty()
                && category != null && !category.isEmpty();
    }
}
