package com.headbangers.epsilon.v3.adapter;

import android.app.Activity;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.ImageLoadTask;
import com.headbangers.epsilon.v3.model.Wish;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class WishesAdapter extends ArrayAdapter<Wish> {

    private Activity context;
    private List<Wish> wishes;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public WishesAdapter(Activity context, List<Wish> wishes) {
        super(context, R.layout.one_wish, wishes);
        this.context = context;
        this.wishes = wishes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.one_wish, null);
        }
        Wish wish = wishes.get(position);

        ImageView image = (ImageView) row.findViewById(R.id.image);
        image.setImageBitmap(null);
        image.setImageResource(R.drawable.noimage);
        if (wish.getThumbnailUrl() != null && !wish.getThumbnailUrl().isEmpty()) {
            new ImageLoadTask(this.context, wish.getId(), wish.getThumbnailUrl(), image).execute();
        }

        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(wish.getName());

        TextView price = (TextView) row.findViewById(R.id.price);
        price.setText(df.format(wish.getPrice()) + "€");

        TextView infos = (TextView) row.findViewById(R.id.infos);
        String infosText = wish.getAccount();
        if (wish.getCategory() != null && !wish.getCategory().isEmpty()) {
            infosText += " - " + wish.getCategory();
        }
        infos.setText(infosText);

        return row;
    }

}
