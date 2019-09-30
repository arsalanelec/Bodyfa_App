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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterCitySp;
import com.example.arsalan.mygym.adapters.AdapterProvinceSp;
import com.example.arsalan.mygym.databinding.FragmentGymEditProfileBinding;
import com.example.arsalan.mygym.dialog.AddLocationDialog;
import com.example.arsalan.mygym.models.City;
import com.example.arsalan.mygym.models.CityNState;
import com.example.arsalan.mygym.models.Gym;
import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.Province;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.google.android.gms.maps.model.LatLng;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.arsalan.mygym.models.MyConst.BASE_CONTENT_URL;


public class GymEditProfileFragment extends Fragment implements AddLocationDialog.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GYM = "param1";
    private static final String TAG = "GymEditProfileFragment";
    private final int REQ_ADD_LOCATION = 100;
    private Gym mCurrentGym;
    private OnFragmentInteractionListener mListener;
    private Uri resultUri;
    private LatLng mLatLong;
    private MultipartBody.Part thumbBody;
    private MultipartBody.Part imageBody;
    private FragmentGymEditProfileBinding mBind;

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
            if (mCurrentGym == null) {
                Log.d(TAG, "onCreate: Current Gym is Null!");
                mCurrentGym = new Gym();
            } else {
                Log.d(TAG, "onCreate: GYM ins Not Null Title:" + mCurrentGym.getTitle());
            }
            /*if(mCurrentGym.getLat()!=0 && mCurrentGym.getLng()!=0) {
                mLatLong = new LatLng(
                        mCurrentGym.getLat(),
                        mCurrentGym.getLng());
            }else {
                mLatLong=new LatLng(29.636080, 52.525654);   //Baghe Eram
            }*/
            mLatLong = new LatLng(
                    mCurrentGym.getLat(),
                    mCurrentGym.getLng());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBind = FragmentGymEditProfileBinding.inflate(inflater, container, false);
        mBind.setGym(mCurrentGym);

        //استانها
        mBind.spnProvince.setAdapter(new AdapterProvinceSp());
        if (mCurrentGym.getCityId() > 0) {
            for (int i = 0; i < mBind.spnProvince.getAdapter().getCount(); i++) {
                if (mBind.spnProvince.getItemIdAtPosition(i) == CityNState.getProvinceByCityId(mCurrentGym.getCityId()).getId()) {
                    mBind.spnProvince.setSelection(i);
                    mBind.spnCity.setAdapter(new AdapterCitySp(CityNState.getProvinceByCityId(mCurrentGym.getCityId()).getId()));
                    for (int j = 0; j < mBind.spnCity.getAdapter().getCount(); j++) {
                        if (mBind.spnCity.getItemIdAtPosition(j) == mCurrentGym.getCityId()) {
                            mBind.spnCity.setSelection(j);
                            break;
                        }

                    }
                    break;
                }
            }
        } else {
            mBind.spnProvince.setSelection(16); //فارس
            mBind.spnCity.setAdapter(new AdapterCitySp(17));
            for (int j = 0; j < mBind.spnCity.getAdapter().getCount(); j++) {
                if (mBind.spnCity.getItemIdAtPosition(j) == 262) { //شیراز
                    mBind.spnCity.setSelection(j);
                    break;
                }

            }
        }

        mBind.spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (((AdapterCitySp) mBind.spnCity.getAdapter()).getProvinceId() != l)
                    mBind.spnCity.setAdapter(new AdapterCitySp(l));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBind.btnAddLocation.setOnClickListener(view -> {
            AddLocationDialog dialog;
            if (mLatLong.latitude != 0.0) { //by lat lng
                Log.d(TAG, "add location by lat lng: " + mLatLong.latitude + " lng:" + mLatLong.longitude);
                dialog = AddLocationDialog.newInstance(mLatLong);

            } else { //by city name
                Log.d(TAG, "add location by city");
                dialog = AddLocationDialog.newInstance(((City) mBind.spnCity.getSelectedItem()).getName(), ((Province) mBind.spnProvince.getSelectedItem()).getName());
            }
            dialog.setTargetFragment(GymEditProfileFragment.this, REQ_ADD_LOCATION);
            dialog.show(getFragmentManager(), "");
        });

        mBind.imgGym.setImageURI(BASE_CONTENT_URL + mCurrentGym.getPictureUrl());

        mBind.btnEditImage.setOnClickListener(view -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setActivityTitle(getString(R.string.choose_gym_pic))
                .setAllowFlipping(false)
                .setAllowRotation(false)
                .setAspectRatio(3, 2)
                .setFixAspectRatio(true)
                .setRequestedSize(1200, 800)
                .start(getContext(), GymEditProfileFragment.this));

        mBind.btnSubmit.setOnClickListener(new View.OnClickListener() {
            public ProgressDialog waitingDialog;

            @Override
            public void onClick(View view) {
                if (mBind.etTitle.getText().toString().isEmpty()) {
                    mBind.etTitle.setError(getString(R.string.title_is_empty));
                    mBind.etTitle.requestFocus();
                    return;
                }


                Log.d(getClass().getSimpleName(), "user id:\"" + mCurrentGym.getId());


                MediaType plainMT = MediaType.parse("text/plain");
                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                requestBodyMap.put("UserId", RequestBody.create(plainMT, String.valueOf(((MyApplication) getActivity().getApplication()).getCurrentUser().getId())));
                requestBodyMap.put("Title", RequestBody.create(plainMT, mBind.etTitle.getText().toString()));
                requestBodyMap.put("Address", RequestBody.create(plainMT, mBind.etAddress.getText().toString()));
                requestBodyMap.put("Phone1", RequestBody.create(plainMT, mBind.etPhone1.getText().toString()));
                requestBodyMap.put("Phone2", RequestBody.create(plainMT, mBind.etPhone2.getText().toString()));
                requestBodyMap.put("Lat", RequestBody.create(plainMT, String.valueOf(mLatLong.latitude)));
                requestBodyMap.put("Long", RequestBody.create(plainMT, String.valueOf(mLatLong.longitude)));
                requestBodyMap.put("Description", RequestBody.create(plainMT, mBind.etDescription.getText().toString()));
                requestBodyMap.put("CityId", RequestBody.create(plainMT, String.valueOf(mBind.spnCity.getSelectedItemId())));
                requestBodyMap.put("OneDayFee", RequestBody.create(plainMT, mBind.etRegisterDailyFee.getText().toString()));
                requestBodyMap.put("WeeklyFee", RequestBody.create(plainMT, mBind.etRegisterWeeklyFee.getText().toString()));
                requestBodyMap.put("TwelveDaysFee", RequestBody.create(plainMT, mBind.etRegisterTwelveFee.getText().toString()));
                requestBodyMap.put("HalfMonthFee", RequestBody.create(plainMT, mBind.etRegister1HalfMonthFee.getText().toString()));
                requestBodyMap.put("MonthlyFee", RequestBody.create(plainMT, mBind.etRegisterMonthlyFee.getText().toString()));

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


                        waitingDialog = new ProgressDialog(getContext());
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
                            waitingDialog.dismiss();
                            Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClick: response is Error:" + t.getCause());
                            //    waitingDialog.dismiss();
                        }
                    });


                    //     MyWebService.editProfile(getActivity(), requestBodyMap, GymEditProfileFragment.this, resultUri != null ? imageBody : null, resultUri != null ? thumbBody : null);
                } catch (IOException e) {
                    e.printStackTrace();

                }


            }


        });
        return mBind.getRoot();
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
                mBind.imgGym.setImageURI(resultUri);
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
