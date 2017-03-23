package be.formation.studymanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by student on 13-03-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "study_db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LessonDAO.CREATE_REQUEST);
        db.execSQL(UserLessonDAO.CREATE_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LessonDAO.UPGRADE_REQUEST);

        onCreate(db);
    }
}
