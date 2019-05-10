package com.example.arsalan.mygym.fragments;

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
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.activities.GalleryActivity;
import com.example.arsalan.mygym.adapters.AdapterCitySp;
import com.example.arsalan.mygym.adapters.AdapterProvinceSp;
import com.example.arsalan.mygym.databinding.FragmentProfileEditBinding;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.CityNState;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.example.arsalan.mygym.webservice.WebServiceResultImplementation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.example.arsalan.mygym.MyKeys.EXTRA_EDIT_MODE;
import static com.example.arsalan.mygym.MyKeys.EXTRA_GALLERY_ARRAY;
import static com.example.arsalan.mygym.MyKeys.EXTRA_POSITION;


public class EditProfileFragment extends Fragment implements WebServiceResultImplementation, Injectable {


    private static final String ARG_PARAM1 = "param1";
    private final static String TAG = "EditProfileFragment";
    private User mUser;
    private OnFragmentInteractionListener mListener;
    private SimpleDraweeView avatar;
    private Uri resultUri;
    private ProgressDialog waitingDialog;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(User user) {
        EditProfileFragment fragment = new EditProfileFragment();
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

        FragmentProfileEditBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_edit, container, false);
        bind.setUser(mUser);

        List<Integer> days = new ArrayList<>();
        for (int i = 1; i < 32; i++) days.add(i);
        bind.spDateDay.setAdapter(new MySpinnerAdapter(days));

