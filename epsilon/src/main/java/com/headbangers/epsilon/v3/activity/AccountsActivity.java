package com.headbangers.epsilon.v3.activity;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.adapter.AccountsAdapter;
import com.headbangers.epsilon.v3.async.AccountsListAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.accounts)
@OptionsMenu(R.menu.menu_welcome)
public class AccountsActivity extends AbstractEpsilonActivity implements Refreshable<List<Account>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.account_list);
        toolbar.setSubtitle(R.string.account_list_subtitle);
        setSupportActionBar(toolbar);

        if (isLogged()) {
            init();
        }
    }

    @AfterInject
    void initData() {
        if (!isLogged()) {
            // goto AuthActivity
            startAuth();
        }
    }

    @Override
    public void refresh(List<Account> result) {
        if (result != null) {
            AccountsAdapter accountsAdapter = new AccountsAdapter(this, result);
            list.setAdapter(accountsAdapter);
        } else {
            Toast.makeText(this, errorLoading, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    @OptionsItem(R.id.menuAuth)
    void menuAuth() {
        startAuth();
    }

    @OnActivityResult(AuthActivity.AUTH_RESULT)
    void afterAuth() {
        init();
    }

    @ItemClick(R.id.list)
    void listClick(Account account) {
        AccountDetailActivity_.intent(this).extra("account", account).start();
    }

    void init() {
        new AccountsListAsyncLoader(this.accessService, this, progressBar).execute(token());
    }
}
