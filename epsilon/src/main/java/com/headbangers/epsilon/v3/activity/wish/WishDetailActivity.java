package com.headbangers.epsilon.v3.activity.wish;

import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.async.ImageLoadTask;
import com.headbangers.epsilon.v3.model.Wish;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.wish_detail)
public class WishDetailActivity extends AbstractEpsilonActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.photo)
    ImageView photo;

    @ViewById(R.id.price)
    TextView price;

    @ViewById(R.id.category)
    TextView category;

    @ViewById(R.id.previsionDate)
    TextView previsionDate;

    @ViewById(R.id.link)
    TextView link;

    @Extra("wish")
    Wish wish;

    @AfterViews
    void details() {
        toolbar.setTitle(wish.getName());
        toolbar.setSubtitle(wish.getAccount());
        setSupportActionBar(toolbar);

        price.setText(df.format(wish.getPrice()) + "€");
        category.setText(wish.getCategory());
        new ImageLoadTask(this, wish.getId(), wish.getThumbnailUrl(), photo).execute();
        previsionDate.setText(wish.getPrevisionBuyFormated());

        link.setText(wish.getWebShopUrl());
        link.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
