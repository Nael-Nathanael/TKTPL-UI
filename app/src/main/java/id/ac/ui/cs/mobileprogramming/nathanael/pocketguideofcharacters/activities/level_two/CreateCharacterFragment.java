package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.level_two;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R2;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.NavigationViewModel;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.FirebaseConnectorService;

public class CreateCharacterFragment extends Fragment {

    FirebaseConnectorService firebaseConnectorService;

    @BindView(R2.id.nameField)
    TextView nameField;

    @BindView(R2.id.ageField)
    TextView ageField;

    @BindView(R2.id.createButton)
    Button createButton;

    View root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        firebaseConnectorService = new FirebaseConnectorService();
        ButterKnife.bind(this, root);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createButtonClick();
            }
        });
        setHasOptionsMenu(true);

        return root;
    }

    public void createButtonClick() {
        String name = nameField.getText().toString();
        String ageStr = ageField.getText().toString();

        if (!name.isEmpty() && !ageStr.isEmpty()) {
            int age = Integer.parseInt(ageStr);
            firebaseConnectorService.createCharacter(name, age);
            nameField.setText("");
            ageField.setText("");
            Toast.makeText(getContext(), "Character Creation Complete", Toast.LENGTH_SHORT).show();

            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(root.getWindowToken(), 0);

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.custom_timer_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Create");

        NavigationViewModel navigationViewModel = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        navigationViewModel.getActive().setValue(false);
    }
}
