package com.headbangers.epsilon.v3.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;

import java.util.Arrays;
import java.util.List;

public class NavigationAdapter extends ArrayAdapter<String> {

    private Activity context;
    private List<String> menus;

    public NavigationAdapter(Activity context){
        super(context, R.layout.one_navigation);
        this.context = context;

        this.menus = Arrays.asList("Test 1", "Test 2", "Test 3");
        this.addAll(menus);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.one_navigation, null);
        }
        String menu = menus.get(position);

        TextView menuText = (TextView) row.findViewById(R.id.menuText);
        menuText.setText(menu);

        return row;
    }
}
