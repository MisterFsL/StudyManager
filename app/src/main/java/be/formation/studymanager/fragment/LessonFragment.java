package be.formation.studymanager.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import be.formation.studymanager.R;
import be.formation.studymanager.db.LessonDAO;
import be.formation.studymanager.db.UserDAO;
import be.formation.studymanager.model.Lesson;

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonFragment extends Fragment {


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
        btnAdd = (FloatingActionButton) v.findViewById(R.id.btn_add);
        lessonDAO = new LessonDAO(this.getContext());
        updateList();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
            }
        });


        myRef= FirebaseDatabase.getInstance().getReference("Lesson");
        Lesson l = new Lesson();
        l.setName("Android");
        Lesson l2 = new Lesson();
        l2.setName("Java");
        ArrayList<Lesson> list=new ArrayList<>();
        list.add(l);
        list.add(l2);
        myRef.setValue(l);

        return v;
    }

    private void updateList(){
        String[] from = new String[]{LessonDAO.COL_NAME, LessonDAO.COL_ID};
        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this.getContext(),
                android.R.layout.simple_list_item_1,
                lessonDAO.getLessonCursor(),
                from,new int[]{1}   //TODO !!!!!!!!!!!!!!!!!!!!!
                );
        lvLessons.setAdapter(adapter);
    }
}
