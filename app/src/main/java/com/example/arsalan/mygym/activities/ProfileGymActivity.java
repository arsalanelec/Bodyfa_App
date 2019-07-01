package com.example.arsalan.mygym.activities;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.Gym;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.example.arsalan.mygym.models.MyConst.BASE_API_URL;

public class ProfileGymActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String[] MAP_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_MAP_PERMISSIONS_REQUEST = 1000;
    private final String TAG = this.getClass().getSimpleName();
    GoogleMap map;
    final Context mContext;
    private Gym mCurrentGym;
    private MapView m;
    private ImageView imgGym;

    public ProfileGymActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gym);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();

        imgGym = findViewById(R.id.imgGym);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //  String imageTransitionName = extras.getString(EXTRA_IMAGE_TRANSITION_NAME);
            //  imgGym.setTransitionName(imageTransitionName);
        }
        if (extras != null) {
            mCurrentGym = extras.getParcelable(MyKeys.EXTRA_OBJ_GYM);
        }

        Glide.with(this)
                .load(BASE_API_URL + mCurrentGym.getPictureUrl())
                .apply(new RequestOptions().placeholder(R.drawable.avatar).centerCrop())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //  supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imgGym);


        TextView titleTV = findViewById(R.id.txtName);
        titleTV.setText(mCurrentGym.getTitle());

        TextView addressTV = findViewById(R.id.txtAddress);
        addressTV.setText(mCurrentGym.getAddress());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        m = findViewById(R.id.mapView);
        m.onCreate(savedInstanceState);
        m.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");

        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileGymActivity.this, MAP_PERMISSIONS, REQUEST_MAP_PERMISSIONS_REQUEST);
            return;
        }*/
        handleOnMapReady();

    }

    private void handleOnMapReady() {
       /* LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
*/
        //   Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        // if (location != null) {
        // Creating a marker

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(mCurrentGym.getLat(), mCurrentGym.getLng());
        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(mCurrentGym.getTitle());//latLng.latitude + " : " + latLng.longitude);
        map.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                //.target(getLocationFromAddress(mContext,"شیراز"))
                .zoom(17)                   // Sets the zoom
                // .bearing(90)                // Sets the orientation of the camera to east
                // .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        // }
    }

    @Override
    protected void onResume() {
        super.onResume();
        m.onResume();

    }

    @Override
    protected void onPause() {


        super.onPause();
        m.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MAP_PERMISSIONS_REQUEST) {
            handleOnMapReady();
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            Log.d(TAG, "getLocationFromAddress: cnt:" + address.size());
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}
