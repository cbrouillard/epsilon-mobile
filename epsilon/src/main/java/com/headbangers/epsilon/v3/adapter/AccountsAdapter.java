package com.headbangers.epsilon.v3.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.Account;

import java.text.DecimalFormat;
import java.util.List;

public class AccountsAdapter extends ArrayAdapter<Account> {

    String openedAt;

    private Activity context;
    private List<Account> accounts;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public AccountsAdapter(Activity context, List<Account> accounts) {
        super(context, R.layout.one_account, accounts);
        this.context = context;
        this.accounts = accounts;
        this.openedAt = getContext().getString(R.string.opened_at);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.one_account, null);
        }
        Account account = accounts.get(position);

        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(account.getName());
        if (account.isMobileDefault()) {
            name.setTypeface(null, Typeface.BOLD);
        } else {
            name.setTypeface(null, Typeface.NORMAL);
        }

        TextView sold = (TextView) row.findViewById(R.id.sold);
        sold.setText(df.format(account.getSold()) + "€");

        TextView date = (TextView) row.findViewById(R.id.date);
        date.setText(openedAt + " " + account.getFormatedDateOpened());

        TextView status = (TextView) row.findViewById(R.id.status);
        if (account.getType().equalsIgnoreCase("CHEQUES")) {
            if (account.getSold() > 0) {
                status.setBackgroundResource(R.drawable.span_ok);
            } else {
                status.setBackgroundResource(R.drawable.span_ko);
            }
        } else {
            status.setBackgroundResource(R.drawable.span_ok);
        }

        return row;
    }
}
