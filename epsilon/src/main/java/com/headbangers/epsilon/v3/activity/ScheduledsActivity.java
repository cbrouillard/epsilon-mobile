package com.headbangers.epsilon.v3.activity;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.adapter.ScheduledsAdapter;
import com.headbangers.epsilon.v3.async.ScheduledsListAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Operation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.scheduleds)
public class ScheduledsActivity extends AbstractEpsilonActivity implements Refreshable<List<Operation>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

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
        new ScheduledsListAsyncLoader(accessService, this, progressBar).execute(token());
    }

    @Override
    public void refresh(List<Operation> result) {
        if (result != null) {
            ScheduledsAdapter accountsAdapter = new ScheduledsAdapter(this, result);
            list.setAdapter(accountsAdapter);
        } else {
            Toast.makeText(this, errorLoading, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
