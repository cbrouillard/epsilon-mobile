package com.headbangers.epsilon.v3;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.headbangers.epsilon.v3.adapter.AccountsListAdapter;
import com.headbangers.epsilon.v3.async.AccountsListAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;
import com.headbangers.epsilon.v3.service.impl.EpsilonAccessServiceImpl;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.accounts)
@OptionsMenu(R.menu.accounts)
public class AccountsActivity extends AppCompatActivity implements Refreshable<List<Account>> {

    @Pref
    EpsilonPrefs_ epsilonPrefs;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.list)
    ListView list;

    @Bean
    EpsilonAccessServiceImpl accessService;

    @AfterViews
    void bindToolbar() {
        setSupportActionBar(toolbar);
    }

    @AfterInject
    void initData() {
        String authToken = epsilonPrefs.token().get();

        if (authToken == null || "".equals(authToken)) {
            // goto AuthActivity
            AuthActivity_.intent(this).start();
        } else {
            // charge des comptes
            new AccountsListAsyncLoader(this.accessService, this).execute(authToken);
        }
    }


    @Override
    public void refresh(List<Account> result) {
        if (result != null) {
            AccountsListAdapter accountsListAdapter = new AccountsListAdapter(this, result);
            list.setAdapter(accountsListAdapter);
        } else {
            Toast.makeText(this, "Erreur lors du chargement.", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
