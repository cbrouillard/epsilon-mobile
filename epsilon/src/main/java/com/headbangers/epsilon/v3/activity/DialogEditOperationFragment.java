package com.headbangers.epsilon.v3.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.Operation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment
public class DialogEditOperationFragment extends DialogFragment {
    private View view;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.category)
    AutoCompleteTextView category;

    @ViewById(R.id.tiers)
    AutoCompleteTextView tiers;

    @ViewById(R.id.amount)
    EditText amount;

    private Operation operation;

    @AfterViews
    public void init() {

        toolbar.setTitle(operation.getFormatedDateApplication());

        category.setText(operation.getCategory());
        tiers.setText(operation.getTiers());
        amount.setText(operation.getAmount().toString());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_operation_detail, null);

        builder.setView(view)

                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}