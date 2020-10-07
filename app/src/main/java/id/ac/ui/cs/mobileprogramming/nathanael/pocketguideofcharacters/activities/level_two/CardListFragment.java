package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.level_two;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.NavigationViewModel;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.adapter.LandingRecyclerViewAdapter;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.layout_managers.GridSpacingItemDecoration;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.FirebaseConnectorService;

/**
 * Primary layout to access, edit, delete characters
 *
 * @author Nathanael
 */
public class CardListFragment extends Fragment {

    /**
     * Firebase real time database connection.
     */
    FirebaseConnectorService firebaseConnectorService;

    /**
     * All fetched character lists.
     */
    List<TheCharacter> characters;

    /**
     * Inflated primary layout
     */
    View root;

    /**
     * Bind context.
     */
    Context context;

    /**
     * Called when view create, inflate root / base layout for this fragment.
     *
     * @param inflater           layout inflater
     * @param container          fragment view group
     * @param savedInstanceState to pass to superclass
     * @return inflated primary layout
     */
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    /**
     * Called when this fragment gain focus.
     *
     * @param context binded context;
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        // rerender and fetch
        rerenderHomeFragment();
        fetchFromDatabase();
    }

    // fetch from firebase real time database
    private void fetchFromDatabase() {
        firebaseConnectorService = new FirebaseConnectorService();

        // create event listener for firebase data change.
        ValueEventListener valueEventListener = new ValueEventListener() {

            /**
             * @param snapshot fetched changes for firebase object
             */
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                // if data changed, refetch all characters
                characters = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    characters.add(
                            postSnapshot.getValue(TheCharacter.class)
                    );
                }

                // then rerender the recycler layout
                rerenderHomeFragment();
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
        firebaseConnectorService.mDatabase.child("characters").addValueEventListener(valueEventListener);
    }

    /**
     * Rerender Recycler View + Grid View + Card View
     */
    private void rerenderHomeFragment() {
        if (characters != null) {
            View loadingbar = root.findViewById(R.id.main_loading_progress_bar);
            if (loadingbar != null) {
                ((ViewGroup) loadingbar.getParent()).removeView(loadingbar);
            }

            RecyclerView recyclerView = root.findViewById(R.id.recyclerViewCard);

            LandingRecyclerViewAdapter viewAdapter = new LandingRecyclerViewAdapter(getContext(), characters);
            int spanCount = getSpanCount();
            recyclerView.setAdapter(viewAdapter);
            recyclerView.setLayoutManager(
                    new GridLayoutManager(getContext(), spanCount)
            );
            recyclerView.addItemDecoration(
                    new GridSpacingItemDecoration(
                            spanCount,
                            Math.round(12 * context.getResources().getDisplayMetrics().density),
                            true
                    )
            );
        }
    }

    /**
     * Calculate span count using context's dimension.
     *
     * @return span count
     */
    private int getSpanCount() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int focus = displayMetrics.widthPixels;
        focus = (int) (focus / context.getResources().getDisplayMetrics().density);
        return focus / 160;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Home");

        NavigationViewModel navigationViewModel = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        navigationViewModel.getActive().setValue(true);
    }
}