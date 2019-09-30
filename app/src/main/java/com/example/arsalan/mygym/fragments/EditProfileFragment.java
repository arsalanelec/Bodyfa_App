package com.example.arsalan.mygym.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

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
import com.example.arsalan.mygym.models.RetStatusProgress;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.UserViewModel;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.example.arsalan.mygym.webservice.WebServiceResultImplementation;
import com.example.arsalan.room.UserDao;
import com.facebook.drawee.view.SimpleDraweeView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.example.arsalan.mygym.MyKeys.EXTRA_EDIT_MODE;
import static com.example.arsalan.mygym.MyKeys.EXTRA_GALLERY_ARRAY;
import static com.example.arsalan.mygym.MyKeys.EXTRA_POSITION;


public class EditProfileFragment extends Fragment implements WebServiceResultImplementation, Injectable {


    private static final String ARG_PARAM1 = "param1";
    private final static String TAG = "EditProfileFragment";
    @Inject
    UserDao mUserDao;

    @Inject
    MyViewModelFactory mFactory;

    private User mUser;
    private OnFragmentInteractionListener mListener;
    private SimpleDraweeView avatar;
    private Uri resultUri;
    private ProgressDialog waitingDialog;
    private FragmentProfileEditBinding mBind;
    private UserViewModel userViewModel;

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

        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_edit, container, false);

        Log.d(TAG, "onCreateView: birthday is:" + mUser.getBirthdayDateFa());

        List<Integer> days = new ArrayList<>();
        for (int i = 1; i < 32; i++) days.add(i);
        mBind.spDateDay.setAdapter(new MySpinnerAdapter(days));
        List<Integer> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) months.add(i);

        mBind.spDateMont.setAdapter(new MySpinnerAdapter(months));

        List<Integer> years = new ArrayList<>();
        for (int i = 1397; i > 1300; i--) years.add(i);
        mBind.spDateYear.setAdapter(new MySpinnerAdapter(years));

        if (mUser.getBirthdayDateFa() != null && !mUser.getBirthdayDateFa().isEmpty()) {
            String[] strArr = mUser.getBirthdayDateFa().split("/");
            try {
                Log.d(TAG, "onCreateView: mathch" + Arrays.toString(strArr));

                int birthDay = Integer.parseInt(strArr[2]);
                int birthMonth = Integer.parseInt(strArr[1]);
                int birthYear = Integer.parseInt(strArr[0]);
                mBind.spDateDay.setSelection(birthDay - 1);
                mBind.spDateMont.setSelection(birthMonth - 1);
                for (int i = 0; i < mBind.spDateYear.getCount(); i++) {
                    if ((int) mBind.spDateYear.getItemAtPosition(i) == birthYear) {
                        mBind.spDateYear.setSelection(i);
                        break;
                    }
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
//استانها
        mBind.spProvince.setAdapter(new AdapterProvinceSp());
        if (mUser.getCityId() > 0) {
            for (int i = 0; i < mBind.spProvince.getAdapter().getCount(); i++) {
                if (mBind.spProvince.getItemIdAtPosition(i) == CityNState.getProvinceByCityId(mUser.getCityId()).getId()) {
                    mBind.spProvince.setSelection(i);
                    mBind.spCity.setAdapter(new AdapterCitySp(CityNState.getProvinceByCityId(mUser.getCityId()).getId()));
                    for (int j = 0; j < mBind.spCity.getAdapter().getCount(); j++) {
                        if (mBind.spCity.getItemIdAtPosition(j) == mUser.getCityId()) {
                            mBind.spCity.setSelection(j);
                            break;
                        }
                    }
                    break;
                }
            }
        } else {
            mBind.spProvince.setSelection(16); //فارس
            mBind.spCity.setAdapter(new AdapterCitySp(17));
            for (int j = 0; j < mBind.spCity.getAdapter().getCount(); j++) {
                if (mBind.spCity.getItemIdAtPosition(j) == 262) { //شیراز
                    mBind.spCity.setSelection(j);
                    break;
                }

            }
        }

        mBind.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (((AdapterCitySp) mBind.spCity.getAdapter()).getProvinceId() != l)
                    mBind.spCity.setAdapter(new AdapterCitySp(l));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        Log.d(TAG, "onCreateView: province:" + CityNState.getProvinceByCityId(mUser.getCityId()).getTitle() + " City:" + CityNState.getCity(mUser.getCityId()).getTitle());

        avatar = mBind.avatar;
        avatar.setImageURI(MyConst.BASE_CONTENT_URL + mUser.getThumbUrl());
        avatar.setOnClickListener(view -> MyWebService.getGalleryWeb(
                "Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken()
                , mUser.getId(), galleryItems -> {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), GalleryActivity.class);
                    intent.putExtra(EXTRA_POSITION, 0);
                    intent.putExtra(EXTRA_GALLERY_ARRAY, galleryItems);
                    intent.putExtra(EXTRA_EDIT_MODE, true);
                    startActivity(intent);
                }));
        mBind.btnEditProfilePic.setOnClickListener(view -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setActivityTitle(getString(R.string.choose_profile_photo))
                .setAllowFlipping(false)
                .setAllowRotation(false)
                .setAspectRatio(3, 2)
                .setFixAspectRatio(true)
                .setRequestedSize(1200, 800)
                .start(getContext(), EditProfileFragment.this));


        mBind.rbMale.setChecked(mUser.isMale());
        mBind.rbFemale.setChecked(!mUser.isMale());

        mBind.btnSubmit.setOnClickListener(view -> {

            if (mBind.etWeight.getText().toString().isEmpty()) {
                mBind.etWeight.setText("0");
            }

            mUser.setName(mBind.etName.getText().toString());
            mUser.setGender(mBind.rbMale.isChecked());
            mUser.setWeight(mBind.etWeight.getText().toString());
            mUser.setCityId(mBind.spCity.getSelectedItemId());
            mUser.setBirthdayDateFa(String.format("%04d/%02d/%02d", (int) mBind.spDateYear.getSelectedItem(), (int) mBind.spDateMont.getSelectedItem(), (int) mBind.spDateDay.getSelectedItem()));
            waitingDialog.show();
            LiveData<RetStatusProgress> status = userViewModel.save(mUser, resultUri, getContext().getCacheDir().getPath());
            status.observe(EditProfileFragment.this, s -> {
                if (s.getStatus() == MyWebService.STATUS_WAITING) {
                    waitingDialog.setProgress(s.getProgress());
                } else if (s.getStatus() == MyWebService.STATUS_SUCCESS) {
                    waitingDialog.dismiss();
                    mListener.onSuccessfulEdited(mUser);

                } else {
                    waitingDialog.dismiss();
                    Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_SHORT).show();

                }
            });
        });
        waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.uploading_picture_wait));
        waitingDialog.setProgress(0);
        waitingDialog.setMax(100);
        waitingDialog.setIndeterminate(false);
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        return mBind.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = ViewModelProviders.of(getActivity(), mFactory).get(UserViewModel.class);
        userViewModel.init(mUser.getUserName());
        userViewModel.getUserLive().observe(this, user -> {
            mBind.setUser(user);
        });

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
        // mUserDao.saveUser(mUser);
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
        final List<Integer> nums;

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
