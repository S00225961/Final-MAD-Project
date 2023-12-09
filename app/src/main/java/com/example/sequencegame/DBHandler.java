package com.example.sequencegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private SQLiteDatabase database;
    private String[] allColumns = { DBHandler.ID_COL,
            DBHandler.NAME_COL, DBHandler.SCORE_COL };
    private static final String DB_NAME = "PlayerScores";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "HighScores";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String NAME_COL = "name";
    private static final String SCORE_COL = "score";


    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT, "
                + SCORE_COL + " INTEGER)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewHighScore(String playerName, String playerScore) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, playerName);
        values.put(SCORE_COL, playerScore);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }
    public List<HighScore> getHighScores() {
        List<HighScore> highScoresList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ID_COL,
                NAME_COL,
                SCORE_COL
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                SCORE_COL + " DESC"
        );
        int idIndex = cursor.getColumnIndex(ID_COL);
        int nameIndex = cursor.getColumnIndex(NAME_COL);
        int scoreIndex = cursor.getColumnIndex(SCORE_COL);

        if (cursor.moveToFirst()) {
            do {
                HighScore highScore = new HighScore();
                highScore.setId(cursor.getLong(idIndex));
                highScore.setPlayerName(cursor.getString(nameIndex));
                highScore.setPlayerScore(cursor.getInt(scoreIndex));

                highScoresList.add(highScore);
            } while (cursor.moveToNext());
        }

        // Close the cursor when done
        cursor.close();

        return highScoresList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}