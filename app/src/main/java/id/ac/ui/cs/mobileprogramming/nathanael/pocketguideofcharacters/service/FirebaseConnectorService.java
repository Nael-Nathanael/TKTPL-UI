package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;

public class FirebaseConnectorService {

    public DatabaseReference mDatabase;

    public FirebaseConnectorService() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void createCharacter(String name, int age) {
        String newId = mDatabase.child("characters").push().getKey();

        TheCharacter theCharacter = new TheCharacter(newId, name, age);
        mDatabase.child("characters").child(newId).setValue(theCharacter);
    }

}
