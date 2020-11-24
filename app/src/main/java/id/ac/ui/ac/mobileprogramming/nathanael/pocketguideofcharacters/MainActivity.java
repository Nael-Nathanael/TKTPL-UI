package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities.CreateCharacterActivity;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.adapter.LandingRecyclerViewAdapter;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.database.DatabaseHelper;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.layout_managers.GridAutofitLayoutManager;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;

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


    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList() {
        List<TheCharacter> characters = databaseHelper.getAllCharacters();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCard);

        LandingRecyclerViewAdapter viewAdapter = new LandingRecyclerViewAdapter(this, characters);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(
                new GridAutofitLayoutManager(this, 144)
        );
    }
}