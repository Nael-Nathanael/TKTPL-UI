package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pocketguideofcharacters.db";
    private static final int DATABASE_VERSION = 1;
    private static String CHARACTER_TABLE = "characters";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, Boolean isRunOnTestMode) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (isRunOnTestMode) {
            CHARACTER_TABLE = "characters_test";
            onCreate(this.getWritableDatabase());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s (id integer primary key, name text null, age integer null)", CHARACTER_TABLE);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createCharacter(String name, String age) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(
                String.format(
                        "INSERT INTO %s (id, name, age) VALUES (null, '%s', '%s');",
                        CHARACTER_TABLE,
                        name,
                        age
                )
        );
    }

    public void destroyTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(String.format("DROP TABLE %s", CHARACTER_TABLE));
    }

    public List<TheCharacter> getAllBiodata() {
        List<TheCharacter> characters = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(CHARACTER_TABLE,
                new String[]{"id", "name", "age"}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TheCharacter focus = cursorToTheCharacter(cursor);
            characters.add(focus);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return characters;
    }

    private TheCharacter cursorToTheCharacter(Cursor cursor) {
        return new TheCharacter(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2)
        );
    }

    public void deleteAll() {
        this.getWritableDatabase().delete(CHARACTER_TABLE, null, null);
    }

    public void deleteCharacter(TheCharacter character) {
        String id = character.getId();
        this.getWritableDatabase().delete(CHARACTER_TABLE, String.format("id = %s", id), null);
    }

    public void deleteCharacterById(String id) {
        this.getWritableDatabase().delete(CHARACTER_TABLE, String.format("id = %s", id), null);
    }

    public void updateCharacter(String originId, String name, String age) {
        this.getWritableDatabase().execSQL(
                String.format(
                        "UPDATE characters SET name = '%s', age = '%s' WHERE id = '%s';",
                        name,
                        age,
                        originId
                )
        );
    }
}
