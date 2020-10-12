package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.preview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.utility.dependency_injectors.GlideApp;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.utility.firebase.FirebaseConnectorService;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.viewmodels.SelectedCharacterIdViewModel;

public class PreviewCharacterFragmentDialog extends DialogFragment {

    View root;
    SelectedCharacterIdViewModel selectedCharacterIdViewModel;
    FirebaseConnectorService firebaseConnectorService;
    Activity context;
    private TheCharacter theCharacter;

    public PreviewCharacterFragmentDialog() {
        // Required empty public constructor
    }


    public PreviewCharacterFragmentDialog(Activity context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_preview_character, null);

        // fetch from firebase real time database
        firebaseConnectorService = new FirebaseConnectorService();
        selectedCharacterIdViewModel = new ViewModelProvider(requireActivity()).get(SelectedCharacterIdViewModel.class);

        builder.setView(root);

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        final Observer<String> selectedIdObserver = this::fetchAndUpdateOneFromFirebase;
        selectedCharacterIdViewModel.getSelected_id().observe(getViewLifecycleOwner(), selectedIdObserver);

        return root;
    }

    private void fetchAndUpdateOneFromFirebase(String id) {
        // create event listener for firebase data change.
        ValueEventListener valueEventListener = new ValueEventListener() {

            /**
             * @param snapshot fetched changes for firebase object
             */
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                Log.d("NaelsTest", "fetched " + id);
                theCharacter = snapshot.getValue(TheCharacter.class);
                TextView name_preview = root.findViewById(R.id.name_preview);
                TextView age_preview = root.findViewById(R.id.age_preview);
                ImageView image_preview = root.findViewById(R.id.image_preview);
                name_preview.setText(theCharacter.name);
                age_preview.setText(String.valueOf(theCharacter.age));
                GlideApp.with(PreviewCharacterFragmentDialog.this).load(theCharacter.image).into(image_preview);
            }

            /**
             * Show toast if has errors.
             * @param error information by firebase
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        };

        // set event listener to firebase's "characters" table
        firebaseConnectorService.mDatabase.child("characters").child(id).addListenerForSingleValueEvent(valueEventListener);
    }
}