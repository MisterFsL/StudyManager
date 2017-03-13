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
        fragments.add(CalendarFragment.getInstance());

        adapter = new StudyPagerAdapter(getSupportFragmentManager(), fragments);

        vpPager = (ViewPager) findViewById(R.id.vp_pager);
        vpPager.setAdapter(adapter);

        btnLogoff.setOnClickListener(this);

        mAuth = mAuth.getInstance();
        tvUser.setText(mAuth.getCurrentUser().getEmail());

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
