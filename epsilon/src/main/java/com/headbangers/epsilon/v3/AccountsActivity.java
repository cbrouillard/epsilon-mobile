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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
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
            startAuth();
        } else {
            // charge des comptes
            loadAccounts();
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

    @Click(R.id.refresh)
    void refreshButton (){
        loadAccounts();
    }

    @OptionsItem(R.id.menuAuth)
    void menuAuth (){
        startAuth();
    }

    @OnActivityResult(AuthActivity.AUTH_RESULT)
    void afterAuth (){
        loadAccounts();
    }

    @ItemClick(R.id.list)
    void listClick (Account account){
        Toast.makeText(this, "Coucou "+account, Toast.LENGTH_LONG).show();
    }

    private void loadAccounts(){
        new AccountsListAsyncLoader(this.accessService, this).execute(epsilonPrefs.token().get());
    }

    private void startAuth(){
        AuthActivity_.intent(this).startForResult(AuthActivity.AUTH_RESULT);
    }
}
