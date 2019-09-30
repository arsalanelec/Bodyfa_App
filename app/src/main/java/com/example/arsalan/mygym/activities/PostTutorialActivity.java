package com.example.arsalan.mygym.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.amosyuen.videorecorder.activity.FFmpegRecorderActivity;
import com.amosyuen.videorecorder.activity.params.FFmpegRecorderActivityParams;
import com.amosyuen.videorecorder.camera.CameraControllerI;
import com.amosyuen.videorecorder.recorder.common.ImageFit;
import com.amosyuen.videorecorder.recorder.common.ImageScale;
import com.amosyuen.videorecorder.recorder.common.ImageSize;
import com.amosyuen.videorecorder.recorder.params.EncoderParamsI;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.RetTutorialList;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.Tutorial;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_TUTORIAL_CAT_ID;

public class PostTutorialActivity extends AppCompatActivity {
    static final String FILE_PREFIX = "recorder-";
    static final String THUMBNAIL_FILE_EXTENSION = "jpg";
    private static final String[] VIDEO_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int REQUEST_VIDEO_PERMISSIONS_REQUEST = 1000;
    private static final int RECORD_VIDEO_REQUEST = 2000;
    private final static String TAG = "PostTutorialActivity";
    private final Context mContext;
    private FloatingActionButton fab;
    private List<Tutorial> tutorialList;
    private TextView titleTV;
    private Spinner tutorialSpn;
    private RadioButton newRB;
    private RadioButton existRB;
    private Button startCameraBtn;
    private TutorialListAdapter adapter;
    private File mVideoFile;
    private File mThumbnailFile;

    public PostTutorialActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tutorial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startCameraBtn = findViewById(R.id.btnCaptureVideo);
        startCameraBtn.setOnClickListener(view -> {
            Log.d(TAG, "onClick: turorialID:" + tutorialSpn.getSelectedItemId());
            if (newRB.isChecked() && titleTV.getText().toString().isEmpty()) {
                titleTV.setError(getString(R.string.workout_title_is_empty));
                return;
            }
            checkPermissions();
        });

