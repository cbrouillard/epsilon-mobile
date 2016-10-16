package com.headbangers.epsilon.v3.activity.budget;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.adapter.BudgetsAdapter;
import com.headbangers.epsilon.v3.async.budget.BudgetsListAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Budget;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.budgets)
public class BudgetsActivity extends AbstractEpsilonActivity implements Refreshable<List<Budget>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

    @ViewById(R.id.used)
    TextView usedAmount;

    @ViewById(R.id.infos_aboutsold)
    LinearLayout infosSold;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.budget_list);
        toolbar.setSubtitle(R.string.budget_list_subtitle);
        setSupportActionBar(toolbar);

        usedAmount.setText("");

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
        new BudgetsListAsyncLoader(accessService, this, progressBar).execute();
    }

    @Override
    public void refresh(List<Budget> result) {
        if (result != null) {
            BudgetsAdapter budgetsAdapter = new BudgetsAdapter(this, result);
            list.setAdapter(budgetsAdapter);

            Double currentUsedAmount = 0D;
            Double totalMaxAmount = 0D;
            for (Budget budget : result) {
                currentUsedAmount += budget.getUsedAmound();
                totalMaxAmount += budget.getMaxAmount();
            }

            usedAmount.setText(df.format(currentUsedAmount) + " / " + df.format(totalMaxAmount) + "€");
            colorizeAmount(this.usedAmount, totalMaxAmount, currentUsedAmount);

            infosSold.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(this, errorLoading, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
