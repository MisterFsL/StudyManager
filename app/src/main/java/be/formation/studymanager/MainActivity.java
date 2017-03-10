package be.formation.studymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG = "MainActivity";
    private Button btnSignin;
    private Button btnSingup;
    private Button btnGoogle;
    private EditText etEmail;
    private EditText etPassword;
    private ImageView logo;
    private ProgressBar progress;
    private CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //region views
        btnSignin = (Button) findViewById(R.id.btn_signin);
        btnSingup = (Button) findViewById(R.id.btn_signup);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        logo = (ImageView) findViewById(R.id.iv_logo);
        progress = (ProgressBar) findViewById(R.id.pb_loading);
        remember = (CheckBox) findViewById(R.id.cb_remember);
        btnSignin.setOnClickListener(this);
        btnSingup.setOnClickListener(this);
        //endregion
        getPreferences();
        //region firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    savePreferences();
                    Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                    startActivity(intent);
                    //TODO startActivity
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        //endregion
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        mAuth.signOut();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }

    public void createAccount(String email,String password){
        //TODO Check mail & adress
        if(email != null && password !=null &&
                !email.isEmpty() && !password.isEmpty()) {
            startLoading();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            endLoading();
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // ...
                        }
                    });
        } else {
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        }
    }

    public void signIn(String email,String password){
        if(email != null && password !=null &&
                !email.isEmpty() && !password.isEmpty()) {
            startLoading();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            endLoading();
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }else {
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        if(remember.isChecked()){
            editor.putBoolean("remember",true);
            editor.putString("email",etEmail.getText().toString());
            editor.putString("password",etPassword.getText().toString());
            editor.apply();
        } else {
            editor.clear();
            editor.apply();
        }
    }

    private void getPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        remember.setChecked(prefs.getBoolean("remember",false));
        etPassword.setText(prefs.getString("password",""));
        etEmail.setText(prefs.getString("email",""));
    }

    @Override
    protected void onRestart() {
        getPreferences();
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signin:
                signIn(etEmail.getText().toString(),
                        etPassword.getText().toString());
                break;
            case R.id.btn_signup:
                createAccount(etEmail.getText().toString(),
                        etPassword.getText().toString());
                break;
        }
    }

    private void startLoading(){
        btnSingup.setEnabled(false);
        btnSignin.setEnabled(false);
        logo.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }

    private void endLoading(){
        btnSingup.setEnabled(true);
        btnSignin.setEnabled(true);
        logo.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }
}
