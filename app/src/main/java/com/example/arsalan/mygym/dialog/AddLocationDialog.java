package com.example.arsalan.mygym.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class AddLocationDialog extends DialogFragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_LATLNG = "param3";

    // TODO: Rename and change types of parameters
    private String mCityName;
    private String mProvinceName;

    private OnFragmentInteractionListener mListener;

    private static final String[] MAP_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_MAP_PERMISSIONS_REQUEST = 1000;
    private GoogleMap map;
    private MapView m;
    private final String TAG = this.getClass().getSimpleName();
    private Button OkBtn;
    private LatLng mLatLng;

    public AddLocationDialog() {
        // Required empty public constructor
    }

    public static AddLocationDialog newInstance(String cityName, String provinceName) {
        AddLocationDialog fragment = new AddLocationDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, cityName);
        args.putString(ARG_PARAM2, provinceName);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddLocationDialog newInstance(LatLng latLng) {
        AddLocationDialog fragment = new AddLocationDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LATLNG, latLng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getParcelable(ARG_LATLNG) != null) {
                mLatLng = getArguments().getParcelable(ARG_LATLNG);
            } else {
                mCityName = getArguments().getString(ARG_PARAM1);
                mProvinceName = getArguments().getString(ARG_PARAM2);
                mLatLng = getLocationFromAddress(getContext(), mCityName + ", " + mProvinceName);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_location_dialog, container, false);
        OkBtn = v.findViewById(R.id.btnOk);
        m = v.findViewById(R.id.mapView);
        m.onCreate(savedInstanceState);
        m.getMapAsync(this);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        m.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        m.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m.onDestroy();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");

        map = googleMap;
        map.setOnMapClickListener(latLng -> {
            Log.d(TAG, "onMapClick: mLatLng:" + latLng.toString());

            mLatLng = latLng;


            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Setting the position for the marker
            markerOptions.position(latLng);

            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(latLng.latitude + " : " + latLng.longitude);

            // Clears the previously touched position
            map.clear();

            // Animating to the touched position
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            // Placing a marker on the touched position
            map.addMarker(markerOptions);
        });
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(MAP_PERMISSIONS, REQUEST_MAP_PERMISSIONS_REQUEST);
            return;
        }

        if (mLatLng != null) {
            OkBtn.setOnClickListener(view -> {
                //  mListener.setLatLng(mLatLng);
                Intent intent = new Intent();
                intent.putExtra("Lng", mLatLng.longitude);
                intent.putExtra("Lat", mLatLng.latitude);

                getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_OK, intent);
                dismiss();
            });
            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Setting the position for the marker
            markerOptions.position(mLatLng);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    // .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .target(mLatLng)
                    .zoom(13)                   // Sets the zoom
                    // .bearing(90)                // Sets the orientation of the camera to east
                    // .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            // Clears the previously touched position
            map.clear();

            // Placing a marker on the touched position
            map.addMarker(markerOptions);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MAP_PERMISSIONS_REQUEST) {
            m.getMapAsync(this);

        }
    }

    private LatLng getLocationFromAddress(Context context, String strAddress) {

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


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setDimAmount(0.0f);
        return dialog;
    }

    public interface OnFragmentInteractionListener {
        void setLatLng(LatLng latLng);
    }
}
