package com.headbangers.epsilon.v3.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
        LinearLayout globalLayout = (LinearLayout) row.findViewById(R.id.oneAccount);
        name.setText(account.getName());
        if (account.isMobileDefault()) {
            name.setTypeface(null, Typeface.BOLD);
            globalLayout.setBackgroundColor(context.getResources().getColor(R.color.lighterBlue));
        } else {
            name.setTypeface(null, Typeface.NORMAL);
            globalLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        TextView sold = (TextView) row.findViewById(R.id.sold);
        sold.setText(df.format(account.getSold()) + "€");

        TextView date = (TextView) row.findViewById(R.id.date);
        date.setText(openedAt + " " + account.getFormatedDateOpened());

        TextView status = (TextView) row.findViewById(R.id.status);
        if (account.getType().equalsIgnoreCase("CHEQUES")) {
            if (account.getSold() > 0) {
                status.setBackgroundResource(R.drawable.span_ok);
                sold.setTextColor(Color.parseColor("#459645"));
            } else {
                status.setBackgroundResource(R.drawable.span_ko);
                sold.setTextColor(Color.RED);
            }
        } else {
            status.setBackgroundResource(R.drawable.span_ok);
            sold.setTextColor(Color.DKGRAY);
        }

        return row;
    }
}
