package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.database.DatabaseHelper;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DatabaseCRUDTest {

    private DatabaseHelper databaseHelper;

    @Before
    public void setUp() throws Exception {
        databaseHelper = new DatabaseHelper(InstrumentationRegistry.getInstrumentation().getTargetContext(), true);
        databaseHelper.getWritableDatabase();
    }

    @After
    public void tearDown() throws Exception {
        databaseHelper.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(databaseHelper);
    }

    @Test
    public void testCouldAddNewCharacter() throws Exception {
        databaseHelper.createCharacter("Nael", "12");
        List<TheCharacter> characters = databaseHelper.getAllCharacters();

        assertThat(characters.size(), is(1));
        assertEquals("Nael", characters.get(0).getName());
        assertEquals("12", characters.get(0).getAge());
    }


    @Test
    public void testDeleteAll() {
        databaseHelper.deleteAll();
        List<TheCharacter> characters = databaseHelper.getAllCharacters();

        assertThat(characters.size(), is(0));
    }

    @Test
    public void testDeleteOnlyOne() {
        databaseHelper.createCharacter("Nael", "12");
        List<TheCharacter> characters = databaseHelper.getAllCharacters();

        assertThat(characters.size(), is(1));

        databaseHelper.deleteCharacter(characters.get(0));
        characters = databaseHelper.getAllCharacters();

        assertThat(characters.size(), is(0));
    }

    @Test
    public void testAddAndDelete() {
        databaseHelper.deleteAll();
        databaseHelper.createCharacter("Nael", "21");
        databaseHelper.createCharacter("Nael2", "21");
        databaseHelper.createCharacter("Nael3", "21");
        databaseHelper.createCharacter("Nael4", "21");

        List<TheCharacter> characters = databaseHelper.getAllCharacters();
        assertThat(characters.size(), is(4));

        databaseHelper.deleteCharacter(characters.get(0));
        databaseHelper.deleteCharacter(characters.get(1));

        characters = databaseHelper.getAllCharacters();
        assertThat(characters.size(), is(2));
    }
}
