package be.formation.studymanager.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;

import be.formation.studymanager.R;
import be.formation.studymanager.task.LoadTask;


public class CalendarFragment extends Fragment implements LoadTask.LoadTaskCallback{

    private static CalendarFragment instance;
    private CalendarView cv_calendar;
    private ImageView iv_calendar;

    public CalendarFragment() {
    }

    public static CalendarFragment getInstance(){
        if(instance==null)
            instance=new CalendarFragment();
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_calendar, container, false);
        cv_calendar = (CalendarView) v.findViewById(R.id.cv_calendar);
        iv_calendar = (ImageView) v.findViewById(R.id.iv_calendar);
        LoadTask loadTask = (LoadTask) new LoadTask(this)
                .execute("https://cdn4.iconfinder.com/data/icons/small-n-flat/24/calendar-128.png");
        return v;
    }

    @Override
    public void updateView(Bitmap bitmap) {
        iv_calendar.setImageBitmap(bitmap);
    }
}
