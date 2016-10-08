package com.headbangers.epsilon.v3.activity.operation;

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
import android.widget.ProgressBar;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.async.DeleteOperationAsyncLoader;
import com.headbangers.epsilon.v3.async.EditOperationAsyncLoader;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;
import com.headbangers.epsilon.v3.service.impl.EpsilonAccessServiceImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EFragment
public class DialogEditOperationFragment extends DialogFragment {
    private View view;

    @Bean
    EpsilonAccessServiceImpl accessService;

    @Pref
    EpsilonPrefs_ epsilonPrefs;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.category)
    AutoCompleteTextView category;

    @ViewById(R.id.tiers)
    AutoCompleteTextView tiers;

    @ViewById(R.id.amount)
    EditText amount;

    private Operation operation;

    ProgressBar progressBar;

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

                        new EditOperationAsyncLoader(accessService, getActivity(), progressBar).execute(
                                epsilonPrefs.token().get(),
                                operation.getId(),
                                category.getText().toString(),
                                tiers.getText().toString(),
                                amount.getText().toString());

                        dismiss();
                    }
                })

                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new DeleteOperationAsyncLoader(accessService, getActivity(), progressBar).execute(
                                epsilonPrefs.token().get(), operation.getId());

                        dismiss();
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

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}