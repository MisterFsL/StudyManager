package be.formation.studymanager;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import be.formation.studymanager.adapter.StudyPagerAdapter;
import be.formation.studymanager.fragment.CalendarFragment;
import be.formation.studymanager.fragment.LessonFragment;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView tvUser;
    private Button btnLogoff;
    private PagerAdapter adapter;
    private ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tvUser = (TextView) findViewById(R.id.tv_current_user);
        btnLogoff = (Button) findViewById(R.id.btn_logoff);

        List fragments = new Vector();

        fragments.add(LessonFragment.getInstance());
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        caldroidFragment.setArguments(args);
        fragments.add(caldroidFragment);

        adapter = new StudyPagerAdapter(getSupportFragmentManager(), fragments);

        vpPager = (ViewPager) findViewById(R.id.vp_pager);
        vpPager.setAdapter(adapter);

        btnLogoff.setOnClickListener(this);

        mAuth = mAuth.getInstance();
        if(mAuth.getCurrentUser()!=null) {
            tvUser.setText(mAuth.getCurrentUser().getEmail());
            StudyApplication app = (StudyApplication) getApplication();
            app.setUserId(mAuth.getCurrentUser().getUid());
        }
        else
            finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logoff:
                mAuth.signOut();
                MenuActivity.this.finish();
                break;
        }
    }
}
