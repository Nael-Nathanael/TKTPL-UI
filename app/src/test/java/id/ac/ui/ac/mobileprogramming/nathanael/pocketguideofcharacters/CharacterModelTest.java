package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters;

import org.junit.Test;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharacterModelTest {

    @Test
    public void whenCreateBiodataShouldHaveCorrectData() {
        TheCharacter character = new TheCharacter(null, "Nael", "21");
        assertNull(character.getId());
        assertEquals("Nael", character.getName());
        assertEquals("21", character.getAge());
    }

    @Test
    public void whenCreateBiodataBySetShouldHaveCorrectData() {
        TheCharacter character = new TheCharacter();

        character.setId("1");
        character.setName("Nael");
        character.setAge("21");

        assertEquals("1", character.getId());
        assertEquals("Nael", character.getName());
        assertEquals("21", character.getAge());
    }
}
