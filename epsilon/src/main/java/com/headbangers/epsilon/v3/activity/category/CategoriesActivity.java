package com.headbangers.epsilon.v3.activity.category;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.adapter.AccountsAdapter;
import com.headbangers.epsilon.v3.adapter.CategoriesAdapter;
import com.headbangers.epsilon.v3.async.category.CategoriesListAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Category;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.categories)
public class CategoriesActivity extends AbstractEpsilonActivity implements Refreshable<List<Category>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.category_list);
        toolbar.setSubtitle(R.string.category_list_subtitle);

        setSupportActionBar(toolbar);

        init();
    }

    private void init() {
        new CategoriesListAsyncLoader(accessService, this, progressBar).execute();
    }

    @Override
    public void refresh(List<Category> result) {
        if (result != null) {
            CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, result);
            list.setAdapter(categoriesAdapter);
        } else {
            Toast.makeText(this, errorLoading, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
