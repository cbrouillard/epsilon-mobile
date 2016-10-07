package com.headbangers.epsilon.v3.activity;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.adapter.BudgetsAdapter;
import com.headbangers.epsilon.v3.async.BudgetsListAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Budget;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static com.headbangers.epsilon.v3.activity.AccountDetailActivity.FROM_DETAILS_ACTIVITY;

@EActivity(R.layout.scheduleds)
public class BudgetsActivity extends AbstractEpsilonActivity implements Refreshable<List<Budget>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.budget_list);
        toolbar.setSubtitle(R.string.budget_list_subtitle);
        setSupportActionBar(toolbar);

        if (isLogged()) {
            init();
        }
    }

    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    @ItemClick(R.id.list)
    void listClick(Budget budget) {
        BudgetDetailActivity_.intent(this).extra("budget", budget).start();
    }

    private void init() {
        new BudgetsListAsyncLoader(accessService, this, progressBar).execute(token());
    }

    @Override
    public void refresh(List<Budget> result) {
        if (result != null) {
            BudgetsAdapter budgetsAdapter = new BudgetsAdapter(this, result);
            list.setAdapter(budgetsAdapter);
        } else {
            Toast.makeText(this, errorLoading, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
