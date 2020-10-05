package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.FirebaseConnectorService;

public class DeletionDialog extends AppCompatDialogFragment {

    Activity context;
    String id;
    FirebaseConnectorService firebaseConnectorService;

    public DeletionDialog(Activity context, String id) {
        this.context = context;
        this.id = id;
        firebaseConnectorService = new FirebaseConnectorService();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_window, null);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        Button confirmYesButton = view.findViewById(R.id.confirmYesButton);
        confirmYesButton.setText("Delete");
        confirmYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseConnectorService.mDatabase.child("characters").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                        context.finish();
                        Toast.makeText(getContext(), "Character Deletion Complete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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
