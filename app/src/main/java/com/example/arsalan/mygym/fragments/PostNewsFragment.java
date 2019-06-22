package com.example.arsalan.mygym.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostNewsFragment extends Fragment {
    private static final String TAG = "PostNewsFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private long mUserId;
    private boolean mIsAthlete;
    private Spinner mCateSpn;
    private TextInputEditText contentET;
    private TextInputEditText titleET;
    private Uri resultUri;
    private ImageView mImage;


    public PostNewsFragment() {
        // Required empty public constructor
    }

    public static PostNewsFragment newInstance(long userId, boolean isAthlete) {
        PostNewsFragment fragment = new PostNewsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, userId);
        args.putBoolean(ARG_PARAM2, isAthlete);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_PARAM1);
            mIsAthlete = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = getLayoutInflater().inflate(R.layout.fragment_post_news, container, false);


        mCateSpn = v.findViewById(R.id.spnCategory);
        if (mIsAthlete) mCateSpn.setVisibility(View.GONE);
        contentET = v.findViewById(R.id.et_content);
        titleET = v.findViewById(R.id.et_title);
        TextInputLayout titleTil = v.findViewById(R.id.til_title);
        TextInputLayout contentTil = v.findViewById(R.id.til_content);
        titleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                titleTil.setError("");
            }
        });
        contentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                contentTil.setError("");
            }
        });
        Button submitBtn = v.findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(b -> {
            // MultipartBody.Part is used to send also the actual file name
            if (resultUri == null) {
                Toast.makeText(getContext(), getString(R.string.choose_a_pic), Toast.LENGTH_LONG).show();
                return;
            }
            if (contentET.getText().toString().length() < 2) {

                contentTil.setError(getString(R.string.text_is_too_short));
                return;
            }
            if (titleET.getText().toString().length() < 6) {
                titleTil.setError(getString(R.string.choose_longer_title));
                return;
            }

            File imageFile = new File(resultUri.getPath());
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(resultUri.getPath()), 128, 128);
            //create a file to write bitmap data
            File thumbFile = new File(getContext().getCacheDir(), "thumb.jpg");
            try {
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
                                MediaType.parse("mImage/jpg"),
                                thumbFile);
                MultipartBody.Part thumbBody =
                        MultipartBody.Part.createFormData("ThumbUrl", thumbFile.getName(), requestThumbFile);


                // create RequestBody instance from file
                Log.d(TAG, "onClick: mediatype:" + MimeTypeMap.getFileExtensionFromUrl(resultUri.getPath()));
                final RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse("mImage/jpg"),
                                imageFile);
                MultipartBody.Part imageBody =
                        MultipartBody.Part.createFormData("PictureUrl", imageFile.getName(), requestFile);

                final ProgressDialog waitingDialog = new ProgressDialog(getContext());
                waitingDialog.setMessage(getString(R.string.sending_content));
                waitingDialog.show();

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<RetroResult> call = apiService.sendContent("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken()
                        , mUserId
                        , mIsAthlete ? 5 : ((StringWithTag) mCateSpn.getSelectedItem()).tag
                        , RequestBody.create(MediaType.parse("text/plain"), titleET.getText().toString())

                        , RequestBody.create(MediaType.parse("text/plain"), contentET.getText().toString())
                        , imageBody
                        , thumbBody
                );
                call.enqueue(new Callback<RetroResult>() {
                    @Override
                    public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                        waitingDialog.dismiss();
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: respone:" + response.body().getResult());
                            Toast.makeText(getContext(), getString(R.string.your_content_sent_successfully), Toast.LENGTH_LONG).show();
                            container.setVisibility(View.GONE);
                        } else {
                            Log.d(TAG, "onResponse: is not ok:" + response.message());
                            Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RetroResult> call, Throwable t) {
                        waitingDialog.dismiss();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        /* Create your ArrayList collection using StringWithTag instead of String. */
        List<StringWithTag> itemList = new ArrayList<StringWithTag>();

        /* Iterate through your original collection, in this case defined with an Integer key and String value. */

        /* Build the StringWithTag List using these keys and values. */
        itemList.add(new StringWithTag(getString(R.string.fitness_news), 1));
        itemList.add(new StringWithTag(getString(R.string.diet_news), 2));



        /* Set your ArrayAdapter with the StringWithTag, and when each entry is shown in the Spinner, .toString() is called. */
        ArrayAdapter<StringWithTag> spinnerAdapter = new ArrayAdapter<StringWithTag>(getContext(), android.R.layout.simple_spinner_item, itemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCateSpn.setAdapter(spinnerAdapter);
        mImage = v.findViewById(R.id.image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setActivityTitle(getString(R.string.choose_content_pic))
                        .setAllowFlipping(false)
                        .setAllowRotation(true)
                        .setAspectRatio(1, 1)
                        .setFixAspectRatio(true)
                        .setRequestedSize(600, 600)
                        .start(getContext(), PostNewsFragment.this);
            }
        });

        ImageButton backBtn = v.findViewById(R.id.img_btn_back);
        backBtn.setOnClickListener(b->container.setVisibility(View.GONE));
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.d(TAG, "onActivityResult: " + resultUri);

                mImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private static class StringWithTag {
        public String string;
        public int tag;

        public StringWithTag(String string, Integer tag) {
            this.string = string;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return string;
        }
    }

}
