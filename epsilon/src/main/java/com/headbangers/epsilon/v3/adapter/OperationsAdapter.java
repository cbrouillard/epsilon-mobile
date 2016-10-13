package com.headbangers.epsilon.v3.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.operation.LocatedOperationMapActivity_;
import com.headbangers.epsilon.v3.model.Operation;

import java.text.DecimalFormat;
import java.util.List;

public class OperationsAdapter extends ArrayAdapter<Operation>{

    private Activity context;
    private List<Operation> operations;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public OperationsAdapter(Activity context, List<Operation> operations) {
        super(context, R.layout.one_operation, operations);
        this.context = context;
        this.operations = operations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.one_operation, null);
        }
        final Operation operation = operations.get(position);

        ImageView icon = (ImageView) row.findViewById (R.id.icon);
        if (operation.getSign().equals("+")){
            icon.setImageResource(R.drawable.revenue);
        } else {
            icon.setImageResource(R.drawable.depense);
        }

        TextView category = (TextView) row.findViewById(R.id.category);
        category.setText(operation.getCategory() + " - ");

        TextView tiers = (TextView) row.findViewById(R.id.tiers);
        tiers.setText(operation.getTiers());

        TextView date = (TextView) row.findViewById(R.id.date);
        date.setText(operation.getFormatedDateApplication());

        TextView amount = (TextView) row.findViewById(R.id.amount);
        amount.setText(operation.getSign()+df.format(operation.getAmount())
                + "€");

        ImageButton showLocation = (ImageButton) row.findViewById(R.id.showLocation);
        showLocation.setVisibility(View.GONE);
        if (operation.getLatitude() != null && operation.getLongitude() != null){
            showLocation.setVisibility(View.VISIBLE);
            showLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocatedOperationMapActivity_.intent(context).extra("operation", operation).start();
                }
            });
        }
        showLocation.setFocusable(false);

        return row;
    }
}
