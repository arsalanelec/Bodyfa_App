package com.example.arsalan.mygym.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalan.mygym.PlayVideoActivity;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetTutorialVideoList;
import com.example.arsalan.mygym.models.TutorialVideo;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TutorialListViewModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TutorialVideoListDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TutorialVideoListDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialVideoListDialog extends DialogFragment implements Injectable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    final private String TAG = getClass().getSimpleName();
    @Inject
    MyViewModelFactory factory;
    // TODO: Rename and change types of parameters
    private long mSubCatId;
    private List<TutorialVideo> tutorialVideoList;
    private OnFragmentInteractionListener mListener;
    private AdapterTutorialVideo mAdapter;

    /*
      @Inject
      DispatchingAndroidInjector<Fragment> childFragmentInjector;
    */
    private TutorialListViewModel tutorialListViewModel;
    private GridView gridView;
    private ProgressBar waitingPB;


    public TutorialVideoListDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment GymListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialVideoListDialog newInstance(long param1) {
        TutorialVideoListDialog fragment = new TutorialVideoListDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubCatId = getArguments().getLong(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tutorial_video_list, container, false);
        gridView = v.findViewById(R.id.lstTutorialVideos);

        tutorialVideoList = new ArrayList<>();
        mAdapter = new AdapterTutorialVideo(tutorialVideoList);
        gridView.setAdapter(mAdapter);
        waitingPB = v.findViewById(R.id.pbWaiting);

//        getTutorialVideoListWeb(mSubCatId);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tutorialListViewModel = ViewModelProviders.of(this, factory).get(TutorialListViewModel.class);
        tutorialListViewModel.init(mSubCatId);
        tutorialListViewModel.getTutorialVideoList().observe(this, tutorialVideos -> {
            Log.d(getClass().getSimpleName(), "onActivityCreated observe: tutorialVideos cnt:" + tutorialVideos.size());
            if (tutorialVideos.size() > 0) {
                tutorialVideoList.removeAll(tutorialVideoList);
                tutorialVideoList.addAll(tutorialVideos);
                mAdapter.notifyDataSetChanged();
                waitingPB.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), R.string.noting_to_show, Toast.LENGTH_SHORT).show();
                dismiss();
            }

            // waitingFL.setVisibility(View.GONE);
        });

    }

    @Override
    public void onAttach(Context context) {
        //AndroidSupportInjection.inject(this);
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
/*

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }
*/

    private void getTutorialVideoListWeb(long subTutorialId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetTutorialVideoList> call = apiService.getTutorialVideoList(0, 100, subTutorialId);
        call.enqueue(new Callback<RetTutorialVideoList>() {
            @Override
            public void onResponse(Call<RetTutorialVideoList> call, Response<RetTutorialVideoList> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful())
                    Log.d("getNewsWeb", "onResponse: records:" + response.body().getRecordsCount());
                if (response.body().getRecordsCount() > 0) {
                    tutorialVideoList.addAll(response.body().getRecords());
                } else {
                    getDialog().dismiss();
                    Toast.makeText(getContext(), R.string.noting_to_show, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RetTutorialVideoList> call, Throwable t) {
                waitingDialog.dismiss();

            }
        });

    }

    private void downloadVideo(String url, DownloadProgressListener downloadProgressListener) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.downloadFileWithDynamicUrlAsync(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    String fileName = url.substring(url.lastIndexOf('/') + 1);

                    new AsyncTask<String, Integer, Integer>() {
                        @Override
                        protected Integer doInBackground(String... strings) {
                            int result = 0;
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), strings[0], new DownloadProgressListener() {
                                @Override
                                public void onUpdateProgress(int progress) {
                                    publishProgress(progress);
                                }

                                @Override
                                public void onDownloadFailed() {


                                }

                                @Override
                                public void onDownloadCompleted() {
                                }
                            });

                            Log.d(TAG, "file download was a success? " + writtenToDisk);
                            return writtenToDisk ? 1 : -1;
                        }

                        @Override
                        protected void onProgressUpdate(Integer... values) {
                            super.onProgressUpdate(values);
                            downloadProgressListener.onUpdateProgress(values[0]);

                        }

                        @Override
                        protected void onPostExecute(Integer result) {
                            super.onPostExecute(result);
                            if (result == 1) {
                                downloadProgressListener.onDownloadCompleted();
                            } else {
                                downloadProgressListener.onDownloadFailed();
                            }

                        }

                    }.execute(fileName);
                } else {
                    try {
                        Log.d(TAG, "server contact failed:"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });

    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName, DownloadProgressListener downloadProgressListener) {
        try {
            File file = new File(getContext().getExternalFilesDir(null) + File.separator + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                    downloadProgressListener.onUpdateProgress((int) (100 * fileSizeDownloaded / fileSize));
                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                outputStream.flush();
                outputStream = null;
                file.delete();
                downloadProgressListener.onDownloadFailed();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            downloadProgressListener.onDownloadFailed();
            return false;
        }
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

    private interface DownloadProgressListener {
        void onUpdateProgress(int progress);

        void onDownloadFailed();

        void onDownloadCompleted();
    }

    class AdapterTutorialVideo extends BaseAdapter {
        List<TutorialVideo> videoList;

        public AdapterTutorialVideo(List<TutorialVideo> tutorialVideoList) {
            videoList = tutorialVideoList;
        }

        @Override
        public int getCount() {
            return videoList.size();
        }

        @Override
        public Object getItem(int i) {
            return videoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return videoList.get(i).getId();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_tutorial_video, viewGroup, false);
            }
            TextView nameTV = view.findViewById(R.id.txtName);
            SimpleDraweeView thumbImg = view.findViewById(R.id.imgThumb);
            nameTV.setText(videoList.get(i).getName());
            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            TextView progressTV = view.findViewById(R.id.txtProgress);
            String thumbUrl = MyConst.BASE_CONTENT_URL + videoList.get(i).getThumbUrl();
            Log.d(TAG, "getView: thumUrl:"+thumbUrl +"\n VideoUrl:"+videoList.get(i).getPictureUrl());
            thumbImg.setImageURI(thumbUrl);

            ImageButton playBtn = view.findViewById(R.id.btnPlay);
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PlayVideoActivity.class);
                    intent.putExtra(PlayVideoActivity.KEY_VIDEO_URL, videoList.get(i).getPictureUrl());
                    startActivity(intent);
                }
            });

            ImageButton downloadBtn = view.findViewById(R.id.btnDownload);
            downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadBtn.setVisibility(View.INVISIBLE);

                    progressBar.setVisibility(View.VISIBLE);
                    progressTV.setVisibility(View.VISIBLE);
                   /* Intent intent = new Intent();
                    intent.setClass(getActivity(), PlayVideoActivity.class);
                    intent.putExtra(PlayVideoActivity.KEY_VIDEO_URL, videoList.get(i).getPictureUrl());
                    startVideoRecorderActivity(intent);*/
                    downloadVideo(MyConst.BASE_CONTENT_URL + videoList.get(i).getPictureUrl(), new DownloadProgressListener() {
                        @Override
                        public void onUpdateProgress(int progress) {
                            progressBar.setProgress(progress);
                            progressTV.setText(String.valueOf(progress));
                        }

                        @Override
                        public void onDownloadFailed() {
                            downloadBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            progressTV.setVisibility(View.GONE);
                        }

                        @Override
                        public void onDownloadCompleted() {
                            playBtn.setVisibility(View.VISIBLE);
                            downloadBtn.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            progressTV.setVisibility(View.GONE);
                        }
                    });

                }
            });
            String videoUrl = MyConst.BASE_CONTENT_URL + videoList.get(i).getPictureUrl();
            String fileName = videoUrl.substring(videoUrl.lastIndexOf('/') + 1);
            File file = new File(getContext().getExternalFilesDir(null) + File.separator + fileName);
            if (file.exists()) {
                downloadBtn.setVisibility(View.GONE);
                playBtn.setVisibility(View.VISIBLE);
            }

            return view;
        }
    }
}
