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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.activities.EditProfileActivity;
import com.example.arsalan.mygym.adapters.GalleryPagerAdapter;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.GalleryViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.example.arsalan.mygym.webservice.WebServiceResultImplementation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_USER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_TRAINER_ID;


public class DashBoardAthleteFragment extends Fragment implements WebServiceResultImplementation, Injectable {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ATHLETE = "param1";
    @Inject
    MyViewModelFactory factory;
    private List<GalleryItem> mGalleryItemList;
    private GalleryPagerAdapter mGalleryAdapter;
    private ViewPager mGalleryPager;
    private TextView nameTV;
    private SimpleDraweeView pictureImg;
    private OnFragmentInteractionListener mListener;
    private final int REQ_SELECT_TRAINER = 1000;
    private User mCurrentAthlete;
    private Uri resultUri;
    private ProgressDialog waitingDialog;
    private GalleryViewModel viewModel;


    public DashBoardAthleteFragment() {
        // Required empty public constructor
    }

    public static DashBoardAthleteFragment newInstance(User athlete) {
        DashBoardAthleteFragment fragment = new DashBoardAthleteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ATHLETE, athlete);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentAthlete = getArguments().getParcelable(ARG_ATHLETE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dash_board_athlete, container, false);
        nameTV = v.findViewById(R.id.txtName);
        nameTV.setText(((MyApplication) getActivity().getApplication()).getCurrentUser().getName());

        LinearLayout trainderSelectBtn = v.findViewById(R.id.llSelectTrainer);
        /*trainderSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainerListDialog dialog = new TrainerListDialog();
                dialog.setTargetFragment(DashBoardAthleteFragment.this, REQ_SELECT_TRAINER);
                dialog.show(getFragmentManager(), "");
            }
        });*/

        Button editProfileBtn = v.findViewById(R.id.btnEditProfilePic);
        editProfileBtn.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(getActivity(), EditProfileActivity.class);
            i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, MyKeys.KEY_ROLE_ATHLETE);
            i.putExtra(EXTRA_OBJ_USER, mCurrentAthlete);
            startActivity(i);
        });

        mGalleryPager = v.findViewById(R.id.vp_gallery);
        TabLayout tabLayout = v.findViewById(R.id.tablayoutGallery);
        mGalleryPager.setOnTouchListener((v1, motionEvent) -> {
            Log.d("setOnTouchListener", "onTouch: " + motionEvent.toString());
            if (
                    motionEvent.getAction() == MotionEvent.ACTION_DOWN &&
                            v1 instanceof ViewPager
            ) {
                ((ViewPager) v1).requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        tabLayout.setupWithViewPager(mGalleryPager);

        mGalleryItemList = new ArrayList<>();
        mGalleryAdapter = new GalleryPagerAdapter(mGalleryItemList);
        mGalleryPager.setAdapter(mGalleryAdapter);

        Button captureImageBtn = v.findViewById(R.id.btnCapture);
        captureImageBtn.setOnClickListener(view -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setActivityTitle(getString(R.string.choose_profile_photo))
                .setAllowFlipping(false)
                .setAllowRotation(false)
                .setAspectRatio(3, 2)
                .setFixAspectRatio(true)
                .setRequestedSize(1200, 800)
                .start(getContext(), DashBoardAthleteFragment.this));
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(GalleryViewModel.class);
        viewModel.init( mCurrentAthlete.getId());
        viewModel.getGalleryItemList().observe(this, galleryItems -> {

            Log.d("onActivityCreated", "observe: ");
            mGalleryItemList.clear();
            mGalleryItemList.addAll(galleryItems);
            mGalleryAdapter.notifyDataSetChanged();
            // waitingFL.setVisibility(View.GONE);
        });

        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
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
    public void webServiceOnSuccess(Bundle bundle) {
        if (waitingDialog != null && waitingDialog.isShowing()) waitingDialog.dismiss();
        viewModel.init( mCurrentAthlete.getId());

    }

    @Override
    public void webServiceOnFail() {
        if (waitingDialog != null && waitingDialog.isShowing()) waitingDialog.dismiss();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_SELECT_TRAINER) {
            if (resultCode == MyKeys.RESULT_OK) {
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                final ProgressDialog waitingDialog = new ProgressDialog(getContext());
                waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
                waitingDialog.setCancelable(false);
                waitingDialog.show();
                final long trainerId = data.getLongExtra(EXTRA_TRAINER_ID, 0);
                Call<RetroResult> call = apiService.selectMyTrainer("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentAthlete.getId(), trainerId);
                call.enqueue(new Callback<RetroResult>() {
                    @Override
                    public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                        waitingDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.done_successfully), Toast.LENGTH_SHORT).show();
                        mListener.changeUserTrainer(trainerId);
                    }

                    @Override
                    public void onFailure(Call<RetroResult> call, Throwable t) {
                        waitingDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.error_accord_try_again), Toast.LENGTH_LONG).show();

                    }
                });
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                addToGalleryWeb(mCurrentAthlete.getId());
                Log.d("EDITPROFILEACTIVITY", "onActivityResult: " + resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void addToGalleryWeb(long userId) {
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
                MultipartBody.Part thumbBody = MultipartBody.Part.createFormData("ThumbUrl", thumbFile.getName(), requestThumbFile);


                // create RequestBody instance from file
                Log.d(getClass().getSimpleName(), "onClick: mediatype:" + MimeTypeMap.getFileExtensionFromUrl(resultUri.getPath()));


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
                        , "the picture");
                MultipartBody.Part imageBody = MultipartBody.Part.createFormData("PictureUrl", imageFile.getName(), requestFile);
                MyWebService.addToGalleryWeb(getActivity(), userId, imageBody, thumbBody, DashBoardAthleteFragment.this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void changeUserTrainer(long trainerId);
    }
    private class galleryAdapter extends FragmentStatePagerAdapter{
        final List<GalleryItem> galleryItemList;

        public galleryAdapter(@NonNull FragmentManager fm,List<GalleryItem> galleryItems) {
            super(fm);
            this.galleryItemList=galleryItems;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return GalleryPageFragment.newInstance(galleryItemList.get(position).getPictureUrl(),"");
        }

        @Override
        public int getCount() {
            return galleryItemList.size();
        }
    }


}

