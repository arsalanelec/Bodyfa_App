package com.example.arsalan.mygym.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterDashboardNews;
import com.example.arsalan.mygym.adapters.GalleryPagerAdapter;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.NewsHead;
import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.viewModels.GalleryViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.NewsListViewModel;
import com.example.arsalan.mygym.viewModels.UserViewModel;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.example.arsalan.mygym.webservice.WebServiceResultImplementation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;
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

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


public class DashBoardProfileFragment extends Fragment implements WebServiceResultImplementation, Injectable {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_ID = "param1";
    private static final String ARG_USER_NAME = "param2";
    private static final String ARG_USER_EDITABLE = "user can edit";
    @Inject
    MyViewModelFactory factory;
    private List<NewsHead> newsList;
    private AdapterDashboardNews adapter;
    private User mUser;
    private TextView mNameTV;
    private SimpleDraweeView pictureImg;
    private Uri resultUri;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog waitingDialog;
    private ArrayList<GalleryItem> mGalleryItemList;
    private GalleryPagerAdapter mGalleryAdapter;
    private ViewPager mGalleryPager;
    private GalleryViewModel galleryViewModel;
    private long mUserId;
    private RecyclerView newsRV;
    private TextInputEditText mNameEt;
    private UserViewModel mUserViewModel;
    private View detailContainer;
    private TextView noNewsTv;
    private View mPostNews;

    public DashBoardProfileFragment() {
        // Required empty public constructor
    }

    public static DashBoardProfileFragment newInstance(long userId) {
        DashBoardProfileFragment fragment = new DashBoardProfileFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dash_board_profile, container, false);
        mNameTV = v.findViewById(R.id.txtName);
        mNameEt = v.findViewById(R.id.et_name);
        TextInputLayout nameTil = v.findViewById(R.id.til_name);
        nameTil.setVisibility(View.GONE);
        ImageButton editBtn = v.findViewById(R.id.btn_edit_name);
        ImageButton saveBtn = v.findViewById(R.id.btn_save_name);
        saveBtn.setVisibility(View.GONE);
        editBtn.setOnClickListener(b -> {
            nameTil.setVisibility(View.VISIBLE);
            mNameTV.setVisibility(View.GONE);
            saveBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.GONE);
            mNameEt.requestFocus();
        });

        saveBtn.setOnClickListener(b -> {
            if (mNameEt.getText().toString().isEmpty()) {
                nameTil.setError(getString(R.string.text_is_too_short));
                return;
            }
            mNameTV.setText(mNameEt.getText());
            nameTil.setVisibility(View.GONE);
            mNameTV.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
            if (!mNameEt.getText().toString().equals(mUser.getName())) {
                mUser.setName(mNameEt.getText().toString());
                mUserViewModel.save(mUser, null, null);
            }
        });
        mNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nameTil.setError("");
            }
        });

        //       pictureImg = v.findViewById(R.id.image);
