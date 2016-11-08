package com.headbangers.epsilon.v3.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.Tiers;

import java.util.List;

public class TiersesAdapter extends ArrayAdapter<Tiers> {

    private Activity context;
    private List<Tiers> categories;

    public TiersesAdapter(Activity context, List<Tiers> categories) {
        super(context, R.layout.one_tiers, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.one_tiers, null);
        }
        Tiers tiers = categories.get(position);

        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(tiers.getName());

        TextView color = (TextView) row.findViewById(R.id.color);
        color.setBackgroundColor(Color.parseColor(tiers.getColor()));

        return row;
    }
}
