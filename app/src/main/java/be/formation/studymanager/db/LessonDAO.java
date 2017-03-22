package be.formation.studymanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.formation.studymanager.StudyApplication;
import be.formation.studymanager.model.Lesson;

/**
 * Created by student on 10-03-17.
 */

public class LessonDAO {
    public static final String TABLE_LESSON = "lesson";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_CATEGORY = "category_id";
    public static final String COL_HOURS = "hours";
    public static final String COL_TRAINER = "trainer";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_LATITUDE = "latitude";

    private FirebaseDatabase root;

    public static final String CREATE_REQUEST = "CREATE TABLE "+
            TABLE_LESSON+ " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME + " TEXT NOT NULL, "
            + COL_TRAINER + " TEXT NOT NULL, "
            + COL_CATEGORY+ " TEXT, "
            + COL_HOURS + " INTEGER, "
            + COL_LONGITUDE +" REAL, "
            + COL_LATITUDE + " REAL"
            +");";

    public static final String UPGRADE_REQUEST = "DROP TABLE "+ TABLE_LESSON;

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public LessonDAO(Context context){
        this.context=context;
        root=FirebaseDatabase.getInstance();
    }

    public LessonDAO openWritable(){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public LessonDAO openReadable(){
        dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public void close(){
        db.close();
        dbHelper.close();
    }

    public long insert (Lesson lesson){
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,lesson.getName());
        cv.put(COL_TRAINER,lesson.getTrainer());
        cv.put(COL_CATEGORY,lesson.getCategory());
        cv.put(COL_HOURS,lesson.getHours());
        cv.put(COL_LATITUDE,lesson.getLatitude());
        cv.put(COL_LONGITUDE,lesson.getLongitude());
        Map<String,Lesson> mapLesson= new HashMap<>();
        StudyApplication app = (StudyApplication) context.getApplicationContext();
        if(app.getUserId()!=null) {

            Toast.makeText(context, app.userId, Toast.LENGTH_SHORT).show();
            mapLesson.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), lesson);
            root.getReference("lesson").setValue(mapLesson);
        }else {
            Toast.makeText(context, "NO USER", Toast.LENGTH_SHORT).show();
        }
        return db.insert(TABLE_LESSON,null,cv);
    }

    public Cursor getLessonCursor(){
        Cursor c = db.query(TABLE_LESSON,null,null,null,null,null,null);
        if(c.getCount()>0){
            c.moveToFirst();
            return c;
        }
        return null;
    }

    public Lesson cursorToLesson(Cursor c){
        Lesson l= new Lesson(
                c.getString(c.getColumnIndex(COL_NAME)),
                c.getString(c.getColumnIndex(COL_TRAINER)),
                c.getString(c.getColumnIndex(COL_CATEGORY)),
                c.getInt(c.getColumnIndex(COL_HOURS)
                        )
        );
        l.setId(c.getInt(c.getColumnIndex(COL_ID)));
        l.setLongitude(c.getDouble(c.getColumnIndex(COL_LONGITUDE)));
        l.setLatitude(c.getDouble(c.getColumnIndex(COL_LATITUDE)));
        return l;
    }

    public List<Lesson> getLessons(){
        List<Lesson> lessons = new ArrayList<>();
        Cursor c = getLessonCursor();
        if(c!=null){
            do
                lessons.add(cursorToLesson(c));
            while (c.moveToNext());
            return lessons;
        }
        return null;
    }

    public void delete (Lesson l){
        db.delete(TABLE_LESSON,COL_ID +"="+l.getId(),null);
    }

    public void delete (Cursor c){
        db.delete(TABLE_LESSON,COL_ID+"="+cursorToLesson(c).getId(),null);
        Log.d("TEST ID",cursorToLesson(c).toString());
    }
}