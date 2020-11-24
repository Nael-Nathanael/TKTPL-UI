package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.preview;

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
import androidx.fragment.app.Fragment;
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

import static androidx.fragment.app.DialogFragment.STYLE_NO_FRAME;

public class PreviewCharacterFragment extends Fragment {

    View root;
    SelectedCharacterIdViewModel selectedCharacterIdViewModel;
    FirebaseConnectorService firebaseConnectorService;
    private TheCharacter theCharacter;

    public PreviewCharacterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_preview_character_land, container, false);

        // fetch from firebase real time database
        firebaseConnectorService = new FirebaseConnectorService();
        selectedCharacterIdViewModel = new ViewModelProvider(requireActivity()).get(SelectedCharacterIdViewModel.class);

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
                GlideApp.with(PreviewCharacterFragment.this).load(theCharacter.image).into(image_preview);
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