package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.database.DatabaseHelper;

public class CreateCharacterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);

        databaseHelper = new DatabaseHelper(CreateCharacterActivity.this);

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView nameField = findViewById(R.id.nameField);
                TextView ageField = findViewById(R.id.ageField);

                String name = nameField.getText().toString();
                String age = ageField.getText().toString();

                if (!name.isEmpty() && !age.isEmpty()) {
                    databaseHelper.createCharacter(name, age);
                    finish();
                }
            }
        });
    }
}