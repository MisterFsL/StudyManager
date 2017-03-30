package be.formation.studymanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.database.FirebaseDatabase;

import be.formation.studymanager.StudyApplication;
import be.formation.studymanager.model.Lesson;



public class UserLessonDAO {
    public static final String COL_USER_LESSON_ID = "_id";
    public static final String TABLE_USER_LESSON = "user_lesson";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_LESSON_ID = "lesson_id";

    public static final String CREATE_REQUEST = "CREATE TABLE "+ TABLE_USER_LESSON+ " ("
            + COL_USER_ID+ " TEXT NOT NULL,"
            + COL_LESSON_ID+ " INTEGER NOT NULL,"
            + COL_USER_LESSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            +");";

    public static final String UPGRADE_REQUEST = "DROP TABLE "+ TABLE_USER_LESSON;

    private FirebaseDatabase root;
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public UserLessonDAO(Context context){
        this.context=context;
        root=FirebaseDatabase.getInstance();
    }

    public UserLessonDAO openWritable(){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public UserLessonDAO openReadable(){
        dbHelper = new DBHelper(context);
        db=dbHelper.getReadableDatabase();
        return this;
    }

    public void close(){
        db.close();
        dbHelper.close();
    }

    public long insert (String userId, long idLesson){
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_ID,userId);
        cv.put(COL_LESSON_ID,idLesson);
        return db.insert(TABLE_USER_LESSON,null,cv);
    }

    public Cursor getLessonByUserId(String userId){
        final String QUERY = "SELECT * FROM "+TABLE_USER_LESSON+" a INNER JOIN "
                +LessonDAO.TABLE_LESSON+ " b ON a."+COL_LESSON_ID+"=b."+LessonDAO.COL_ID
                + " WHERE "+COL_USER_ID+"= '"+userId+"'";
        return db.rawQuery(QUERY,null);
    }

    public void deleteLessonById(String userId,Lesson l){
        db.delete(TABLE_USER_LESSON,COL_LESSON_ID +"="+l.getId()+" and "+
                COL_USER_ID+"='"+userId+"'",null);
    }
}
