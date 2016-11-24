package com.headbangers.epsilon.v3.activity.tiers;

import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.activity.category.CategoryDetailActivity_;
import com.headbangers.epsilon.v3.adapter.TiersesAdapter;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.tiers.TiersesListAsyncLoader;
import com.headbangers.epsilon.v3.model.Category;
import com.headbangers.epsilon.v3.model.Tiers;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static com.headbangers.epsilon.v3.activity.account.AccountDetailActivity.FROM_DETAILS_ACTIVITY;

@EActivity(R.layout.tierses)
public class TiersesActivity extends AbstractEpsilonActivity implements Refreshable<List<Tiers>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

    @ViewById(R.id.search)
    AutoCompleteTextView search;

    private List<Tiers> tierses;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.tiers_list);
        toolbar.setSubtitle(R.string.tiers_list_subtitle);
        setSupportActionBar(toolbar);
        this.setupDefaultBackNavigationOnToolbar();

        init();
    }

    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    @ItemClick(R.id.list)
    void listClick(Tiers tiers) {
        TiersDetailActivity_.intent(this).extra("tiers", tiers).startForResult(FROM_DETAILS_ACTIVITY);
    }

    @EditorAction(R.id.search)
    void goSearch (){
        String searchString = this.search.getText().toString();
        for (Tiers tiers : tierses){
            if (tiers.getName().equalsIgnoreCase(searchString)){
                TiersDetailActivity_.intent(this).extra("tiers", tiers).startForResult(FROM_DETAILS_ACTIVITY);
                return;
            }
        }

        Toast.makeText(this, R.string.search_invalid, Toast.LENGTH_LONG).show();
    }

    private void init() {
        new TiersesListAsyncLoader(accessService, this, progressBar).execute();
    }

    @Override
    public void refresh(List<Tiers> result) {
        if (result != null) {
            tierses = result;

            TiersesAdapter tiersesAdapter = new TiersesAdapter(this, tierses);
            list.setAdapter(tiersesAdapter);

            List<String> tiersesName = new ArrayList<>();
            for(Tiers tiers : tierses){
                tiersesName.add(tiers.getName());
            }

            ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_item, tiersesName);
            this.search.setAdapter(searchAdapter);
        } else {
            Toast.makeText(this, errorLoading, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
