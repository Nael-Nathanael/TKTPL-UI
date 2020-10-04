package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.database.DatabaseHelper;

public class UpdateCharacterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    String originId;
    String originName;
    String originAge;
    TextView nameField;
    TextView ageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_character);

        databaseHelper = new DatabaseHelper(UpdateCharacterActivity.this);

        String value = getIntent().getStringExtra("id");
        originId = value;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(String.format("SELECT * FROM characters WHERE id = '%s'", value), null);
        cursor.moveToFirst();

        String name = cursor.getString(1);
        Objects.requireNonNull(getSupportActionBar()).setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameField = findViewById(R.id.nameFieldUpdate);
        originName = cursor.getString(1);
        nameField.setText(originName);

        ageField = findViewById(R.id.ageFieldUpdate);
        originAge = cursor.getString(2);
        ageField.setText(originAge);

        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                String name = nameField.getText().toString();
                String age = ageField.getText().toString();

                if (!name.isEmpty() && !age.isEmpty()) {
                    databaseHelper.updateCharacter(originId, name, age);
                    finish();
                }
                finish();
            }
        });

        cursor.close();
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
            String age = String.valueOf(ageField.getText());
            if (!name.equals(originName) || !age.equals(originAge)) {
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