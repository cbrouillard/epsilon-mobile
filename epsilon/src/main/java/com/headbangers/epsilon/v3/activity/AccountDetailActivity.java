package com.headbangers.epsilon.v3.activity;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.Account;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;

@EActivity(R.layout.account_detail)
public class AccountDetailActivity extends AbstractEpsilonActivity{

    private static DecimalFormat df = new DecimalFormat("0.00");

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @Extra ("account")
    Account account;

    @ViewById(R.id.sold)
    TextView sold;

    @AfterViews
    void showDetails() {
        toolbar.setTitle(account.getName());
        setSupportActionBar(toolbar);

        sold.setText(df.format(account.getSold()) + "€");
    }

    @Override
    void init() {
        // charge les opérations du mois
    }
}
