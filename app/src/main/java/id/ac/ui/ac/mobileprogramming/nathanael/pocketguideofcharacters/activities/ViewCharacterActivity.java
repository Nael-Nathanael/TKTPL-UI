package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.content.Intent;
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
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities.fragment.DeletionDialog;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.database.DatabaseHelper;

public class ViewCharacterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_character);

        databaseHelper = new DatabaseHelper(ViewCharacterActivity.this);

        id = getIntent().getStringExtra("id");

        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UpdateCharacterActivity.class);
                i.putExtra("id", ViewCharacterActivity.this.id);
                startActivity(i);
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteConfirmationDialog();
            }
        });
    }

    private void openDeleteConfirmationDialog() {
        DeletionDialog confirmationDialog = new DeletionDialog(ViewCharacterActivity.this, ViewCharacterActivity.this.id);
        confirmationDialog.show(getSupportFragmentManager(), "Deletion dialogue");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(String.format("SELECT * FROM characters WHERE id = '%s'", id), null);
        cursor.moveToFirst();

        TextView target = findViewById(R.id.nameFieldRead);
        target.setText(cursor.getString(1));

        target = findViewById(R.id.ageFieldRead);
        target.setText(cursor.getString(2));

        final String name = cursor.getString(1);

        Objects.requireNonNull(getSupportActionBar()).setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cursor.close();
    }

}