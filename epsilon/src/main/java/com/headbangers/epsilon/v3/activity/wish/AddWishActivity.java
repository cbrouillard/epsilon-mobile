package com.headbangers.epsilon.v3.activity.wish;

import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.async.data.AutoCompleteDataAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.operation.AddOperationAsyncLoader;
import com.headbangers.epsilon.v3.async.wish.AddWishAsyncLoader;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.AutoCompleteData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.add_wish)
@OptionsMenu(R.menu.menu_ok)
public class AddWishActivity extends AbstractEpsilonActivity implements Refreshable<AutoCompleteData> {

    public static final int ADD_WISH_DONE = 300;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.category)
    AutoCompleteTextView category;

    @ViewById(R.id.account)
    Spinner account;

    @ViewById(R.id.price)
    TextView price;

    @ViewById(R.id.name)
    TextView name;

    private List<Account> accounts;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.add_wish_title);
        toolbar.setSubtitle(R.string.add_whish_subtitle);
        setSupportActionBar(toolbar);

        init();
    }

    @EditorAction(R.id.name)
    void nameOk() {
        category.requestFocus();
    }

    @EditorAction(R.id.category)
    void categoryOk() {
        price.requestFocus();
    }

    @OptionsItem(R.id.action_ok)
    @EditorAction(R.id.price)
    void ok() {

        if (validateForm()) {
            String price = this.price.getText().toString();
            String category = this.category.getText().toString();
            String name = this.name.getText().toString();
            String account = this.accounts.get(this.account.getSelectedItemPosition()).getId();

            new AddWishAsyncLoader(accessService, this, progressBar).execute(name,
                    price, category, account);
        }
    }

    private void init() {
        new AutoCompleteDataAsyncLoader(accessService, this, progressBar)
                .execute(AutoCompleteDataAsyncLoader.Load.CATEGORY_ACCOUNTS.toString());
    }

    @Override
    public void refresh(AutoCompleteData result) {
        if (result != null) {
            this.accounts = result.getAccounts();

            List<String> accountsName = new ArrayList<>();
            for (Account account : accounts) {
                accountsName.add(account.getName() + " - "
                        + df.format(account.getSold()) + "€");
            }

            ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item,
                    accountsName);
            accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.account.setAdapter(accountAdapter);

            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_item, result.getCategories());
            this.category.setAdapter(categoryAdapter);
        }
    }

    private boolean validateForm() {
        String price = this.price.getText().toString();
        String category = this.category.getText().toString();
        String name = this.name.getText().toString();

        if (name == null || name.isEmpty()) {
            this.name.setError(errorFormName);
        }

        if (category == null || category.isEmpty()) {
            this.category.setError(errorFormCategory);
        }

        if (price == null || price.isEmpty()) {
            this.price.setError(errorFormAmount);
        }

        return price != null && !price.isEmpty()
                && category != null && !category.isEmpty()
                && name != null && !name.isEmpty();
    }
}
