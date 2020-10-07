package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R2;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.fragment.ConfirmationDialog;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.FirebaseConnectorService;

/**
 * Simple edit character data.
 *
 * @author Nathanael
 */
public class UpdateCharacterActivity extends AppCompatActivity {

    /**
     * Connection to firebase
     */
    FirebaseConnectorService firebaseConnectorService;

    /**
     * Original Id passed using extras
     */
    String originId;

    /**
     * Original name passed using extras
     */
    String originName;

    /**
     * Original age passed using extras
     */
    int originAge;

    /**
     * Text field for name
     */
    @BindView(R2.id.nameFieldUpdate)
    TextView nameField;

    /**
     * Text field for age
     */
    @BindView(R2.id.ageFieldUpdate)
    TextView ageField;

    /**
     * @param savedInstanceState to pass to superclass
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_character);
        ButterKnife.bind(this);

        // initiate attribute variables
        firebaseConnectorService = new FirebaseConnectorService();
        originId = getIntent().getStringExtra("id");
        originName = getIntent().getStringExtra("name");
        originAge = getIntent().getIntExtra("age", 0);

        // set action bar title to name
        Objects.requireNonNull(getSupportActionBar()).setTitle(originName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set default value for text input field
        nameField.setText(originName);
        ageField.setText(String.valueOf(originAge));

        // bind update button
        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get values from views
                String name = nameField.getText().toString();
                String ageStr = ageField.getText().toString();

                // only run this if fields are not empty
                if (!name.isEmpty() && !ageStr.isEmpty()) {
                    int age = Integer.parseInt(ageStr);

                    // only update database if name or age different than in records
                    if (!name.equals(originName) || age != originAge) {
                        firebaseConnectorService.mDatabase.child("characters").child(originId).child("name").setValue(name);
                        firebaseConnectorService.mDatabase.child("characters").child(originId).child("age").setValue(age);
                    }

                    // end this activity
                    finish();
                }
            }
        });
    }

    /**
     * @param item from menu
     * @return selected menu option
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Created to override back button default behavior.
     *
     * @param keyCode pressed key
     * @param event   to pass to superclass
     * @return superclass's return if pressed key not back. return true to allow default back behavior. return false otherwise.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // only run this on back button pressed
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // get values from view
            String name = String.valueOf(nameField.getText());
            int age = Integer.parseInt(ageField.getText().toString());

            // send confirmation dialog if try to exit without saving changes
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

    /**
     * Show confirmation dialog
     */
    private void openBackConfirmationDialog() {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(UpdateCharacterActivity.this);
        confirmationDialog.show(getSupportFragmentManager(), "Form cancelation dialogue");
    }
}