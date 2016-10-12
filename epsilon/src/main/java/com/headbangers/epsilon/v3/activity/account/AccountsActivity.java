package com.headbangers.epsilon.v3.activity.account;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.activity.AuthActivity;
import com.headbangers.epsilon.v3.activity.account.chart.MyMarkerView;
import com.headbangers.epsilon.v3.activity.budget.BudgetsActivity_;
import com.headbangers.epsilon.v3.activity.operation.AddOperationActivity_;
import com.headbangers.epsilon.v3.activity.scheduled.ScheduledsActivity_;
import com.headbangers.epsilon.v3.adapter.AccountsAdapter;
import com.headbangers.epsilon.v3.async.account.AccountsListAsyncLoader;
import com.headbangers.epsilon.v3.async.data.ChartCategoryDataAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.model.chart.GraphData;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static com.headbangers.epsilon.v3.activity.account.AccountDetailActivity.FROM_DETAILS_ACTIVITY;
import static com.headbangers.epsilon.v3.activity.operation.AddOperationActivity.OPERATION_ADD_DONE;

@EActivity(R.layout.accounts)
@OptionsMenu(R.menu.menu_welcome)
public class AccountsActivity extends AbstractEpsilonActivity implements Refreshable<List<Account>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

    @ViewById(R.id.chart)
    PieChart chart;

    @ViewById(R.id.addFacture)
    FloatingActionButton addFactureOnDefaultAccountButton;

    private Account defaultAccount;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.account_list);
        toolbar.setSubtitle(R.string.account_list_subtitle);
        setSupportActionBar(toolbar);

        init();
    }

    @AfterInject
    void checkAuth() {
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

            this.defaultAccount = null;
            addFactureOnDefaultAccountButton.setVisibility(View.GONE);
            for (Account account : result) {
                if (account.isMobileDefault()) {
                    this.defaultAccount = account;
                    addFactureOnDefaultAccountButton.setVisibility(View.VISIBLE);
                }
            }

            initChart();
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

    @OptionsItem(R.id.menuScheduled)
    void showScheduleds() {
        ScheduledsActivity_.intent(this).start();
    }

    @OptionsItem(R.id.menuBudgets)
    void showBudgets() {
        BudgetsActivity_.intent(this).start();
    }

    @OnActivityResult(AuthActivity.AUTH_RESULT)
    void afterAuth() {
        this.accessService.refreshServerUrl();
        init();
    }

    @OnActivityResult(FROM_DETAILS_ACTIVITY)
    void fromDetailsActivity() {
        init();
    }

    @OnActivityResult(OPERATION_ADD_DONE)
    void addOperationDone() {
        init();
    }

    @ItemClick(R.id.list)
    void listClick(Account account) {
        AccountDetailActivity_.intent(this).extra("account", account).startForResult(FROM_DETAILS_ACTIVITY);
    }

    @Click(R.id.addFacture)
    void addDepenseOnDefaultAccount() {
        if (this.defaultAccount != null) {
            AddOperationActivity_.intent(this)
                    .extra("account", this.defaultAccount)
                    .extra("operationType", OperationType.DEPENSE)
                    .startForResult(OPERATION_ADD_DONE);
        }
    }

    void init() {
        if (isLogged()) {
            new AccountsListAsyncLoader(this.accessService, this, progressBar).execute();
        }
    }

    private void initChart() {
        chart.setUsePercentValues(true);
        chart.setDescription("");
        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(30f);
        chart.setTransparentCircleRadius(35f);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setDrawEntryLabels(false);

        chart.setEntryLabelColor(R.color.colorAmount);
        chart.setEntryLabelTextSize(12f);

        chart.getLegend().setEnabled(true);
        chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        chart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        chart.getLegend().setDrawInside(false);
        chart.invalidate();

        new ChartCategoryDataAsyncLoader(accessService, this, progressBar).execute();
    }

    public void fillChartWithData(ChartData result) {
        if (result != null && result.getData() != null && !result.getData().isEmpty()) {

            MarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view, result);
            chart.setMarkerView(mv);

            ArrayList<PieEntry> entries = new ArrayList<>();
            for (GraphData oneData : result.getData()) {
                entries.add(new PieEntry(oneData.getValue().floatValue(), oneData.getKey()));
            }

            ArrayList<Integer> colors = new ArrayList<>();
            for (String oneColor : result.getColors()) {
                colors.add(Color.parseColor(oneColor));
            }

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(10f);
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            chart.setData(data);

            chart.highlightValues(null);
            chart.setVisibility(View.VISIBLE);
            chart.invalidate();
            chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        }
    }
}
