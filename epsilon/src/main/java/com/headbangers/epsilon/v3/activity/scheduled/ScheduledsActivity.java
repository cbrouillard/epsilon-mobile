package com.headbangers.epsilon.v3.activity.scheduled;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.adapter.ScheduledsAdapter;
import com.headbangers.epsilon.v3.async.data.SoldStatsDataAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.scheduled.ScheduledsListAsyncLoader;
import com.headbangers.epsilon.v3.model.Scheduled;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Map;

@EActivity(R.layout.scheduleds)
public class ScheduledsActivity extends AbstractEpsilonActivity implements Refreshable<List<Scheduled>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

    @ViewById(R.id.soldStats)
    LinearLayout soldStats;

    @ViewById(R.id.spent)
    TextView spent;
    @ViewById(R.id.revenue)
    TextView revenue;
    @ViewById(R.id.threshold)
    TextView threshold;
    @ViewById(R.id.saving)
    TextView saving;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.scheduled_list);
        toolbar.setSubtitle(R.string.scheduled_list_subtitle);
        setSupportActionBar(toolbar);

        if (isLogged()) {
            init();
        }
    }

    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    private void init() {
        new ScheduledsListAsyncLoader(accessService, this, progressBar).execute();
    }

    @Override
    public void refresh(List<Scheduled> result) {
        if (result != null) {
            ScheduledsAdapter scheduledsAdapter = new ScheduledsAdapter(this, result);
            list.setAdapter(scheduledsAdapter);

            new SoldStatsDataAsyncLoader(accessService, this, progressBar).execute();
        } else {
            Toast.makeText(this, errorLoading, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void refresh(Map<String, Double> soldStats) {
        Double spent = soldStats.get("spent");
        Double revenue = soldStats.get("revenue");
        Double threshold = soldStats.get("threshold");
        Double saving = soldStats.get("saving");

        this.spent.setText(df.format(spent) + "€");
        this.revenue.setText(df.format(revenue) + "€");
        this.threshold.setText(df.format(threshold) + "€");
        this.saving.setText(df.format(saving) + "€");

        int pL = this.saving.getPaddingLeft();
        int pT = this.saving.getPaddingTop();
        int pR = this.saving.getPaddingRight();
        int pB = this.saving.getPaddingBottom();
        if (saving > 0){
            this.saving.setBackgroundResource(R.drawable.span_ok);
        } else {
            this.saving.setBackgroundResource(R.drawable.span_ko);
        }
        this.saving.setPadding(pL, pT, pR, pB);

        this.soldStats.setVisibility(View.VISIBLE);
    }
}
