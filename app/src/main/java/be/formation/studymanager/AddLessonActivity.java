package be.formation.studymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import be.formation.studymanager.db.LessonDAO;
import be.formation.studymanager.model.Lesson;

public class AddLessonActivity extends AppCompatActivity implements View.OnClickListener,Validator.ValidationListener{

    @NotEmpty
    private EditText etName;
    @NotEmpty
    private EditText etCategory;
    @NotEmpty
    private EditText etHours;
    @NotEmpty
    private EditText etTrainer;
    private Button btnAdd;
    private Button btnCancel;
    private Validator validator;
    private AddLessonCallback callback;

    public interface AddLessonCallback{
        void updateFromAddLesson();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        etName= (EditText) findViewById(R.id.et_add_name);
        etCategory = (EditText) findViewById(R.id.et_add_category);
        etHours = (EditText) findViewById(R.id.et_add_hours);
        etTrainer= (EditText) findViewById(R.id.et_add_trainer);
        btnAdd= (Button) findViewById(R.id.btn_add_activity_add);
        btnCancel= (Button) findViewById(R.id.btn_add_cancel);
        btnCancel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        validator= new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_cancel:
                finish();
                break;
            case R.id.btn_add_activity_add:
                validator.validate();
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        LessonDAO lessonDAO=new LessonDAO(this);
        lessonDAO.openWritable();
        lessonDAO.insert(new Lesson(etName.getText().toString(),
                etTrainer.getText().toString(),
                etCategory.getText().toString(),
                Integer.parseInt(etHours.getText().toString())));
        lessonDAO.close();
        finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
