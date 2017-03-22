package be.formation.studymanager.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import be.formation.studymanager.AddLessonActivity;
import be.formation.studymanager.LessonActivity;
import be.formation.studymanager.R;
import be.formation.studymanager.db.LessonDAO;
import be.formation.studymanager.db.UserDAO;
import be.formation.studymanager.model.Lesson;

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonFragment extends Fragment implements AddLessonActivity.AddLessonCallback,AdapterView.OnItemClickListener {


    private ListView lvLessons;
    private static  LessonFragment instance;
    private FloatingActionButton btnAdd;
    private DatabaseReference myRef;
    private LessonDAO lessonDAO;

    public LessonFragment() {
    }

    public static LessonFragment getInstance(){
        if(instance==null)
            instance=new LessonFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lessons, container, false);
        lvLessons = (ListView) v.findViewById(R.id.lv_lessons);
        lvLessons.setOnItemClickListener(this);
        btnAdd = (FloatingActionButton) v.findViewById(R.id.btn_add);
        lessonDAO = new LessonDAO(this.getContext());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonFragment.this.getContext(),AddLessonActivity.class);
                startActivity(intent);
            }
        });



        updateList();
        return v;
    }

    private void updateList(){
        lessonDAO.openReadable();
        String[] from = new String[]{LessonDAO.COL_NAME, LessonDAO.COL_TRAINER};
        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this.getContext(),
                R.layout.list_entry,
                lessonDAO.getLessonCursor(),
                from, new int[]{R.id.tv_name, R.id.tv_id}
                );
        lessonDAO.close();
        lvLessons.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        updateList();
        super.onResume();
    }

    @Override
    public void updateFromAddLesson() {
        updateList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
        Intent intent = new Intent(this.getContext(), LessonActivity.class);
        intent.putExtra("lesson",lessonDAO.cursorToLesson(cursor));
        startActivity(intent);
    }
}
