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
import android.widget.Button;
import android.widget.EditText;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.example.arsalan.mygym.webservice.WebServiceResultImplementation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.example.arsalan.mygym.MyKeys.KEY_BUNDLE_OBJ;


public class TrainerEditProfileFragment extends Fragment implements WebServiceResultImplementation {


    public static final int ACTIVITY_REQUEST_NATIONAL_CARD = 200;
    public static final int ACTIVITY_REQUEST_DOCUMENT = 201;
    private static final String ARG_PARAM1 = "param1";
    private final static String TAG = "EditProfileFragment";
    private OnFragmentInteractionListener mListener;
    private SimpleDraweeView docImg;
    private SimpleDraweeView natCardImg;

    private Uri resultDocUri;
    private Uri resultNatCardUri;

    private ProgressDialog waitingDialog;

    private Trainer mTrainer;

    public TrainerEditProfileFragment() {
        // Required empty public constructor
    }

    public static TrainerEditProfileFragment newInstance(Trainer trainer) {
        TrainerEditProfileFragment fragment = new TrainerEditProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, trainer);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTrainer = getArguments().getParcelable(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_trainer_profile_edit, container, false);
        getActivity().setTitle(getString(R.string.edit_trainer_profile));
        final EditText mealPriceTV = v.findViewById(R.id.etMealPrice);
        final EditText workoutPriceTV = v.findViewById(R.id.etWorkoutPrice);
        natCardImg = v.findViewById(R.id.imgNatCard);
        docImg = v.findViewById(R.id.imgDoc);

