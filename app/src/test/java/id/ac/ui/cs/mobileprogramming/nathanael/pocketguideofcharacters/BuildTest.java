package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuildTest {

    public final String PACKAGE_NAME_TO_TEST = "id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters";

    @Test
    public void whenBuildCompleteShouldHaveValidInformation() {
        assertTrue(BuildConfig.DEBUG);
        assertEquals(BuildConfig.APPLICATION_ID, PACKAGE_NAME_TO_TEST);
    }
}