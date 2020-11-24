package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.cardlist;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.utility.RecyclerItemClickListener;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.utility.firebase.FirebaseConnectorService;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.viewmodels.NavigationViewModel;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.viewmodels.SelectedCharacterIdViewModel;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.preview.PreviewCharacterFragmentDialog;

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

    RecyclerView recyclerView;

    SelectedCharacterIdViewModel selectedCharacterIdViewModel;

    String first_id;

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

        recyclerView = root.findViewById(R.id.recyclerViewCard);
        setRecyclerItemListener();

        selectedCharacterIdViewModel = new ViewModelProvider(requireActivity()).get(SelectedCharacterIdViewModel.class);

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

    private void setRecyclerItemListener() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        context,
                        (view, position) -> {
                            TextView idView = view.findViewById(R.id.the_card).findViewById(R.id.character_id);
                            String id = (String) idView.getText();
                            selectedCharacterIdViewModel.getSelected_id().setValue(id);

                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                PreviewCharacterFragmentDialog previewCharacterFragment = new PreviewCharacterFragmentDialog(getActivity());
                                previewCharacterFragment.show(requireActivity().getSupportFragmentManager(), "Preview");
                            }
                        }
                )
        );
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
                    TheCharacter newChara = postSnapshot.getValue(TheCharacter.class);
                    if (characters.size() == 0) {
                        assert newChara != null;
                        first_id = newChara.id;
                    }
                    characters.add(newChara);
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

            CardListViewAdapter viewAdapter = new CardListViewAdapter(getContext(), characters);
            int spanCount = getSpanCount();
            recyclerView.setAdapter(viewAdapter);
            recyclerView.setLayoutManager(
                    new GridLayoutManager(getContext(), spanCount)
            );
            recyclerView.addItemDecoration(
                    new CardListGridSpacer(
                            spanCount,
                            Math.round(10 * context.getResources().getDisplayMetrics().density),
                            true
                    )
            );
            if (characters.size() > 0) {
                selectedCharacterIdViewModel.getSelected_id().setValue(first_id);
                View preview_right_fragment = root.findViewById(R.id.preview_right_fragment);
                if (preview_right_fragment != null) {
                    preview_right_fragment.setVisibility(View.VISIBLE);
                }
            }
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
        return focus / 300;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Home");

        NavigationViewModel navigationViewModel = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        navigationViewModel.getActive().setValue(true);
    }
}