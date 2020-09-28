package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities.CreateCharacterActivity;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateCharacterActivity.class);
                startActivity(i);
            }
        });
    }
}