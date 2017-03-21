package be.formation.studymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import be.formation.studymanager.db.LessonDAO;
import be.formation.studymanager.model.Lesson;

public class LessonActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvName;
    private TextView tvTrainer;
    private TextView tvHours;
    private Button btnDelete;
    private Button btnUpdate;
    private Lesson lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        tvHours = (TextView) findViewById(R.id.tv_lesson_hours);
        tvName = (TextView) findViewById(R.id.tv_lesson_name);
        tvTrainer = (TextView) findViewById(R.id.tv_lesson_trainer);
        btnDelete = (Button) findViewById(R.id.btn_lesson_delete);
        btnUpdate = (Button) findViewById(R.id.btn_lesson_update);

        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        lesson= (Lesson) getIntent().getExtras().get("lesson");
        Log.d("TAG",lesson.toString());
        tvName.setText(lesson.getName());
        tvTrainer.setText(lesson.getTrainer());
        tvHours.setText(String.valueOf(lesson.getHours()));
    }

    @Override
    public void onClick(View v) {
        LessonDAO lessonDAO = new LessonDAO(this);
        lessonDAO.openWritable();
        switch (v.getId()){
            case R.id.btn_lesson_delete:
                lessonDAO.delete(lesson);
                lessonDAO.close();
                finish();
                break;
            case R.id.btn_lesson_update:
                finish();
                break;
        }
    }
}
