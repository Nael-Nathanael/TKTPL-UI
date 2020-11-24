package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TheCharacter {

    public String id;
    public String name;
    public int age;
    public String image;

    public TheCharacter() {
    }

    public TheCharacter(String newId, String name, int age, String image_url) {
        this.id = newId;
        this.name = name;
        this.age = age;
        this.image = image_url;
    }
}
