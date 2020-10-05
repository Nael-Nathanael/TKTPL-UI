package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TheCharacter {

    public String id = "0";
    public String name = "placeholder";
    public int age = 77;

    public TheCharacter() {
    }

    public TheCharacter(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
