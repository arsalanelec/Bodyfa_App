package com.example.arsalan.mygym.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import static com.example.arsalan.mygym.MyKeys.EXTRA_IS_ATHLETE;
import static com.example.arsalan.mygym.MyKeys.EXTRA_USER_ID;

public class PostContentActivity extends AppCompatActivity {

    private final String TAG = "PostContentActivity";
    private Uri resultUri;
    private SimpleDraweeView image;
    private Spinner cateSpn;
    private EditText contentET;
    private final Context mContext;
    private EditText titleET;
    private long mUserId;
    private boolean mIsAthlete;

    public PostContentActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().getExtras() == null)
            throw new RuntimeException(this.toString() + " should hav a user id in the intent!");
        mUserId = getIntent().getLongExtra(EXTRA_USER_ID, 0);
        mIsAthlete = getIntent().getBooleanExtra(EXTRA_IS_ATHLETE, true);

        cateSpn = (Spinner) findViewById(R.id.spnCategory);
        if(mIsAthlete) cateSpn.setVisibility(View.GONE);
        contentET = findViewById(R.id.etContent);
        titleET = findViewById(R.id.etTitle);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // MultipartBody.Part is used to send also the actual file name
            if (resultUri == null) {
                Toast.makeText(mContext, getString(R.string.choose_a_pic), Toast.LENGTH_LONG).show();
                return;
            }
            if (contentET.getText().toString().length() < 2) {

                contentET.setError(getString(R.string.text_is_too_short));
                return;
            }
            if (titleET.getText().toString().length() < 6) {
                titleET.setError(getString(R.string.choose_longer_title));
                return;
            }
            File imageFile = new File(resultUri.getPath());
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(resultUri.getPath()), 128, 128);
            //create a file to write bitmap data
            File thumbFile = new File(mContext.getCacheDir(), "thumb.jpg");
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
                                MediaType.parse("image/jpg"),
                                thumbFile);
                MultipartBody.Part thumbBody =
                        MultipartBody.Part.createFormData("ThumbUrl", thumbFile.getName(), requestThumbFile);


                // create RequestBody instance from file
                Log.d(TAG, "onClick: mediatype:" + MimeTypeMap.getFileExtensionFromUrl(resultUri.getPath()));
                final RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse("image/jpg"),
                                imageFile);
                MultipartBody.Part imageBody =
                        MultipartBody.Part.createFormData("PictureUrl", imageFile.getName(), requestFile);

                final ProgressDialog waitingDialog = new ProgressDialog(PostContentActivity.this);
                waitingDialog.setMessage(getString(R.string.sending_content));
                waitingDialog.show();

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<RetroResult> call = apiService.sendContent("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken()
                        , mUserId
                        , mIsAthlete?5:((StringWithTag) cateSpn.getSelectedItem()).tag
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
                            Toast.makeText(mContext, getString(R.string.your_content_sent_successfully), Toast.LENGTH_LONG).show();
                            PostContentActivity.super.onBackPressed();
                        } else {
                            Log.d(TAG, "onResponse: is not ok:" + response.message());
                            Toast.makeText(mContext, R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RetroResult> call, Throwable t) {
                        waitingDialog.dismiss();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(mContext, R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Create your ArrayList collection using StringWithTag instead of String. */
        List<StringWithTag> itemList = new ArrayList<>();

        /* Iterate through your original collection, in this case defined with an Integer key and String value. */


        /* Build the StringWithTag List using these keys and values. */
        itemList.add(new StringWithTag(getString(R.string.fitness_news), 1));
        itemList.add(new StringWithTag(getString(R.string.diet_news), 2));



        /* Set your ArrayAdapter with the StringWithTag, and when each entry is shown in the Spinner, .toString() is called. */
        ArrayAdapter<StringWithTag> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cateSpn.setAdapter(spinnerAdapter);
        image = findViewById(R.id.image);
        image.setOnClickListener(view -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setActivityTitle(getString(R.string.choose_content_pic))
                .setAllowFlipping(false)
                .setAllowRotation(true)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .setRequestedSize(600, 600)
                .start(PostContentActivity.this));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.d("EDITPROFILEACTIVITY", "onActivityResult: " + resultUri);

                image.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private static class StringWithTag {
        public final String string;
        public final int tag;

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
