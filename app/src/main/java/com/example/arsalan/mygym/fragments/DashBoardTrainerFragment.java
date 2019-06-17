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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.activities.NewsDetailActivity;
import com.example.arsalan.mygym.activities.PostContentActivity;
import com.example.arsalan.mygym.adapters.AdapterNews;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.NewsHead;
import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.User;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


public class DashBoardTrainerFragment extends Fragment implements WebServiceResultImplementation, Injectable {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER = "param1";
    @Inject
    MyViewModelFactory factory;
    private List<NewsHead> newsList;
    private AdapterNews adapter;
    private User mUser;
    private TextView nameTV;
    private SimpleDraweeView pictureImg;
    private Uri resultUri;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog waitingDialog;
    private ArrayList<GalleryItem> mGalleryItemList;
    private GalleryPagerAdapter mGalleryAdapter;
    private ViewPager mGalleryPager;
    private GalleryViewModel viewModel;

    public DashBoardTrainerFragment() {
        // Required empty public constructor
    }

    public static DashBoardTrainerFragment newInstance(User user) {
        DashBoardTrainerFragment fragment = new DashBoardTrainerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dash_board_trainer, container, false);
        nameTV = v.findViewById(R.id.txtName);
        nameTV.setText(((MyApplication) getActivity().getApplication()).getCurrentUser().getName());

        //       pictureImg = v.findViewById(R.id.image);
//        pictureImg.setImageURI(MyConst.BASE_API_URL+ (((MyApplication2) getActivity().getApplication()).getCurrentUser().getPictureUrl()));

        Button postNews = v.findViewById(R.id.btnPostNews);
        postNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getActivity(), PostContentActivity.class);
                startActivity(i);
            }
        });

        RecyclerView newsRV = v.findViewById(R.id.rvNews);
        newsList = new ArrayList<>();
        adapter = new AdapterNews(getActivity(), newsList,new AdapterNews.OnAdapterNewsEventListener() {
            @Override
            public void onNewsHeadClick(long newsId) {
                Intent i = new Intent();
                i.setClass(getContext(), NewsDetailActivity.class);
                i.putExtra(NewsDetailActivity.KEY_NEWS_ID,newsId);
                startActivity(i);
            }
        });

        newsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsRV.setAdapter(adapter);
        MyWebService.getNewsWeb(((MyApplication) getActivity().getApplication()).getCurrentTrainer().getId(), 2, newsList, adapter, getContext(), newsRV, DashBoardTrainerFragment.this);

        mGalleryPager = v.findViewById(R.id.vpGallery);
        TabLayout tabLayout = v.findViewById(R.id.tablayout);
        mGalleryPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                Log.d("setOnTouchListener", "onTouch: " + motionEvent.toString());
                if (
                        motionEvent.getAction() == MotionEvent.ACTION_DOWN &&
                                v instanceof ViewPager
                ) {
                    ((ViewPager) v).requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        tabLayout.setupWithViewPager(mGalleryPager);
        mGalleryItemList = new ArrayList<GalleryItem>();
        mGalleryAdapter = new GalleryPagerAdapter(mGalleryItemList);
        mGalleryPager.setAdapter(mGalleryAdapter);
        /*MyWebService.getGalleryWeb(
                "Bearer "+((MyApplication2)getActivity().getApplication()).getCurrentToken().getToken()
                , mUser.getId(), new MyWebService.OnGalleryLoadListener() {
            @Override
            public void onGalleryLoad(ArrayList<GalleryItem> galleryItems) {
                mGalleryItemList.removeAll(mGalleryItemList);
                mGalleryItemList.addAll(galleryItems);
                mGalleryAdapter.notifyDataSetChanged();

            }
        });*/
        Button captureImageBtn = v.findViewById(R.id.btnCapture);
        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setActivityTitle(getString(R.string.choose_profile_photo))
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setAspectRatio(3, 2)
                        .setFixAspectRatio(true)
                        .setRequestedSize(1200, 800)
                        .start(getContext(), DashBoardTrainerFragment.this);
            }
        });
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(GalleryViewModel.class);
        viewModel.init( mUser.getId());
        viewModel.getGalleryItemList().observe(this, galleryItems -> {
            Log.d("onActivityCreated", "observe: ");
            mGalleryItemList.removeAll(mGalleryItemList);
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
        if (getActivity() != null)
            viewModel.init(mUser.getId());

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
                addToGalleryWeb(mUser.getId());

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
                MyWebService.addToGalleryWeb(getActivity(), userId, imageBody, thumbBody, DashBoardTrainerFragment.this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class GalleryPagerAdapter extends PagerAdapter {
        ArrayList<GalleryItem> galleryItemList;

        public GalleryPagerAdapter(ArrayList<GalleryItem> galleryItemList) {
            this.galleryItemList = galleryItemList;
        }

        @Override
        public int getCount() {
            return galleryItemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView image = new ImageView(container.getContext());
            CircularProgressDrawable loadingDrawable = new CircularProgressDrawable(container.getContext());
            loadingDrawable.setStrokeWidth(5f);
            loadingDrawable.setCenterRadius(30f);

            Glide.with(getContext())
                    .load(MyConst.BASE_CONTENT_URL + galleryItemList.get((getCount() - 1) - position).getPictureUrl())
                    .apply(new RequestOptions().placeholder(loadingDrawable).centerCrop())
                    .into(image);

            container.addView(image);
/*            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), GalleryActivity.class);
                    intent.putExtra(EXTRA_POSITION, position);
                    intent.putExtra(EXTRA_GALLERY_ARRAY, galleryItemList);
                    intent.putExtra(EXTRA_EDIT_MODE, true);
                    startVideoRecorderActivity(intent);


                }
            });*/
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
