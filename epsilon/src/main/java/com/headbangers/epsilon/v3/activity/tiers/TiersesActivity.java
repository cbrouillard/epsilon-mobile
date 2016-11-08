package com.headbangers.epsilon.v3.activity.tiers;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.adapter.TiersesAdapter;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.tiers.TiersesListAsyncLoader;
import com.headbangers.epsilon.v3.model.Tiers;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.tierses)
public class TiersesActivity extends AbstractEpsilonActivity implements Refreshable<List<Tiers>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.tiers_list);
        toolbar.setSubtitle(R.string.tiers_list_subtitle);

        setSupportActionBar(toolbar);

        init();
    }

    private void init() {
        new TiersesListAsyncLoader(accessService, this, progressBar).execute();
    }

    @Override
    public void refresh(List<Tiers> result) {
        if (result != null) {
            TiersesAdapter tiersesAdapter = new TiersesAdapter(this, result);
            list.setAdapter(tiersesAdapter);
        } else {
            Toast.makeText(this, errorLoading, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