//        pictureImg.setImageURI(MyConst.BASE_API_URL+ (((MyApplication2) getActivity().getApplication()).getCurrentUser().getPictureUrl()));

         mPostNews = v.findViewById(R.id.btnPostNews);

        mPostNews.setOnClickListener(view -> {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_dashboard_news, PostNewsFragment.newInstance(mUserId, mUser.getRoleName().equalsIgnoreCase("athlete")))
            .commit();
            detailContainer.setVisibility(View.VISIBLE);
            /*Intent i = new Intent();
            i.putExtra(EXTRA_USER_ID, mUserId);
            i.putExtra(EXTRA_IS_ATHLETE, mUser.getRoleName().equalsIgnoreCase("athlete"));
            i.setClass(getActivity(), PostContentActivity.class);
            startActivity(i);*/
        });

        newsRV = v.findViewById(R.id.rv_news);
        newsList = new ArrayList<>();

        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        newsRV.setLayoutManager(linearLayout);
        newsRV.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        adapter = new AdapterDashboardNews( newsList,AdapterDashboardNews.LAYOUT_TYPE_LINEAR, (newsId, catType) -> {
            /*Intent i = new Intent();
            i.setClass(getContext(), NewsDetailActivity.class);
            i.putExtra(NewsDetailActivity.KEY_NEWS_ID, newsId);
            startActivity(i);*/
            getFragmentManager().beginTransaction().replace(R.id.container_dashboard_news, NewsDetailFragment.newInstance(mUserId, newsId, catType), "").commit();
            detailContainer.setVisibility(View.VISIBLE);

        });

        newsRV.setAdapter(adapter);

        ImageButton linearLayoutBtn = v.findViewById(R.id.btn_layout_linear);
        linearLayoutBtn.setOnClickListener(b->{
            newsRV.setLayoutManager(linearLayout);
            adapter.setLayoutType(AdapterDashboardNews.LAYOUT_TYPE_LINEAR);
            adapter.notifyDataSetChanged();
        });

        ImageButton gridLayoutBtn = v.findViewById(R.id.btn_layout_grid);
        gridLayoutBtn.setOnClickListener(b->{
            newsRV.setLayoutManager(gridLayoutManager);
            adapter.setLayoutType(AdapterDashboardNews.LAYOUT_TYPE_GRID);
            adapter.notifyDataSetChanged();
        });

        mGalleryPager = v.findViewById(R.id.vp_gallery);
        TabLayout tabLayout = v.findViewById(R.id.tablayout_trainer_dashboard);
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
                .start(getContext(), DashBoardProfileFragment.this));

        detailContainer = v.findViewById(R.id.container_dashboard_news);
        noNewsTv = v.findViewById(R.id.txt_no_news);
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NewsListViewModel newsListViewModel = ViewModelProviders.of(this, factory).get(NewsListViewModel.class);
        newsListViewModel.getNewsList().observe(this, newNewsList -> {
            if (newNewsList != null) {
                Log.d("NewsListViewModel", "observe: cnt:" + newNewsList.size());
                newsList.clear();
                newsList.addAll(newNewsList);
                adapter.notifyDataSetChanged();
                noNewsTv.setVisibility((newNewsList.size() == 0) ? View.VISIBLE : View.GONE);
                newsRV.setVisibility((newNewsList.size() != 0) ? View.VISIBLE : View.GONE);
            }
        });

        mUserViewModel = ViewModelProviders.of(getActivity(), factory).get(UserViewModel.class);
        //mUserViewModel.init(mUserName);
        mUserViewModel.getUserLive().observe(DashBoardProfileFragment.this, user -> {
            mUser = user;
            mPostNews.setVisibility(user.isConfirmed()?View.VISIBLE:View.INVISIBLE);
            mNameTV.setText(user.getName());
            mNameEt.setText(user.getName());
            // MyWebService.getNewsWeb(mUserId, user.getRoleName().equalsIgnoreCase("athlete") ? 5 : 2, newsList, adapter, getContext(), newsRV, DashBoardProfileFragment.this);
            newsListViewModel.init(user.getId(),0);

        });


        galleryViewModel = ViewModelProviders.of(this, factory).get(GalleryViewModel.class);
        galleryViewModel.init(mUserId);
        galleryViewModel.getGalleryItemList().observe(DashBoardProfileFragment.this, galleryItems -> {
            Log.d("onActivityCreated", "observe: ");
            mGalleryItemList.clear();
            mGalleryItemList.addAll(galleryItems);
            mGalleryAdapter.notifyDataSetChanged();
            // waitingFL.setVisibility(View.GONE);
        });

        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
    }

    @Override
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && detailContainer.getVisibility() == View.VISIBLE) {
                detailContainer.setVisibility(View.GONE);
                //your code

                return true;
            }
            return false;
        });
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
    public void webServiceOnSuccess(Bundle bundle) {
        if (waitingDialog != null && waitingDialog.isShowing()) waitingDialog.dismiss();

        /*MyWebService.getGalleryWeb(
                "Bearer "+((MyApplication2)getActivity().getApplication()).getCurrentToken().getToken()
                , mUser.getId(), new MyWebService.OnGalleryLoadListener() {
            @Override
            public void onGalleryLoad(ArrayList<GalleryItem> galleryItems) {
                mGalleryItemList.removeAll(mGalleryItemList);
                mGalleryItemList.addAll(galleryItems);
                mGalleryAdapter.notifyDataSetChanged();
               // mGalleryAdapter.notifyDataSetChanged();
                mGalleryPager.setCurrentItem(mGalleryAdapter.getCount()-1);
            }
        });*/
    }

    @Override
    public void webServiceOnFail() {
        if (waitingDialog != null && waitingDialog.isShowing()) waitingDialog.dismiss();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                addToGalleryWeb(mUserId);

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
                MyWebService.addToGalleryWeb(getActivity(), userId, imageBody, thumbBody, DashBoardProfileFragment.this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
