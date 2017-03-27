package be.formation.studymanager;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import be.formation.studymanager.db.LessonDAO;
import be.formation.studymanager.db.UserLessonDAO;
import be.formation.studymanager.model.Lesson;

public class AllLessonsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


    private LessonDAO lessonDAO;
    private ListView lvAllLessons;
    private StudyApplication app;
    private SearchView searchView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lessons);
        lessonDAO = new LessonDAO(this);
        lvAllLessons = (ListView) findViewById(R.id.lv_all_lessons);
        searchView= (SearchView) findViewById(R.id.sv_filter_lesson);

        lvAllLessons.setOnItemClickListener(this);
        app = (StudyApplication) getApplication();
        updateList();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("TEXT","CHANGED");
                adapter.getFilter().filter(newText);
                adapter.notifyDataSetChanged();
                lvAllLessons.setAdapter(adapter);
                return true;
            }
        });
    }

    private void updateList(){
        lessonDAO.openReadable();
        String[] from = new String[]{LessonDAO.COL_NAME, LessonDAO.COL_TRAINER};
        adapter = new SimpleCursorAdapter(this,
                R.layout.list_entry,
                lessonDAO.getLessonCursor(),
                from, new int[]{R.id.tv_name, R.id.tv_id}
        );

        lvAllLessons.setAdapter(adapter);
        lessonDAO.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
        Lesson lesson=lessonDAO.cursorToLesson(cursor);
        UserLessonDAO userLessonDAO = new UserLessonDAO(this);
        userLessonDAO.openWritable();
        userLessonDAO.insert(app.getUserId(),lesson.getId());
        userLessonDAO.close();
        finish();
    }
}
