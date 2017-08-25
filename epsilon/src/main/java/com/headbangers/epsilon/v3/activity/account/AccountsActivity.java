package com.headbangers.epsilon.v3.activity.account;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
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
import com.headbangers.epsilon.v3.activity.category.CategoriesActivity_;
import com.headbangers.epsilon.v3.activity.operation.AddOperationActivity_;
import com.headbangers.epsilon.v3.activity.scheduled.ScheduledsActivity_;
import com.headbangers.epsilon.v3.activity.tiers.TiersesActivity_;
import com.headbangers.epsilon.v3.activity.wish.WishesActivity_;
import com.headbangers.epsilon.v3.adapter.AccountsAdapter;
import com.headbangers.epsilon.v3.adapter.NavigationAdapter;
import com.headbangers.epsilon.v3.async.account.AccountsListAsyncLoader;
import com.headbangers.epsilon.v3.async.data.ChartPieCategoryDataAsyncLoader;
import com.headbangers.epsilon.v3.async.enums.OperationType;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.model.chart.GraphData;
import com.headbangers.epsilon.v3.swipeinlist.accounts.AccountsListSwipeCreator;
import com.headbangers.epsilon.v3.swipeinlist.accounts.AccountsListSwipeListener;
import com.headbangers.epsilon.v3.swipeinlist.operations.OperationsListSwipeListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
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
    SwipeMenuListView list;

    @ViewById(R.id.chart)
    PieChart chart;

    @ViewById(R.id.addFacture)
    FloatingActionButton addFactureOnDefaultAccountButton;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.navigationList)
    ListView drawerList;

    @ViewById(R.id.drawerInsideLayout)
    LinearLayout drawerInsideLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Account defaultAccount;
    private List<Account> accounts;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.account_list);
        toolbar.setSubtitle(R.string.account_list_subtitle);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, toolbar, R.string.action_ok, R.string.action_ok);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        drawerList.setAdapter(new NavigationAdapter(this));
        drawerList.setOnItemClickListener(new DrawerClickListener());

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
            this.accounts = result;

            AccountsAdapter accountsAdapter = new AccountsAdapter(this, result);
            list.setAdapter(accountsAdapter);

            AccountsListSwipeCreator accountsListSwipeCreator = new AccountsListSwipeCreator(this);
            list.setMenuCreator(accountsListSwipeCreator);
            list.setOnMenuItemClickListener(new AccountsListSwipeListener(this, result));

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
        AccountDetailActivity_.intent(this)
                .extra("account", account)
                .extra("accounts", this.accounts.toArray(new Account[this.accounts.size()]))
                .startForResult(FROM_DETAILS_ACTIVITY);
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
        chart.getDescription().setEnabled(false);

        chart.setHoleRadius(30f);
        chart.setTransparentCircleRadius(35f);

        //chart.setMaxAngle(180f);
        //chart.setRotationAngle(180f);

        chart.setRotationEnabled(true);
        chart.setDrawEntryLabels(false);

        chart.getLegend().setEnabled(true);
        chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        chart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);

        new ChartPieCategoryDataAsyncLoader(accessService, this, progressBar).execute();
    }

    public void fillChartWithData(ChartData result) {
        if (result != null && result.getData() != null && !result.getData().isEmpty()) {

            MarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view, result);
            chart.setMarker(mv);

            ArrayList<PieEntry> entries = new ArrayList<>();
            for (GraphData oneData : result.getData()) {
                entries.add(new PieEntry(oneData.getValue().floatValue(), oneData.getKey()));
            }

            ArrayList<Integer> colors = new ArrayList<>();
            for (String oneColor : result.getColors()) {
                colors.add(Color.parseColor(oneColor));
            }

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(colors);
            //if (this.getResources().getConfiguration().orientation ==  Configuration.ORIENTATION_PORTRAIT) {
            //    dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            //}

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(8f);
            //if (this.getResources().getConfiguration().orientation ==  Configuration.ORIENTATION_PORTRAIT) {
            //    data.setValueTextColor(R.color.colorAmount);
            //} else {
                data.setValueTextColor(Color.WHITE);
            //}
            chart.setData(data);
            chart.setVisibility(View.VISIBLE);
            chart.animateY(800, Easing.EasingOption.EaseInOutQuad);
        }
    }

    void showScheduleds() {
        ScheduledsActivity_.intent(this).start();
    }

    void showBudgets() {
        BudgetsActivity_.intent(this).start();
    }

    void showWishes() {
        WishesActivity_.intent(this).start();
    }

    void showCategories() {
        CategoriesActivity_.intent(this).start();
    }

    void showTierses() {
        TiersesActivity_.intent(this).start();
    }

    private class DrawerClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerLayout.closeDrawer(drawerInsideLayout);
            switch (position) {
                case 0:
                    showScheduleds();
                    return;
                case 1:
                    showBudgets();
                    return;
                case 2:
                    showWishes();
                    return;
                case 3:
                    showCategories();
                    return;
                case 4:
                    showTierses();
                    return;
            }
        }
    }
}
