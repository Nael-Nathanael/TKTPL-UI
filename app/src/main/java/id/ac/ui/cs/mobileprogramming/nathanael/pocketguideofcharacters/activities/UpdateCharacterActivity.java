package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.fragment.ConfirmationDialog;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.FirebaseConnectorService;

public class UpdateCharacterActivity extends AppCompatActivity {

    FirebaseConnectorService firebaseConnectorService;
    String originId;
    String originName;
    int originAge;
    TextView nameField;
    TextView ageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_character);

        firebaseConnectorService = new FirebaseConnectorService();

        originId = getIntent().getStringExtra("id");
        originName = getIntent().getStringExtra("name");
        originAge = getIntent().getIntExtra("age", 0);

        Objects.requireNonNull(getSupportActionBar()).setTitle(originName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameField = findViewById(R.id.nameFieldUpdate);
        nameField.setText(originName);

        ageField = findViewById(R.id.ageFieldUpdate);
        ageField.setText(String.valueOf(originAge));

        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String ageStr = ageField.getText().toString();
                if (!name.isEmpty() && !ageStr.isEmpty()) {
                    int age = Integer.parseInt(ageStr);
                    firebaseConnectorService.mDatabase.child("characters").child(originId).child("name").setValue(name);
                    firebaseConnectorService.mDatabase.child("characters").child(originId).child("age").setValue(age);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String name = String.valueOf(nameField.getText());
            int age = Integer.parseInt(ageField.getText().toString());
            if (!name.equals(originName) || age != originAge) {
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
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(UpdateCharacterActivity.this);
        confirmationDialog.show(getSupportFragmentManager(), "Form cancelation dialogue");
    }
}