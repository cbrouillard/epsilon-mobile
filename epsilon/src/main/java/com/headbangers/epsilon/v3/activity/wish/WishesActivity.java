package com.headbangers.epsilon.v3.activity.wish;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.activity.account.AccountDetailActivity_;
import com.headbangers.epsilon.v3.adapter.WishesAdapter;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.wish.WishListAsyncLoader;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Wish;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static com.headbangers.epsilon.v3.activity.account.AccountDetailActivity.FROM_DETAILS_ACTIVITY;

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

    @Click(R.id.refresh)
    void refreshButton() {
        init();
    }

    @Click(R.id.addWish)
    void addWish() {
        AddWishActivity_.intent(this)
                .startForResult(AddWishActivity.ADD_WISH_DONE);
    }

    @OnActivityResult(AddWishActivity.ADD_WISH_DONE)
    void wishAdded() {
        init();
    }

    @ItemClick(R.id.list)
    void listClick(Wish wish) {
        WishDetailActivity_.intent(this).extra("wish", wish).start();
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
