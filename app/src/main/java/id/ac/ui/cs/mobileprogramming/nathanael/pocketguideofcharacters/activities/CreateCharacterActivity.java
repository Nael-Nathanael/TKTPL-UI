package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.fragment.ConfirmationDialog;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.database.DatabaseHelper;

public class CreateCharacterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    TextView nameField;
    TextView ageField;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);

        databaseHelper = new DatabaseHelper(CreateCharacterActivity.this);

        createButton = findViewById(R.id.createButton);
        nameField = findViewById(R.id.nameField);
        ageField = findViewById(R.id.ageField);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String age = ageField.getText().toString();

                if (!name.isEmpty() && !age.isEmpty()) {
                    databaseHelper.createCharacter(name, age);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String name = String.valueOf(nameField.getText());
            String age = String.valueOf(ageField.getText());
            if (!name.isEmpty() || !age.isEmpty()) {
                openBackConfirmationDialog();
                return false;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openBackConfirmationDialog() {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(CreateCharacterActivity.this);
        confirmationDialog.show(getSupportFragmentManager(), "Form cancelation dialogue");
    }
}