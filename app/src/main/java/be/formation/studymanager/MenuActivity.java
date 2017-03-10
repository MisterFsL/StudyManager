package be.formation.studymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView tvUser;
    private Button btnLogoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tvUser = (TextView) findViewById(R.id.tv_current_user);
        btnLogoff= (Button) findViewById(R.id.btn_logoff);

        btnLogoff.setOnClickListener(this);

        mAuth=mAuth.getInstance();
        tvUser.setText(mAuth.getCurrentUser().getEmail());
            }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logoff:
                mAuth.signOut();
                MenuActivity.this.finish();
                break;
        }
    }
}
