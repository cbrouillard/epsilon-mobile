package com.headbangers.epsilon.v3.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.Budget;
import com.headbangers.epsilon.v3.model.Category;

import java.text.DecimalFormat;
import java.util.List;

public class BudgetsAdapter extends ArrayAdapter<Budget> {
    private Activity context;
    private List<Budget> budgets;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public BudgetsAdapter(Activity context, List<Budget> budgets) {
        super(context, R.layout.one_budget, budgets);
        this.context = context;
        this.budgets = budgets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.one_budget, null);
        }
        Budget budget = budgets.get(position);

        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(budget.getName());

        TextView amount = (TextView) row.findViewById(R.id.amount);
        amount.setText(df.format(budget.getUsedAmound()) + " / "
                + df.format(budget.getMaxAmount()) + "€");

        TextView status = (TextView) row.findViewById(R.id.status);
        if (budget.getUsedAmound() <= budget.getMaxAmount()) {
            status.setBackgroundResource(R.drawable.span_ok);
            amount.setTextColor(Color.parseColor("#459645"));
        } else {
            status.setBackgroundResource(R.drawable.span_ko);
            amount.setTextColor(Color.RED);
        }

        TextView note = (TextView) row.findViewById(R.id.note);
        StringBuffer buffer = new StringBuffer();
        for (Category category : budget.getCategories()){
            buffer.append(category.getName()).append (" / ");
        }
        note.setText(buffer);

        return row;
    }

}
