package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.adapter.LandingRecyclerViewAdapter;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.database.DatabaseHelper;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.layout_managers.GridAutofitLayoutManager;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;

public class HomeFragment extends Fragment {
    DatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        databaseHelper = new DatabaseHelper(getContext());
        List<TheCharacter> characters = databaseHelper.getAllCharacters();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewCard);

        LandingRecyclerViewAdapter viewAdapter = new LandingRecyclerViewAdapter(getContext(), characters);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(
                new GridAutofitLayoutManager(getContext(), 144)
        );

        return root;
    }


}