package be.formation.studymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import be.formation.studymanager.db.LessonDAO;
import be.formation.studymanager.model.Lesson;

public class LessonActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private TextView tvName;
    private TextView tvTrainer;
    private TextView tvHours;
    private TextView tvIdLesson;
    private Button btnDelete;
    private Lesson lesson;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        tvHours = (TextView) findViewById(R.id.tv_lesson_hours);
        tvName = (TextView) findViewById(R.id.tv_lesson_name);
        tvIdLesson = (TextView) findViewById(R.id.tv_id_lesson);
        tvTrainer = (TextView) findViewById(R.id.tv_lesson_trainer);
        btnDelete = (Button) findViewById(R.id.btn_lesson_delete);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mf_detail_location);
        mapFragment.getMapAsync(this);

        btnDelete.setOnClickListener(this);

        lesson= (Lesson) getIntent().getExtras().get("lesson");
        Log.d("TAG",lesson.toString());
        tvName.setText(lesson.getName());
        tvTrainer.setText(lesson.getTrainer());
        tvHours.setText(String.valueOf(lesson.getHours()));
        tvIdLesson.setText(String.valueOf(lesson.getId()));
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
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(lesson.getLatitude()!=0 || lesson.getLongitude()!=0){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lesson.getLatitude(),lesson.getLongitude()))
                    .title(lesson.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lesson.getLatitude(),lesson.getLongitude()), 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }
}
