package be.formation.studymanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import be.formation.studymanager.db.LessonDAO;
import be.formation.studymanager.model.Lesson;

public class AddLessonActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener
        , LocationListener {

    @NotEmpty
    private EditText etName;
    private EditText etCategory;
    @NotEmpty
    private EditText etHours;
    @NotEmpty
    private EditText etTrainer;
    private Button btnAdd;
    private Button btnCancel;
    private Validator validator;
    private MapFragment mapFragment;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 999;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Marker marker;
    private LatLng locationLesson;
    private LocationRequest mLocationRequest;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMapClickListener(this);
        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0)));
        marker.setVisible(false);
        createLocationRequest();
        Log.d("TAG", "MAPREADY");
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            Log.d("MAP", "LASTLOCATION OK");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void moveMarker(LatLng latLng) {
        marker.setPosition(latLng);
        locationLesson = latLng;
        marker.setVisible(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("MAP", "NOICE");
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("MAP", "SUSP");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("MAP", "FAILED");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        moveMarker(latLng);

    }

    private void moveCameraToLocation(LatLng location) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

    @Override
    public void onLocationChanged(Location location) {
        moveMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        moveCameraToLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        Log.d("MAP", "CHANGED");
    }

    public interface AddLessonCallback {
        void updateFromAddLesson();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAP", "CREATE");
        setContentView(R.layout.activity_add_lesson);
        etName = (EditText) findViewById(R.id.et_add_name);
        etCategory = (EditText) findViewById(R.id.et_add_category);
        etHours = (EditText) findViewById(R.id.et_add_hours);
        etTrainer = (EditText) findViewById(R.id.et_add_trainer);
        btnAdd = (Button) findViewById(R.id.btn_add_activity_add);
        btnCancel = (Button) findViewById(R.id.btn_add_cancel);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mf_map_lesson);
        mapFragment.getMapAsync(this);

        btnCancel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        LessonDAO lessonDAO = new LessonDAO(this);
        lessonDAO.openWritable();
        Lesson l = new Lesson(etName.getText().toString(),
                etTrainer.getText().toString(),
                etCategory.getText().toString(),
                Integer.parseInt(etHours.getText().toString())
        );
        if (locationLesson != null) {
            l.setLatitude(locationLesson.latitude);
            l.setLongitude(locationLesson.longitude);
        }
        lessonDAO.insert(l);
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

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
}
