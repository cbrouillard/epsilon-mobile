package com.headbangers.epsilon.v3.activity.wish;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.adapter.WishesAdapter;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.wish.WishListAsyncLoader;
import com.headbangers.epsilon.v3.model.Wish;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.wishes)
public class WishesActivity extends AbstractEpsilonActivity implements Refreshable<List<Wish>> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.list)
    ListView list;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.wish_list);
        toolbar.setSubtitle(R.string.wish_list_subtitle);
        setSupportActionBar(toolbar);

        init();
    }

    private void init() {
        if (isLogged()) {
            new WishListAsyncLoader(this.accessService, this, progressBar).execute();
        }
    }

    @Override
    public void refresh(List<Wish> result) {
        if (result != null) {
            WishesAdapter wishesAdapter = new WishesAdapter(this, result);
            list.setAdapter(wishesAdapter);
        } else {
            Toast.makeText(this, errorLoading, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
