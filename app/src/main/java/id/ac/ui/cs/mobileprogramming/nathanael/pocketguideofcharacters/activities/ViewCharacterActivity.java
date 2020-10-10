package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R2;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.FirebaseConnectorService;

public class ViewCharacterActivity extends AppCompatActivity {

    FirebaseConnectorService firebaseConnectorService;
    TheCharacter mainCharacter;
    String extraId;
    View.OnClickListener editButtonClickListener;
    View.OnClickListener deleteButtonClickListener;
    FirebaseStorage storage;

    @BindView(R2.id.editButton)
    Button editButton;

    @BindView(R2.id.deleteButton)
    Button deleteButton;

    @BindView(R2.id.nameFieldRead)
    TextView nameView;

    @BindView(R2.id.ageFieldRead)
    TextView ageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_character);
        ButterKnife.bind(this);

        firebaseConnectorService = new FirebaseConnectorService();
        storage = FirebaseStorage.getInstance();
        extraId = getIntent().getStringExtra("id");

        rerenderCharacterView();
        refreshData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mainCharacter = snapshot.getValue(TheCharacter.class);
                Log.d("ViewCH", "Fetched");
                rerenderCharacterView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewCharacterActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        };
        firebaseConnectorService
                .mDatabase.child("characters").child(extraId)
                .addValueEventListener(valueEventListener);
    }

    private void rerenderCharacterView() {
        if (mainCharacter != null) {
            editButton.setOnClickListener(null);
            deleteButton.setOnClickListener(null);

            nameView.setText(mainCharacter.name);
            ageView.setText(String.valueOf(mainCharacter.age));

            editButtonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), UpdateCharacterActivity.class);
                    i.putExtra("id", mainCharacter.id);
                    i.putExtra("name", mainCharacter.name);
                    i.putExtra("age", mainCharacter.age);
                    startActivity(i);
                }
            };
            editButton.setOnClickListener(editButtonClickListener);

            deleteButtonClickListener = v -> new MaterialAlertDialogBuilder(ViewCharacterActivity.this, R.style.DeleteDialogTheme)
                    .setTitle("Confirm Delete " + mainCharacter.name)
                    .setMessage("After deletion, " + mainCharacter.name + " will be lost forever")
                    .setPositiveButton("Delete", (dialogInterface, i) -> firebaseConnectorService.mDatabase.child("characters").child(mainCharacter.id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            StorageReference photoRef = storage.getReferenceFromUrl(mainCharacter.image);
                            photoRef.delete().addOnSuccessListener(aVoid -> {
                                // File deleted successfully
                                dataSnapshot.getRef().removeValue();
                                finish();
                                Toast.makeText(ViewCharacterActivity.this, "Character Deletion Complete", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(exception -> {
                                // Uh-oh, an error occurred!
                                Toast.makeText(ViewCharacterActivity.this, "Error deleting image", Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    }))
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
            deleteButton.setOnClickListener(deleteButtonClickListener);
        }
    }
}