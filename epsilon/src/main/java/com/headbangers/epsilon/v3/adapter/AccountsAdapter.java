package com.headbangers.epsilon.v3.adapter;

import android.app.Activity;
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

    private Activity context;
    private List<Account> accounts;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public AccountsAdapter(Activity context, List<Account> accounts) {
        super(context, R.layout.one_account, accounts);
        this.context = context;
        this.accounts = accounts;
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

        TextView sold = (TextView) row.findViewById(R.id.sold);
        sold.setText(df.format(account.getSold()) + "€");

        TextView date = (TextView) row.findViewById(R.id.date);
        date.setText("Ouvert le: " + account.getFormatedDateOpened());

        return row;
    }

}
