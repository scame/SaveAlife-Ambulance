package com.example.scame.savealife.presentation.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class ConfirmDialogFragment extends DialogFragment {

    private ConfirmDialogListener listener;

    public interface ConfirmDialogListener {

        void destinationPointConfirmed();
    }

    public ConfirmDialogFragment() { }

    public static ConfirmDialogFragment newInstance(String title, String destination) {
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", "Are you sure that " + destination + " is the final destination?");
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title", "Default title");
        String message = getArguments().getString("message", "empty message");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            listener.destinationPointConfirmed();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        return builder.create();
    }

    public void setConfirmationListener(ConfirmDialogListener listener) {
        this.listener = listener;
    }
}