        titleTV = findViewById(R.id.txtNewTitle);
        tutorialSpn = findViewById(R.id.spnCategory);
        existRB = findViewById(R.id.rbExist);
        newRB = findViewById(R.id.rbNewTitle);
        existRB.setOnCheckedChangeListener((compoundButton, b) -> {
            newRB.setChecked(!b);
            titleTV.setVisibility(b ? View.GONE : View.VISIBLE);
            tutorialSpn.setEnabled(b);

        });
        newRB.setOnCheckedChangeListener((compoundButton, b) -> {
            existRB.setChecked(!b);
            titleTV.setVisibility(b ? View.VISIBLE : View.GONE);
            tutorialSpn.setEnabled(!b);

        });
        existRB.setChecked(true);
        tutorialList = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getTutorialWeb(getIntent().getIntExtra(EXTRA_TUTORIAL_CAT_ID, 1));
    }

    public boolean hasAllPermissions() {
        for (String permission : VIDEO_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(mContext, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void checkPermissions() {
        if (hasAllPermissions()) {
            startActivity(new File(mContext.getCacheDir().toURI()), new File(mContext.getCacheDir().toURI()));
            return;
        }

        ActivityCompat.requestPermissions(PostTutorialActivity.this, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS_REQUEST);
    }

    private void startAppDetailsIntent() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(intent, REQUEST_VIDEO_PERMISSIONS_REQUEST);
    }

    private void handleRequestPermissionsResult() {
        if (hasAllPermissions()) {
            startActivity(new File(mContext.getCacheDir().toURI()), new File(mContext.getCacheDir().toURI()));
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(PostTutorialActivity.this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(PostTutorialActivity.this, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS_REQUEST);
        } else {
            new AlertDialog.Builder(mContext)
                    .setTitle("Need permissions")
                    .setMessage("You have set permissions to never show. You must enable" +
                            " them in the app details screen to record video.")
                    .setPositiveButton("App Details",
                            (dialogInterface, i) -> startAppDetailsIntent())
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_VIDEO_PERMISSIONS_REQUEST:
                handleRequestPermissionsResult();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void createTempFiles() {
        if (mVideoFile != null && mThumbnailFile != null) {
            return;
        }

        File dir = mContext.getExternalCacheDir();
        try {
            while (true) {
                int n = (int) (Math.random() * Integer.MAX_VALUE);
                String videoFileName = FILE_PREFIX + n + ".mp4";
                mVideoFile = new File(dir, videoFileName);
                if (!mVideoFile.exists() && mVideoFile.createNewFile()) {
                    String thumbnailFileName =
                            FILE_PREFIX + n + "." + THUMBNAIL_FILE_EXTENSION;
                    mThumbnailFile = new File(dir, thumbnailFileName);
                    if (!mThumbnailFile.exists() && mThumbnailFile.createNewFile()) {
                        return;
                    }
                    mVideoFile.delete();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startActivity(File videoFile, File thumbnailFile) {
        createTempFiles();
        FFmpegRecorderActivityParams.Builder paramsBuilder =
                FFmpegRecorderActivityParams.builder(mContext)

                        .setVideoOutputFileUri(mVideoFile)
                        .setVideoThumbnailOutputFileUri(mThumbnailFile);

        paramsBuilder.recorderParamsBuilder()
                .setVideoSize(new ImageSize(360, 360))
                .setVideoCodec(EncoderParamsI.VideoCodec.H264)
                .setVideoBitrate(800000)
                .setVideoFrameRate(30)
                .setVideoImageFit(ImageFit.FILL)
                .setVideoImageScale(ImageScale.ANY)
                .setShouldCropVideo(true)
                .setShouldPadVideo(false)
                .setVideoCameraFacing(CameraControllerI.Facing.BACK)
                .setAudioCodec(EncoderParamsI.AudioCodec.AAC)
                .setAudioSamplingRateHz(44100)
                .setAudioBitrate(32000)
                .setAudioChannelCount(1)
                .setOutputFormat(EncoderParamsI.OutputFormat.MP4);

        paramsBuilder.interactionParamsBuilder().setMinRecordingMillis(1000);
        paramsBuilder.interactionParamsBuilder().setMaxRecordingMillis(15000);

        Intent intent = new Intent(this, FFmpegRecorderActivity.class);
        intent.putExtra(FFmpegRecorderActivity.REQUEST_PARAMS_KEY, paramsBuilder.build());
        startActivityForResult(intent, RECORD_VIDEO_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_VIDEO_PERMISSIONS_REQUEST:
                handleRequestPermissionsResult();
                break;
            case RECORD_VIDEO_REQUEST:
                switch (resultCode) {
                    case RESULT_OK:
                        Uri videoUri = data.getData();
                       /* Uri thumbnailUri =
                                data.getParcelableExtra(FFmpegRecorderActivity.RESULT_THUMBNAIL_URI_KEY);
                        mListener.onVideoRecorded(VideoFile.create(
                                new File(videoUri.getPath()), new File(thumbnailUri.getPath())));*/
                        //mVideoFile = null;
                        //mThumbnailFile = null;
                        sendVideo();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    case FFmpegRecorderActivity.RESULT_ERROR:
                        Exception error = (Exception)
                                data.getSerializableExtra(FFmpegRecorderActivity.RESULT_ERROR_PATH_KEY);
                        new AlertDialog.Builder(mContext)
                                .setCancelable(false)
                                .setTitle(R.string.error)
                                .setMessage(error.getLocalizedMessage())
                                .setPositiveButton(R.string.ok, null)
                                .show();
                        break;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    private void sendVideo() {
        final ProgressDialog waitingDialog = new ProgressDialog(PostTutorialActivity.this);
        waitingDialog.setMessage(getString(R.string.uploading_video_wait));
        waitingDialog.setProgress(0);
        waitingDialog.setMax(100);
        waitingDialog.setIndeterminate(false);
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        waitingDialog.show();

        final RequestBody requestThumbFile =
                RequestBody.create(
                        MediaType.parse("image/jpg"),
                        mThumbnailFile);
        MultipartBody.Part thumbBody =
                MultipartBody.Part.createFormData("ThumbUrl", mThumbnailFile.getName(), requestThumbFile);

        final ProgressRequestBody requestVideoFile = new ProgressRequestBody(MediaType.parse("video/mp4"), mVideoFile, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage, String tag) {
                Log.d("Send Video", "onProgressUpdate: completed:" + percentage + "%");
                waitingDialog.setProgress(percentage);
            }

            @Override
            public void onError(String tag) {

            }

            @Override
            public void onFinish(String tag) {
                waitingDialog.setIndeterminate(true);
            }
        }
                , "Video Clip");

        Log.d(TAG, "sendVideo: groupdID:" + getIntent().getIntExtra(EXTRA_TUTORIAL_CAT_ID, 1) + "\n tutorialId:" + (int) tutorialSpn.getSelectedItemId());
        MultipartBody.Part videoBody =
                MultipartBody.Part.createFormData("PictureUrl", mVideoFile.getName(), requestVideoFile);

        MediaType plainMT = MediaType.parse("text/plain");
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("UserId", RequestBody.create(plainMT, String.valueOf(((MyApplication) getApplication()).getCurrentUser().getId())));
        requestBodyMap.put("tutorialCategoryId", RequestBody.create(plainMT, String.valueOf(getIntent().getIntExtra(EXTRA_TUTORIAL_CAT_ID, 1))));
        if (!newRB.isChecked()) {
            requestBodyMap.put("TutorialSubCategoryId", RequestBody.create(plainMT, String.valueOf((int) tutorialSpn.getSelectedItemId())));
        } else {
            requestBodyMap.put("Title", RequestBody.create(plainMT, titleTV.getText().toString()));
        }
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.sendTutorialVideo(
                "Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken()
                , requestBodyMap
                , videoBody
                , thumbBody);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                Log.d("Send Video", "onResponse: " + response.message());
                if (response.isSuccessful()) {
                    new AlertDialog.Builder(mContext)
                            .setMessage(getString(R.string.video_sent_successfully))
                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                dialog.dismiss();
                                finish();
                            })
                            .show();

                   /* Snackbar.make(fab, "ویدیوی شما با موفقیت ارسال شد و بعد از تایید به نمایش در خواهد آمد", Snackbar.LENGTH_LONG)
                            .setAction("باشه", null).show();*/
                } else {
                    Toast.makeText(mContext, R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                waitingDialog.dismiss();
                Log.d("Send Video", "onFailure: " + t.getMessage());
                Toast.makeText(mContext, R.string.error_accord_try_again, Toast.LENGTH_LONG).show();

            }
        });

    }

    void getTutorialWeb(int catId) {
        final String TAG = "getTutorialWeb";
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(mContext);
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetTutorialList> call = apiService.getTutorialList("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), catId);
        call.enqueue(new Callback<RetTutorialList>() {
            @Override
            public void onResponse(Call<RetTutorialList> call, Response<RetTutorialList> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful() && response.body().getRecords().size() > 0) {
                    tutorialList.addAll(response.body().getRecords());
                    adapter = new TutorialListAdapter(tutorialList);
                    tutorialSpn.setAdapter(adapter);
                    Log.d(TAG, "onResponse: recordCnt:" + response.body().getRecordsCount());
                } else {
                    Log.d(TAG, "onResponse: message:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<RetTutorialList> call, Throwable t) {
                waitingDialog.dismiss();
                Log.d(TAG, "onFailure: message:" + t.getMessage());
            }
        });
    }

    class TutorialListAdapter implements SpinnerAdapter {
        final List<Tutorial> tutorialList;

        public TutorialListAdapter(List<Tutorial> tutorialList) {
            this.tutorialList = tutorialList;
        }

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = new TextView(mContext);
                Typeface typeface = ResourcesCompat.getFont(mContext, R.font.iran_sans_mobile);

                ((TextView) view).setTypeface(typeface);
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                ((TextView) view).setTextColor(Color.BLACK);
            }
            ((TextView) view).setText(tutorialList.get(i).getTitle());

            return view;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            if (tutorialList != null)
                return tutorialList.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return tutorialList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return tutorialList.get(i).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = new TextView(mContext);
                Typeface typeface = ResourcesCompat.getFont(mContext, R.font.iran_sans_mobile);

                ((TextView) view).setTypeface(typeface);
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                ((TextView) view).setTextColor(Color.BLACK);
            }
            ((TextView) view).setText(tutorialList.get(i).getTitle());
            return view;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

}