        if (mTrainer != null) {
            mealPriceTV.setText("" + mTrainer.getMealPlanPrice());
            workoutPriceTV.setText("" + mTrainer.getWorkoutPlanPrice());

            docImg.setImageURI(MyConst.BASE_CONTENT_URL + mTrainer.getDocThumbUrl());

            natCardImg.setImageURI(MyConst.BASE_CONTENT_URL + mTrainer.getNationalCardThumbUrl());
        }
        Button editProfileBtn = v.findViewById(R.id.btnEditProfilePic);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(3, 2)
                        .setActivityTitle(getString(R.string.choose_training_licence))
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setFixAspectRatio(true)
                        .setRequestedSize(1200, 800)
                        .getIntent(getContext());
                // .start(getContext(), TrainerEditProfileFragment.this);
                /*Intent intent = CropImage.activity()
                        .getIntent(getContext());*/
                startActivityForResult(intent, ACTIVITY_REQUEST_DOCUMENT);
            }
        });

        Button sendNatCardBtn = v.findViewById(R.id.btnEditNatCard);
        sendNatCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setActivityTitle(getString(R.string.choose_id_card))
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setRequestedSize(1200, 800)
                        .setAspectRatio(3, 2)
                        .setFixAspectRatio(true)
                        .getIntent(getContext());
                // .start(getContext(), TrainerEditProfileFragment.this);
                /*Intent intent = CropImage.activity()
                        .getIntent(getContext());*/
                startActivityForResult(intent, ACTIVITY_REQUEST_NATIONAL_CARD);
            }
        });

        Button submitBtn = v.findViewById(R.id.btnSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {

            public MultipartBody.Part thumbDocBody;
            public MultipartBody.Part docImageBody;

            public MultipartBody.Part natCardThumbBody;
            public MultipartBody.Part natCardImageBody;

            @Override
            public void onClick(View view) {
                boolean hasError = false;


                RequestBody userIdReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(((MyApplication) getActivity().getApplication()).getCurrentUser().getId()));

                RequestBody mealPriceReq = RequestBody.create(MediaType.parse("text/plain"), mealPriceTV.getText().toString());
                RequestBody workoutPriceReq = RequestBody.create(MediaType.parse("text/plain"), workoutPriceTV.getText().toString());

                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                requestBodyMap.put("userId", userIdReq);

                requestBodyMap.put("MealPlanPrice", mealPriceReq);
                requestBodyMap.put("WorkoutPlanPrice", workoutPriceReq);


                try {
                    if (resultDocUri != null) {
                        File imageFile = new File(resultDocUri.getPath());
                        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(resultDocUri.getPath()), 128, 128);
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
                        thumbDocBody =
                                MultipartBody.Part.createFormData("DocumentsThumbUrl", thumbFile.getName(), requestThumbFile);

                        // create RequestBody instance from file
                        Log.d(TAG, "onClick: mediatype:" + MimeTypeMap.getFileExtensionFromUrl(resultDocUri.getPath()));

                        final ProgressDialog waitingDialog2 = new ProgressDialog(getContext(), R.style.AlertDialogCustom);
                        waitingDialog2.setMessage(getString(R.string.uploading_picture_wait));
                        waitingDialog2.setProgress(0);
                        waitingDialog2.setMax(100);
                        waitingDialog2.setIndeterminate(false);
                        waitingDialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                        waitingDialog2.show();
                        final ProgressRequestBody requestDocFile = new ProgressRequestBody(MediaType.parse("image/jpg"), imageFile, new ProgressRequestBody.UploadCallbacks() {
                            @Override
                            public void onProgressUpdate(int percentage, String tag) {
                                Log.d("Send Picture", "onProgressUpdate: completed:" + percentage + "%");
                                waitingDialog2.setProgress(percentage);
                            }

                            @Override
                            public void onError(String tag) {
                                waitingDialog2.dismiss();
                            }

                            @Override
                            public void onFinish(String tag) {
                                waitingDialog2.setIndeterminate(true);
                                waitingDialog2.dismiss();
                            }
                        }
                                , "Video Clip");
                        docImageBody =
                                MultipartBody.Part.createFormData("DocumentsPictureUrl", imageFile.getName(), requestDocFile);
                    }


                } catch (IOException e) {
                    e.printStackTrace();

                }

                try {
                    if (resultNatCardUri != null) {
                        File natCardFile = new File(resultNatCardUri.getPath());
                        Bitmap thumbNatCard = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(resultNatCardUri.getPath()), 128, 128);
                        //create a file to write bitmap data
                        File thumbFile = new File(getContext().getCacheDir(), "thumb2.jpg");


                        thumbFile.createNewFile();
                        //Convert bitmap to byte array
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        thumbNatCard.compress(Bitmap.CompressFormat.JPEG, 60 /*ignored for PNG*/, bos);
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
                        natCardThumbBody =
                                MultipartBody.Part.createFormData("NationalCardThumbUrl", thumbFile.getName(), requestThumbFile);

                        // create RequestBody instance from file
                        Log.d(TAG, "onClick: mediatype:" + MimeTypeMap.getFileExtensionFromUrl(resultNatCardUri.getPath()));

                        waitingDialog = new ProgressDialog(getContext(), R.style.AlertDialogCustom);
                        waitingDialog.setMessage(getString(R.string.uploading_picture_wait));
                        waitingDialog.setProgress(0);
                        waitingDialog.setMax(100);
                        waitingDialog.setIndeterminate(false);
                        waitingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        waitingDialog.show();
                        final ProgressRequestBody requestNatCardFile = new ProgressRequestBody(MediaType.parse("image/jpg"), natCardFile, new ProgressRequestBody.UploadCallbacks() {
                            @Override
                            public void onProgressUpdate(int percentage, String tag) {
                                Log.d("Send Picture", "onProgressUpdate: completed:" + percentage + "%");
                                waitingDialog.setProgress(percentage);
                            }

                            @Override
                            public void onError(String tag) {
                                waitingDialog.dismiss();
                            }

                            @Override
                            public void onFinish(String tag) {
                                waitingDialog.setIndeterminate(true);
                            }
                        }
                                , "Video Clip");
                        natCardImageBody =
                                MultipartBody.Part.createFormData("NationalCardPictureUrl", natCardFile.getName(), requestNatCardFile);
                    }


                } catch (IOException e) {
                    e.printStackTrace();

                }
                MyWebService.editTrainerProfile(getActivity()
                        , requestBodyMap
                        , TrainerEditProfileFragment.this
                        , resultDocUri != null ? docImageBody : null
                        , resultDocUri != null ? thumbDocBody : null
                        , resultNatCardUri != null ? natCardImageBody : null
                        , resultNatCardUri != null ? natCardThumbBody : null
                );


            }
        });
        View waitingview = v.findViewById(R.id.flWaiting);
        MyWebService.getTrainerProfileDetail((AppCompatActivity) getActivity(), ((MyApplication) getActivity().getApplication()).getCurrentUser().getId(), new WebServiceResultImplementation() {
                    @Override
                    public void webServiceOnSuccess(Bundle bundle) {

                        Trainer trainer = bundle.getParcelable(KEY_BUNDLE_OBJ);
                        if (trainer == null) return;
                        mealPriceTV.setText("" + trainer.getMealPlanPrice());
                        workoutPriceTV.setText("" + trainer.getWorkoutPlanPrice());
                        docImg.setImageURI(MyConst.BASE_CONTENT_URL + trainer.getDocThumbUrl());
                        natCardImg.setImageURI(MyConst.BASE_CONTENT_URL + trainer.getNationalCardThumbUrl());
                        waitingview.setVisibility(View.GONE);
                    }

                    @Override
                    public void webServiceOnFail() {
                        waitingview.setVisibility(View.GONE);

                    }
                }
        );

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_REQUEST_DOCUMENT) { //کارت مربیگری
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultDocUri = result.getUri();
                Log.d("EDITPROFILEACTIVITY", "onActivityResult: Doc:" + resultDocUri);
                docImg.setImageURI(resultDocUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == ACTIVITY_REQUEST_NATIONAL_CARD) { //کارت ملی
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultNatCardUri = result.getUri();
                Log.d("EDITPROFILEACTIVITY", "onActivityResult: natCar:" + resultNatCardUri);
                natCardImg.setImageURI(resultNatCardUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void webServiceOnSuccess(Bundle bundle) {
        if (waitingDialog != null)
            waitingDialog.dismiss();
        MyWebService.getProfileDetail(((MyApplication) getActivity().getApplication()).getCurrentUser().getUserName(), ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), (AppCompatActivity) getActivity(), new WebServiceResultImplementation() {
            @Override
            public void webServiceOnSuccess(Bundle bundle) {
                mListener.onSuccessfulTrainerEditProfile();
            }

            @Override
            public void webServiceOnFail() {

            }
        });

    }

    @Override
    public void webServiceOnFail() {
        if (waitingDialog != null)
            waitingDialog.dismiss();
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

    public interface OnFragmentInteractionListener {
        void onSuccessfulTrainerEditProfile();

    }


}
