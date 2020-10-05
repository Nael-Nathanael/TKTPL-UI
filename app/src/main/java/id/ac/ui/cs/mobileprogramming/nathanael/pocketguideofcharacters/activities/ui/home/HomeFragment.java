package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.adapter.LandingRecyclerViewAdapter;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.layout_managers.GridSpacingItemDecoration;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.FirebaseConnectorService;

public class HomeFragment extends Fragment {

    FirebaseConnectorService firebaseConnectorService;
    List<TheCharacter> characters;
    View root;
    Context context;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        rerenderHomeFragment();
        fetchFromDatabase();
    }

    private void fetchFromDatabase() {
        firebaseConnectorService = new FirebaseConnectorService();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                characters = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    characters.add(
                            postSnapshot.getValue(TheCharacter.class)
                    );
                }
                rerenderHomeFragment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        };
        firebaseConnectorService.mDatabase.child("characters").addValueEventListener(valueEventListener);
    }

    private void rerenderHomeFragment() {
        if (characters != null) {
            View loadingbar = root.findViewById(R.id.main_loading_progress_bar);
            if (loadingbar != null) {
                ((ViewGroup) loadingbar.getParent()).removeView(loadingbar);
            }

            RecyclerView recyclerView = root.findViewById(R.id.recyclerViewCard);

            LandingRecyclerViewAdapter viewAdapter = new LandingRecyclerViewAdapter(getContext(), characters);
            recyclerView.setAdapter(viewAdapter);
            recyclerView.setLayoutManager(
                    new GridLayoutManager(getContext(), 2)
            );
            recyclerView.addItemDecoration(
                    new GridSpacingItemDecoration(
                            2,
                            Math.round(12 * context.getResources().getDisplayMetrics().density),
                            true
                    )
            );
        }
    }
}