        List<Integer> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) months.add(i);

        bind.spDateMont.setAdapter(new MySpinnerAdapter(months));

        List<Integer> years = new ArrayList<>();
        for (int i = 1397; i > 1307; i--) years.add(i);
        bind.spDateYear.setAdapter(new MySpinnerAdapter(years));


        //استانها
        bind.spProvince.setAdapter(new AdapterProvinceSp());
        if (mUser.getCityId() > 0) {
            for (int i = 0; i < bind.spProvince.getAdapter().getCount(); i++) {
                Log.d(TAG, "onItemSelected: i:" + bind.spProvince.getItemIdAtPosition(i) + " id:" + CityNState.getProvinceByCityId(mUser.getCityId()).getId());

                if (bind.spProvince.getItemIdAtPosition(i) == CityNState.getProvinceByCityId(mUser.getCityId()).getId()) {
                    bind.spProvince.setSelection(i);
                    bind.spCity.setAdapter(new AdapterCitySp(CityNState.getProvinceByCityId(mUser.getCityId()).getId()));
                    for (int j = 0; j < bind.spCity.getAdapter().getCount(); j++) {
                        Log.d(TAG, "onCreateView: j:" + bind.spCity.getItemIdAtPosition(j) + " id:" + mUser.getCityId());
                        if (bind.spCity.getItemIdAtPosition(j) == mUser.getCityId()) {
                            bind.spCity.setSelection(j);
                            break;
                        }

                    }
                    break;
                }
            }
        } else {
            bind.spProvince.setSelection(16); //فارس
            bind.spCity.setAdapter(new AdapterCitySp(17));
            for (int j = 0; j < bind.spCity.getAdapter().getCount(); j++) {
                if (bind.spCity.getItemIdAtPosition(j) == 262) { //شیراز
                    bind.spCity.setSelection(j);
                    break;
                }

            }
        }

        bind.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (((AdapterCitySp) bind.spCity.getAdapter()).getProvinceId() != l)
                    bind.spCity.setAdapter(new AdapterCitySp(l));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        Log.d(TAG, "onCreateView: province:" + CityNState.getProvinceByCityId(mUser.getCityId()).getTitle() + " City:" + CityNState.getCity(mUser.getCityId()).getTitle());

        avatar = bind.avatar;
        avatar.setImageURI(MyConst.BASE_CONTENT_URL + mUser.getThumbUrl());
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyWebService.getGalleryWeb(
                        "Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken()
                        , mUser.getId(), galleryItems -> {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), GalleryActivity.class);
                            intent.putExtra(EXTRA_POSITION, 0);
                            intent.putExtra(EXTRA_GALLERY_ARRAY, galleryItems);
                            intent.putExtra(EXTRA_EDIT_MODE, true);
                            startActivity(intent);
                        });
            }
        });
        bind.btnEditProfilePic.setOnClickListener(view -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setActivityTitle(getString(R.string.choose_profile_photo))
                .setAllowFlipping(false)
                .setAllowRotation(false)
                .setAspectRatio(3, 2)
                .setFixAspectRatio(true)
                .setRequestedSize(1200, 800)
                .start(getContext(), EditProfileFragment.this));


        bind.rbMale.setChecked(mUser.isMale());
        bind.rbFemale.setChecked(!mUser.isMale());

        bind.btnSubmit.setOnClickListener(new View.OnClickListener() {
            public MultipartBody.Part thumbBody;
            public MultipartBody.Part imageBody;

            @Override
            public void onClick(View view) {

                if (bind.etWeight.getText().toString().isEmpty()) {
                    bind.etWeight.setText("0");
                }


                RequestBody userIdReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mUser.getId()));
                RequestBody nameReq = RequestBody.create(MediaType.parse("text/plain"), bind.etName.getText().toString());
                RequestBody genderReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(bind.rbMale.isChecked()));
                RequestBody weightReq = RequestBody.create(MediaType.parse("text/plain"), bind.etWeight.getText().toString());
                RequestBody cityReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(bind.spCity.getSelectedItemId()));
                RequestBody birthDayReq = RequestBody.create(MediaType.parse("text/plain"), bind.spDateYear.getSelectedItem() + "/" + bind.spDateMont.getSelectedItem() + "/" + bind.spDateDay.getSelectedItem());

                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                requestBodyMap.put("UserId", userIdReq);
                requestBodyMap.put("name", nameReq);
                requestBodyMap.put("gender", genderReq);
                requestBodyMap.put("weight", weightReq);
                requestBodyMap.put("BirthDateFa", birthDayReq);
                requestBodyMap.put("cityId", cityReq);
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
                                , "Video Clip");
                        imageBody =
                                MultipartBody.Part.createFormData("PictureUrl", imageFile.getName(), requestFile);
                    }


                    MyWebService.editProfile(getActivity(), requestBodyMap, EditProfileFragment.this, resultUri != null ? imageBody : null, resultUri != null ? thumbBody : null);
                } catch (IOException e) {
                    e.printStackTrace();

                }


            }
        });
        return bind.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.d("EDITPROFILEACTIVITY", "onActivityResult: " + resultUri);
                avatar.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void webServiceOnSuccess(Bundle bundle) {
        if (waitingDialog != null)
            waitingDialog.dismiss();
        Log.d(TAG, "webServiceOnSuccess: usrname:" + mUser.getUserName() + " name:" + mUser.getName());
        MyWebService.getProfileDetail(mUser.getUserName(), ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), (AppCompatActivity) getActivity(), new WebServiceResultImplementation() {
            @Override
            public void webServiceOnSuccess(Bundle bundle) {
                User user = bundle.getParcelable(MyKeys.KEY_BUNDLE_OBJ);
                mListener.onSuccessfulEdited(user);
            }

            @Override
            public void webServiceOnFail() {

            }
        });

//        Intent intent=new Intent();
//        intent.setClass(getActivity(), MainActivity.class);
//        intent.putExtra("KEY", MainActivity.KEY_OMOMI);
//        getActivity().startVideoRecorderActivity(intent);
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

        void onSuccessfulEdited(User user);

    }

    private class MySpinnerAdapter implements SpinnerAdapter {
        List<Integer> nums;

        public MySpinnerAdapter(List<Integer> nums) {
            this.nums = nums;
        }

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_date_drop, viewGroup, false);
            }
            TextView textView = view.findViewById(R.id.textView);
            textView.setText(String.valueOf(nums.get(i)));
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
            return nums.size();
        }

        @Override
        public Integer getItem(int i) {
            return nums.get(i);
        }

        @Override
        public long getItemId(int i) {
            return nums.get(i);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_date, viewGroup, false);
            }
            TextView textView = view.findViewById(R.id.textView);
            textView.setText(String.valueOf(nums.get(i)));
            return view;
        }

        @Override
        public int getItemViewType(int i) {
            return 1;
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
