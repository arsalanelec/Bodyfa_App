package com.example.arsalan.mygym.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterCitySp;
import com.example.arsalan.mygym.adapters.AdapterProvinceSp;
import com.example.arsalan.mygym.dialog.AddLocationDialog;
import com.example.arsalan.mygym.models.City;
import com.example.arsalan.mygym.models.CityNState;
import com.example.arsalan.mygym.models.Gym;
import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.Province;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.model.LatLng;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.arsalan.mygym.models.MyConst.BASE_API_URL;


public class GymEditProfileFragment extends Fragment implements AddLocationDialog.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GYM = "param1";
    private static final String TAG = "GymEditProfileFragment";
    private Gym mCurrentGym;
    private OnFragmentInteractionListener mListener;
    private Uri resultUri;
    private SimpleDraweeView gymImg;

    private LatLng mLatLong;

    private MultipartBody.Part thumbBody;
    private MultipartBody.Part imageBody;
    private int REQ_ADD_LOCATION = 100;

    public GymEditProfileFragment() {
        // Required empty public constructor
    }


    public static GymEditProfileFragment newInstance(Gym gym) {
        GymEditProfileFragment fragment = new GymEditProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GYM, gym);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentGym = getArguments().getParcelable(ARG_GYM);
            if (mCurrentGym != null) {
                mLatLong = new LatLng(
                        mCurrentGym.getLat(),
                        mCurrentGym.getLon());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gym_edit_profile, container, false);
        final EditText titleET = v.findViewById(R.id.etTitle);
        titleET.setText(mCurrentGym.getTitle());

        final EditText addressET = v.findViewById(R.id.etAddress);
        addressET.setText(mCurrentGym.getAddress());

        final EditText descriptionET = v.findViewById(R.id.etDescription);
        descriptionET.setText(mCurrentGym.getDescription());

        //استانها
        final Spinner provinceSp = v.findViewById(R.id.spnProvince);
        final Spinner citySp = v.findViewById(R.id.spnCity);
        provinceSp.setAdapter(new AdapterProvinceSp());
        if (mCurrentGym.getCityId() > 0) {
            for (int i = 0; i < provinceSp.getAdapter().getCount(); i++) {
                if (provinceSp.getItemIdAtPosition(i) == CityNState.getProvinceByCityId(mCurrentGym.getCityId()).getId()) {
                    provinceSp.setSelection(i);
                    citySp.setAdapter(new AdapterCitySp(CityNState.getProvinceByCityId(mCurrentGym.getCityId()).getId()));
                    for (int j = 0; j < citySp.getAdapter().getCount(); j++) {
                        if (citySp.getItemIdAtPosition(j) == mCurrentGym.getCityId()) {
                            citySp.setSelection(j);
                            break;
                        }

                    }
                    break;
                }
            }
        } else {
            provinceSp.setSelection(16); //فارس
            citySp.setAdapter(new AdapterCitySp(17));
            for (int j = 0; j < citySp.getAdapter().getCount(); j++) {
                if (citySp.getItemIdAtPosition(j) == 262) { //شیراز
                    citySp.setSelection(j);
                    break;
                }

            }
        }

        provinceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (((AdapterCitySp) citySp.getAdapter()).getProvinceId() != l)
                    citySp.setAdapter(new AdapterCitySp(l));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button addLocationBtn = v.findViewById(R.id.btnAddLocation);
        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddLocationDialog dialog;
                if (mLatLong.latitude > 0.0) { //by lat lng
                    Log.d(TAG, "add location by lat lng: " + mLatLong.latitude + " lng:" + mLatLong.longitude);
                    dialog = AddLocationDialog.newInstance(mLatLong);

                } else { //by city name
                    Log.d(TAG, "add location by city");
                    dialog = AddLocationDialog.newInstance(((City) citySp.getSelectedItem()).getName(), ((Province) provinceSp.getSelectedItem()).getName());
                }
                dialog.setTargetFragment(GymEditProfileFragment.this, REQ_ADD_LOCATION);
                dialog.show(getFragmentManager(), "");
            }
        });

        gymImg = v.findViewById(R.id.imgGym);
        gymImg.setImageURI(BASE_API_URL + mCurrentGym.getPictureUrl());

        Button changeImageBtn = v.findViewById(R.id.btnEditImage);
        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setActivityTitle(getString(R.string.choose_gym_pic))
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setAspectRatio(3, 2)
                        .setFixAspectRatio(true)
                        .setRequestedSize(1200, 800)
                        .start(getContext(), GymEditProfileFragment.this);
            }
        });
        final EditText phone1ET = v.findViewById(R.id.etPhone1);
        phone1ET.setText(mCurrentGym.getPhone1());

        final EditText phone2ET = v.findViewById(R.id.etPhone2);
        phone2ET.setText(mCurrentGym.getPhone2());

        final EditText halfMonthFeeET = v.findViewById(R.id.etHalfMonthFee);
        halfMonthFeeET.setText(String.valueOf(mCurrentGym.getHalfMonthFee()));

        final EditText monthlyFeeET = v.findViewById(R.id.etMonthlyFee);
        monthlyFeeET.setText(String.valueOf(mCurrentGym.getMonthlyFee()));

        Button submitBtn = v.findViewById(R.id.btnSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public ProgressDialog waitingDialog;

            @Override
            public void onClick(View view) {
                if (titleET.getText().toString().isEmpty()) {
                    titleET.setError(getString(R.string.title_is_empty));
                    titleET.requestFocus();
                    return;
                }


                Log.d(getClass().getSimpleName(), "user id:\"" + mCurrentGym.getId());


                MediaType plainMT = MediaType.parse("text/plain");
                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                requestBodyMap.put("UserId", RequestBody.create(plainMT, String.valueOf(mCurrentGym.getId())));
                requestBodyMap.put("Title", RequestBody.create(plainMT, String.valueOf(titleET.getText().toString())));
                requestBodyMap.put("Address", RequestBody.create(plainMT, String.valueOf(addressET.getText().toString())));
                requestBodyMap.put("Phone1", RequestBody.create(plainMT, String.valueOf(phone1ET.getText().toString())));
                requestBodyMap.put("Phone2", RequestBody.create(plainMT, String.valueOf(phone2ET.getText().toString())));
                requestBodyMap.put("Lat", RequestBody.create(plainMT, String.valueOf(mLatLong.latitude)));
                requestBodyMap.put("Long", RequestBody.create(plainMT, String.valueOf(mLatLong.longitude)));
                requestBodyMap.put("Description", RequestBody.create(plainMT, descriptionET.getText().toString()));
                requestBodyMap.put("CityId", RequestBody.create(plainMT, String.valueOf(citySp.getSelectedItemId())));
                requestBodyMap.put("HalfMonthFee", RequestBody.create(plainMT, halfMonthFeeET.getText().toString()));
                requestBodyMap.put("MonthlyFee", RequestBody.create(plainMT, monthlyFeeET.getText().toString()));

                requestBodyMap.put("Description", RequestBody.create(plainMT, descriptionET.getText().toString()));


                try {
                    if (resultUri != null) {
                        File imageFile = new File(resultUri.getPath());
                        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(resultUri.getPath()), 128, 128);
                        //create a file to write bitmap data
                        File thumbFile = new File(getContext().getCacheDir(), "thumb.jpg");


                        thumbFile.createNewFile();
                        //Convert bitmap to byte array
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        thumbImage.compress(Bitmap.CompressFormat.JPEG, 60 /*ignored for PNG*/, bos);
                        byte[] bitmapData = bos.toByteArray();
                        //write the bytes in file
                        FileOutputStream fos = new FileOutputStream(thumbFile);
                        fos.write(bitmapData);
                        fos.flush();
                        fos.close();
                        final RequestBody requestThumbFile =
                                RequestBody.create(
                                        MediaType.parse("image/jpg"),
                                        thumbFile);
                        thumbBody =
                                MultipartBody.Part.createFormData("ThumbUrl", thumbFile.getName(), requestThumbFile);


                        // create RequestBody instance from file
                        Log.d(TAG, "onClick: mediatype:" + MimeTypeMap.getFileExtensionFromUrl(resultUri.getPath()));


                        waitingDialog = new ProgressDialog(getContext(), R.style.AlertDialogCustom);
                        waitingDialog.setMessage(getString(R.string.uploading_picture_wait));
                        waitingDialog.setProgress(0);
                        waitingDialog.setMax(100);
                        waitingDialog.setIndeterminate(false);
                        waitingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        waitingDialog.setCancelable(false);
                        waitingDialog.show();
                        final ProgressRequestBody requestFile = new ProgressRequestBody(MediaType.parse("image/jpg"), imageFile, new ProgressRequestBody.UploadCallbacks() {
                            @Override
                            public void onProgressUpdate(int percentage, String tag) {
                                Log.d("Send picture", "onProgressUpdate: completed:" + percentage + "%");
                                waitingDialog.setProgress(percentage);
                            }

                            @Override
                            public void onError(String tag) {
                                waitingDialog.dismiss();
                            }

                            @Override
                            public void onFinish(String tag) {
                                waitingDialog.dismiss();
                                waitingDialog.setIndeterminate(true);
                            }
                        }
                                , "the picture");
                        imageBody =
                                MultipartBody.Part.createFormData("PictureUrl", imageFile.getName(), requestFile);
                    }

                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

                    Call<RetroResult> call = apiService.addEditGymDetail("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), requestBodyMap, resultUri != null ? imageBody : null, resultUri != null ? thumbBody : null);
                    call.enqueue(new Callback<RetroResult>() {
                        @Override
                        public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                            //   waitingDialog.dismiss();
                            if (response.isSuccessful()) {
                                Log.d(TAG, "onClick: response is ok");
                                mListener.onSuccessfulGymEditProfile();
                            } else {
                                try {
                                    Log.d(TAG, "onClick: response is not ok:" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<RetroResult> call, Throwable t) {
                            Log.d(TAG, "onClick: response is Error:" + t.getMessage());
                            //    waitingDialog.dismiss();
                        }
                    });


                    //     MyWebService.editProfile(getActivity(), requestBodyMap, GymEditProfileFragment.this, resultUri != null ? imageBody : null, resultUri != null ? thumbBody : null);
                } catch (IOException e) {
                    e.printStackTrace();

                }


            }


        });
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setLatLng(LatLng latLng) {

        mLatLong = latLng;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.d("EDITPROFILEACTIVITY", "onActivityResult: " + resultUri);
                gymImg.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == REQ_ADD_LOCATION) {
            if (resultCode == MyKeys.RESULT_OK) {
                Log.d(TAG, "onActivityResult: lat lang:" + data.getDoubleExtra("Lat", 0.0) + data.getDoubleExtra("Lng", 0.0));
                mLatLong = new LatLng(data.getDoubleExtra("Lat", 0.0), data.getDoubleExtra("Lng", 0.0));
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onSuccessfulGymEditProfile();
    }
}
