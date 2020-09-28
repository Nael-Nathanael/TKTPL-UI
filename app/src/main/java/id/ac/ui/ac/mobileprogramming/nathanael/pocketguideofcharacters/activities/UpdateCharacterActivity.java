package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.database.DatabaseHelper;

public class UpdateCharacterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    String originId;

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

        TextView target = findViewById(R.id.nameFieldUpdate);
        target.setText(cursor.getString(1));

        target = findViewById(R.id.ageFieldUpdate);
        target.setText(cursor.getString(2));


        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                TextView nameField = findViewById(R.id.nameFieldUpdate);
                String name = nameField.getText().toString();
                TextView ageField = findViewById(R.id.ageFieldUpdate);
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
}