package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.R;

public class ConfirmationDialog extends AppCompatDialogFragment {

    Activity context;

    public ConfirmationDialog(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_window, null);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        Button confirmYesButton = view.findViewById(R.id.confirmYesButton);
        confirmYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });

        Button confirmCancelButton = view.findViewById(R.id.confirmCancelButton);
        confirmCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }
}
