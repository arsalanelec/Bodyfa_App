package com.example.arsalan.mygym.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddMedalDialog extends DialogFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private static final String TAG = "AddMedalDialog";

    private OnFragmentInteractionListener mListener;
    private ProgressDialog waitingDialog;
    private Uri resultUri;
    private ImageView imgThumb;
    private User mUser;

    public AddMedalDialog() {
        // Required empty public constructor
    }

    public static AddMedalDialog newInstance(User user) {
        AddMedalDialog fragment = new AddMedalDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_medal_dialog, container, false);

        Spinner categorySpn = v.findViewById(R.id.spnMedalCat);
        categorySpn.setAdapter(new AdapterMedalCategory());

        EditText titleET = v.findViewById(R.id.etTitle);
        Button submitBtn = v.findViewById(R.id.btnSubmit);

        submitBtn.setOnClickListener(new View.OnClickListener() {

            public MultipartBody.Part thumbBody;
            public MultipartBody.Part imageBody;

            @Override
            public void onClick(View view) {

                if (titleET.getText().toString().isEmpty()) {
                    titleET.setError(getString(R.string.title_is_empty));
                    return;
                }

                Log.d(TAG, "onClick: userId:" + mUser.getId() + " title:" + titleET.getText().toString() + " cat:" + categorySpn.getSelectedItemId());
                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                requestBodyMap.put("UserId", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mUser.getId())));
                requestBodyMap.put("Title", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(titleET.getText().toString())));
                requestBodyMap.put("HonorCategory", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(categorySpn.getSelectedItemId())));
                requestBodyMap.put("GetDateFa", RequestBody.create(MediaType.parse("text/plain"), "1400/01/01"));

                try {
                    if (resultUri != null && !resultUri.getPath().isEmpty()) {
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
                        waitingDialog.setCancelable(false);
                        waitingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        waitingDialog.show();
                        final ProgressRequestBody requestFile = new ProgressRequestBody(MediaType.parse("image/jpg"), imageFile, new ProgressRequestBody.UploadCallbacks() {
                            @Override
                            public void onProgressUpdate(int percentage, String tag) {
                                Log.d("Send Video", "onProgressUpdate: completed:" + percentage + "%");
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
                                , "picture");
                        imageBody =
                                MultipartBody.Part.createFormData("PictureUrl", imageFile.getName(), requestFile);
                    }else { //image uri is null or empty
                        Toast.makeText(getContext(), R.string.medal_photo_is_empty, Toast.LENGTH_LONG).show();
                        return;
                    }
                    addHonorWeb(requestBodyMap, imageBody, thumbBody);

                } catch (IOException e) {
                    e.printStackTrace();

                }


            }


        });
        imgThumb = v.findViewById(R.id.imgThumb);
        imgThumb.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setActivityTitle(getString(R.string.select_medal_pic))
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setAspectRatio(3, 2)
                        .setFixAspectRatio(true)
                        .setRequestedSize(1200, 800)
                        .start(getContext(), AddMedalDialog.this);


            }
        });
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.d(TAG, "onActivityResult: " + resultUri);
                imgThumb.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG,"onActivityResult: error:"+error.getMessage());
            }
        }
    }

    private void addHonorWeb(Map<String, RequestBody> requestBodyMap, MultipartBody.Part image, MultipartBody.Part thumb) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.addEditHonor("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), requestBodyMap, image, thumb);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                if(waitingDialog!=null && waitingDialog.isShowing()) {
                    waitingDialog.dismiss();
                }
                Log.d(TAG, "onResponse: "+response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), getString(R.string.medal_sent_successfully), Toast.LENGTH_LONG).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_accord_try_again), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                if(waitingDialog!=null && waitingDialog.isShowing()) {
                    waitingDialog.dismiss();
                }
                Toast.makeText(getContext(), getString(R.string.error_accord_try_again), Toast.LENGTH_LONG).show();

            }
        });

    }

    private class AdapterMedalCategory implements SpinnerAdapter {

        String[] titles = {getString(R.string.world_first_place)
                , getString(R.string.world_second_place)
                , getString(R.string.world_third_place)
                , getString(R.string.national_first_place)
                , getString(R.string.national_second_place)
                , getString(R.string.national_third_place)
                , getString(R.string.province_first_place)
                , getString(R.string.province_second_place)
                , getString(R.string.province_third_place)};

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner_date, viewGroup, false);
            }
            TextView textView = view.findViewById(R.id.textView);

            textView.setText(getItem(i));
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
            return titles.length;
        }

        @Override
        public String getItem(int i) {
            return titles[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner_date, viewGroup, false);
            }
            TextView textView = view.findViewById(R.id.textView);

            textView.setText(getItem(i));
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
