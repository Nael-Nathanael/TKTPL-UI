package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.FirebaseConnectorService;

public class DashboardFragment extends Fragment {

    FirebaseConnectorService firebaseConnectorService;
    TextView nameField;
    TextView ageField;
    Button createButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        firebaseConnectorService = new FirebaseConnectorService();

        createButton = root.findViewById(R.id.createButton);
        nameField = root.findViewById(R.id.nameField);
        ageField = root.findViewById(R.id.ageField);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String ageStr = ageField.getText().toString();

                if (!name.isEmpty() && !ageStr.isEmpty()) {
                    int age = Integer.parseInt(ageStr);
                    firebaseConnectorService.createCharacter(name, age);
                    nameField.setText("");
                    ageField.setText("");
                    Toast.makeText(getContext(), "Character Creation Complete", Toast.LENGTH_SHORT).show();

                    requireActivity().findViewById(R.id.navigation_home).performClick();
                }
            }
        });

        return root;
    }
}