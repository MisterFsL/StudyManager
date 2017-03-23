package be.formation.studymanager.db;

/**
 * Created by student on 10-03-17.
 */

public class UserLessonDAO {
    public static final String TABLE_USER_LESSON = "user_lesson";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_LESSON_ID = "lesson_id";

    public static final String CREATE_REQUEST = "CREATE TABLE "+ TABLE_USER_LESSON+ " ("
            + COL_USER_ID+ " INTEGER ,"
            + COL_LESSON_ID+ " INTGER ,"
            + "PRIMARY KEY("+COL_USER_ID+","+COL_LESSON_ID+")"
            +");";

    public static final String UPGRADE_REQUEST = "DROP TABLE "+ TABLE_USER_LESSON;
}
