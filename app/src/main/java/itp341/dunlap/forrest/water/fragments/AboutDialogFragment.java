package itp341.dunlap.forrest.water.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import itp341.dunlap.forrest.water.R;

public class AboutDialogFragment extends DialogFragment {

    public static AboutDialogFragment newInstance(String text) {
        AboutDialogFragment f = new AboutDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("text", text);
        f.setArguments(args);

        return f;
    }

    public AboutDialogFragment(){

    }

    static TextView aboutText;

    String text;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        text = (String) getArguments().get("text");

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_about, null);
        builder.setView(v);
        aboutText = (TextView) v.findViewById(R.id.dialog_text);
        aboutText.setText(text);

        ((TextView) v.findViewById(R.id.okay_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.setView(v).create();
    }
}
