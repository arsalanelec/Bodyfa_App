package com.example.arsalan.mygym.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.models.MealPlan;
import com.example.arsalan.mygym.models.MealPlanDay;
import com.example.arsalan.mygym.models.RetMealPlan;
import com.example.arsalan.mygym.models.RetMealPlanList;
import com.example.arsalan.mygym.models.RetWorkoutPlan;
import com.example.arsalan.mygym.models.RetWorkoutPlanList;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterTrainerMealPlanList;
import com.example.arsalan.mygym.adapters.AdapterTrainerWorkoutPlanList;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.dialog.MealPlanListDialog;
import com.example.arsalan.mygym.dialog.MyAthleteListDialog;
import com.example.arsalan.mygym.dialog.NewPlanDialog;
import com.example.arsalan.mygym.dialog.TrainerWorkoutPlanListDialog;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerMealPlanListViewModel;
import com.example.arsalan.mygym.viewModels.TrainerWorkoutListViewModel;
import com.example.arsalan.room.MealPlanDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.arsalan.mygym.MyKeys.EXTRA_ATHLETE_ID;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PLAN_BODY;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PLAN_ID;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PLAN_TITLE;


public class TrainerPlansTabFragment extends Fragment implements Injectable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final int REQ_MEAL_PLAN_LIST_DIALOG = 1000;
    private static final int REQ_WORKOUT_PLAN_LIST_DIALOG = 1001;
    private static final int REQ_SELECT_USER_MEAL_PLAN = 1002;
    private static final int REQ_SELECT_USER_WORKOUT_PLAN = 1003;

    // TODO: Rename and change types of parameters
    private Trainer mCurrentTrainer;

    private OnFragmentInteractionListener mListener;
    private List<MealPlan> mealPlanListLimited;
    private ArrayList<MealPlan> mealPlanList;

    private List<WorkoutPlan> workoutPlanListLimited;
    private ArrayList<WorkoutPlan> workoutPlanList;

    private AdapterTrainerMealPlanList adapterMealLVLimited;
    private AdapterTrainerWorkoutPlanList adapterWorkoutLVLimited;

    private TrainerMealPlanListViewModel mealPlanListViewModel;
    private TrainerWorkoutListViewModel workoutListViewModel;

    @Inject
    MyViewModelFactory factory;

    @Inject
    MealPlanDao mealPlanDao;

    public TrainerPlansTabFragment() {
        // Required empty public constructor
    }

    public static TrainerPlansTabFragment newInstance(Trainer trainer) {
        TrainerPlansTabFragment fragment = new TrainerPlansTabFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, trainer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentTrainer = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trainer_plans_tab, container, false);
        LinearLayout newPlaneBtn = v.findViewById(R.id.llNewPlane);
        newPlaneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPlanDialog dialog = new NewPlanDialog();
                dialog.show(getFragmentManager(), "");
                // برو به صفحه ایجاد پلن
            }
        });

        //باز کردن لیست کامل برنامه های غذایی
        LinearLayout mealPlanListLL = v.findViewById(R.id.llOrderMealPlane);
        mealPlanListLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealPlanListDialog dialog = MealPlanListDialog.newInstance(mealPlanList);
                dialog.setTargetFragment(TrainerPlansTabFragment.this, REQ_MEAL_PLAN_LIST_DIALOG);
                dialog.show(getFragmentManager(), "");
            }
        });

        //باز کردن لیست کامل برنامه های تمرینی
        final LinearLayout workoutPlanLL = v.findViewById(R.id.llWokroutPlane);
        workoutPlanLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainerWorkoutPlanListDialog dialog = TrainerWorkoutPlanListDialog.newInstance(workoutPlanList);
                dialog.setTargetFragment(TrainerPlansTabFragment.this, REQ_WORKOUT_PLAN_LIST_DIALOG);
                dialog.show(getFragmentManager(), "");
            }
        });
        mealPlanListLimited = new ArrayList<>();
        mealPlanList = new ArrayList<>();

        adapterMealLVLimited = new AdapterTrainerMealPlanList(getContext(), mealPlanListLimited, new AdapterTrainerMealPlanList.OnItemClickListener() {
            @Override
            public void onItemEditClick(MealPlan mealPlan, int position) {
  /*              mealPlanRepository.getMealPlan("Bearer " + ((MyApplication2) getActivity().getApplication()).getCurrentToken().getToken(), mealPlan.getTrainerMealPlanId())
                        .observe(TrainerPlansTabFragment.this, mealPlan1 -> {
                            mListener.addEditMealPlan(mealPlan1.getTrainerMealPlanId(), mealPlan1.getTitle(), mealPlan1.getDescription(), true);

                        });*/
                getTrainerMealPlanWeb(mealPlan.getTrainerMealPlanId(), new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        //ویرایش برنامه
                        mListener.addEditMealPlan(id, title, body, true);
                    }
                });
            }

            @Override
            public void onItemDeleteClick(final MealPlan mealPlan, final int position) {
                new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom).setMessage(getString(R.string.ask_remove_plan))
                        .setPositiveButton(getString(R.string.remove), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int ii) {
                                dialogInterface.dismiss();
                                removeTrainerMealPlanWeb(mealPlan.getTrainerMealPlanId());
                                adapterMealLVLimited.removeItem(position);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }

            @Override
            public void onItemShowClick(MealPlan mealPlan, int position) {
                getTrainerMealPlanWeb(mealPlan.getTrainerMealPlanId(), new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        //ویرایش برنامه
                        mListener.addEditMealPlan(id, title, body, false);
                    }
                });

            }

            @Override
            public void onItemSendClick(MealPlan mealPlan, int position) {
                getTrainerMealPlanWeb(mealPlan.getTrainerMealPlanId(), new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        MyAthleteListDialog dialog = MyAthleteListDialog.newInstance(mCurrentTrainer, id, title, body);
                        dialog.setTargetFragment(TrainerPlansTabFragment.this, REQ_SELECT_USER_MEAL_PLAN);
                        dialog.show(getFragmentManager(), "");
                    }
                });

            }
        });


        ListView mealPlanLV = v.findViewById(R.id.lstMealPlan);
        mealPlanLV.setAdapter(adapterMealLVLimited);

        workoutPlanListLimited = new ArrayList<>();
        workoutPlanList = new ArrayList<>();

        adapterWorkoutLVLimited = new AdapterTrainerWorkoutPlanList(getContext(), workoutPlanListLimited, new AdapterTrainerWorkoutPlanList.OnItemClickListener() {
            @Override
            public void onItemEditClick(WorkoutPlan workoutPlan, int position) {
                getTrainerWorkoutPlanWeb(workoutPlan.getTrainerWorkoutPlanId(), new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        mListener.addEditWorkoutPlan(id, title, body, true);

                    }
                });
            }

            @Override
            public void onItemDeleteClick(final WorkoutPlan workoutPlan, final int position) {
                new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom).setMessage(getString(R.string.ask_remove_plan))
                        .setPositiveButton(getString(R.string.remove), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int ii) {
                                dialogInterface.dismiss();
                                removeTrainerWorkoutPlanWeb(workoutPlan.getTrainerWorkoutPlanId());
                                adapterWorkoutLVLimited.removeItem(position);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }

            @Override
            public void onItemShowClick(WorkoutPlan workoutPlan, int position) {
                getTrainerWorkoutPlanWeb(workoutPlan.getTrainerWorkoutPlanId(), new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        mListener.addEditWorkoutPlan(id, title, body, false);

                    }
                });

            }

            @Override
            public void onItemSendClick(WorkoutPlan workoutPlan, int position) {
                getTrainerWorkoutPlanWeb(workoutPlan.getTrainerWorkoutPlanId(), new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        MyAthleteListDialog dialog = MyAthleteListDialog.newInstance(mCurrentTrainer, id, title, body);
                        dialog.setTargetFragment(TrainerPlansTabFragment.this, REQ_SELECT_USER_WORKOUT_PLAN);
                        dialog.show(getFragmentManager(), "");
                    }
                });
            }
        });
        ListView workoutLV = v.findViewById(R.id.lstWorkoutPlan);
        workoutLV.setAdapter(adapterWorkoutLVLimited);

        //  getMealPlanListWeb();
        //   getWorkoutPlanListWeb();
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mealPlanListViewModel = ViewModelProviders.of(this, factory).get(TrainerMealPlanListViewModel.class);
        mealPlanListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentTrainer.getId());
        mealPlanListViewModel.getMealPlanItemList().observe(this, mealPlans -> {
            Log.d("onActivityCreated", "observe: ");
            mealPlanListLimited.removeAll(mealPlanListLimited);
            int cnt = 0;
            for (MealPlan mealPlan : mealPlans) {
                if (cnt >= 2) break;
                mealPlanListLimited.add(mealPlan);
                cnt++;
            }
            adapterMealLVLimited.notifyDataSetChanged();
            mealPlanList.removeAll(mealPlanList);
            mealPlanList.addAll(mealPlans);
            // waitingFL.setVisibility(View.GONE);
        });

        workoutListViewModel = ViewModelProviders.of(this, factory).get(TrainerWorkoutListViewModel.class);
        workoutListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentTrainer.getId());
        workoutListViewModel.getWorkoutPlanItemList().observe(this, newWorkoutPlans -> {
            Log.d("onActivityCreated", "observe: ");
            workoutPlanListLimited.removeAll(workoutPlanListLimited);
            int cnt = 0;
            for (WorkoutPlan plan : newWorkoutPlans) {
                if (cnt >= 2) break;
                workoutPlanListLimited.add(plan);
                cnt++;
            }
            adapterWorkoutLVLimited.notifyDataSetChanged();
            workoutPlanList.removeAll(workoutPlanList);
            workoutPlanList.addAll(newWorkoutPlans);
            // waitingFL.setVisibility(View.GONE);
        });
        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }/* else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void addEditMealPlan(long planId, String planName, String planBody, boolean editMode);

        void addEditWorkoutPlan(long planId, String planName, String planBody, boolean isEditable);
    }

    public void refreshMealPlanList() {
        //getMealPlanListWeb();
        mealPlanListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentTrainer.getId());

    }

    public void refreshWorkoutPlanList() {
        workoutListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentTrainer.getId());

        // getWorkoutPlanListWeb();
    }


    private class AdapterWorkoutPlanLV extends BaseAdapter {

        @Override
        public int getCount() {
            return workoutPlanListLimited.size();
        }

        @Override
        public Object getItem(int i) {
            return workoutPlanListLimited.get(i);
        }

        @Override
        public long getItemId(int i) {
            return workoutPlanListLimited.get(i).getTrainerWorkoutPlanId();
        }

        public void removeItem(int i) {
            workoutPlanListLimited.remove(i);
            adapterWorkoutLVLimited.notifyDataSetChanged();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.item_listview_trainer_plan, null);
                Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                animation.setDuration(500);
                view.startAnimation(animation);
            }
            TextView titleTV = view.findViewById(R.id.txtTitle);
            TextView dateTV = view.findViewById(R.id.txtDate);

            titleTV.setText(workoutPlanListLimited.get(i).getTitle());
            dateTV.setText(workoutPlanListLimited.get(i).getDate());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getTrainerWorkoutPlanWeb(getItemId(i), new OnGetPlanFromWeb() {
                        @Override
                        public void onGetPlan(long id, String title, String body) {
                            mListener.addEditWorkoutPlan(id, title, body, false);

                        }
                    });
                }
            });

            Button deleteBtn = view.findViewById(R.id.btnDelete);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom).setMessage(getString(R.string.ask_remove_plan))
                            .setPositiveButton(getString(R.string.remove), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int ii) {
                                    dialogInterface.dismiss();
                                    removeTrainerWorkoutPlanWeb(getItemId(i));
                                    removeItem(i);
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).show();

                }
            });
            return view;
        }
    }

   /* private void getMealPlanListWeb() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.receiving_meal_plan_list));
        waitingDialog.show();

        Call<RetMealPlanList> call = apiService.getTrainerMealPlanList("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentTrainer.getId(), 0, 100);
        call.enqueue(new Callback<RetMealPlanList>() {
            @Override
            public void onResponse(Call<RetMealPlanList> call, Response<RetMealPlanList> response) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("getMealPlanListWeb", "onResponse: count" + response.body().getRecordsCount());
                   *//* mealPlanListLimited.removeAll(mealPlanListLimited);
                    if (response.body().getRecordsCount() > 0 && response.body().getRecords().get(0) != null)
                        mealPlanListLimited.add(response.body().getRecords().get(0));
                                            adapterMealLVLimited.notifyDataSetChanged();

                    if (response.body().getRecordsCount() > 1 && response.body().getRecords().get(1) != null)
                        mealPlanListLimited.add(response.body().getRecords().get(1));
*//*
                    // mealPlanList.removeAll(mealPlanList);
                    //  mealPlanList.addAll(response.body().getRecords());

                }
            }

            @Override
            public void onFailure(Call<RetMealPlanList> call, Throwable t) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();

            }
        });

    }

    private void getWorkoutPlanListWeb() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.receiving_workout_plan_list));
        waitingDialog.show();

        Call<RetWorkoutPlanList> call = apiService.getTrainerWorkoutPlanList("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentTrainer.getId(), 0, 100);
        call.enqueue(new Callback<RetWorkoutPlanList>() {
            @Override
            public void onResponse(Call<RetWorkoutPlanList> call, Response<RetWorkoutPlanList> response) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("getWorkoutPlanListWeb", "onResponse: count" + response.body().getRecordsCount());
                    workoutPlanListLimited.removeAll(workoutPlanListLimited);
                    if (response.body().getRecordsCount() > 0 && response.body().getRecords().get(0) != null)
                        workoutPlanListLimited.add(response.body().getRecords().get(0));
                    if (response.body().getRecordsCount() > 1 && response.body().getRecords().get(1) != null)
                        workoutPlanListLimited.add(response.body().getRecords().get(1));
                    adapterWorkoutLVLimited.notifyDataSetChanged();
                    workoutPlanList.removeAll(workoutPlanList);
                    workoutPlanList.addAll(response.body().getRecords());
                }
            }

            @Override
            public void onFailure(Call<RetWorkoutPlanList> call, Throwable t) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();

            }
        });

    }*/

    private void getTrainerMealPlanWeb(final long planId, final OnGetPlanFromWeb OnGetPlanFromWeb) {
        // MutableLiveData<MealPlan> mealPlanLiveData=new MutableLiveData<>();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        Log.d("getView", "onClick: id:" + planId);
        waitingDialog.setMessage(getString(R.string.receiving_meal_plan));
        waitingDialog.show();
        Executor executor = Executors.newFixedThreadPool(3);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Call<RetMealPlan> call = apiService.getTrainerMealPlan("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), planId);
                try {
                    Response<RetMealPlan> response = call.execute();
                    if (response.isSuccessful()) {
                        /*Gson gson = new Gson();
                        List<MealPlanDay> mealPlanList = gson.fromJson(response.body().getRecord().getDescription(), new TypeToken<List<MealPlanDay>>() {
                        }.getType());*/
                        OnGetPlanFromWeb.onGetPlan(planId, response.body().getRecord().getTitle(), response.body().getRecord().getDescription());
                        mealPlanDao.updateMealPlan(response.body().getRecord());
                    }else {
                        MealPlan mealPlan = mealPlanDao.getMealPlanById(planId);

                        OnGetPlanFromWeb.onGetPlan(planId, mealPlan.getTitle(),mealPlan.getDescription());
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    MealPlan mealPlan = mealPlanDao.getMealPlanById(planId);

                    OnGetPlanFromWeb.onGetPlan(planId, mealPlan.getTitle(),mealPlan.getDescription());
                }finally {
                    if (waitingDialog.isShowing())
                        waitingDialog.dismiss();
                }
                /*call.enqueue(new Callback<RetMealPlan>() {
                    @Override
                    public void onResponse(Call<RetMealPlan> call, Response<RetMealPlan> response) {
                        if (waitingDialog.isShowing())
                            waitingDialog.dismiss();
                        if (response.isSuccessful()) {
                            Log.d("getTrainerMealPlanWeb", "onResponse: desc:" + response.body().getRecord().getDescription());

                            try {

                                Log.d("getTrainerMealPlanWeb", "onResponse: " + mealPlanList);

                            } catch (Exception e) {
                            }
                            Log.d("getTrainerMealPlanWeb", "onResponse: planId:" + planId);
                            //  mealPlanLiveData.setValue(response.body().getRecord());

                        }
                    }

                    @Override
                    public void onFailure(Call<RetMealPlan> call, Throwable t) {
                        if (waitingDialog.isShowing())
                            waitingDialog.dismiss();

                    }
                });*/
            }
        });


    }

    private void getTrainerWorkoutPlanWeb(final long planId, final OnGetPlanFromWeb OnGetPlanFromWeb) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        Log.d("getView", "onClick: id:" + planId);
        waitingDialog.setMessage(getString(R.string.receiving_workout_plan));
        waitingDialog.show();
        Call<RetWorkoutPlan> call = apiService.getTrainerWorkoutPlan("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), planId);
        call.enqueue(new Callback<RetWorkoutPlan>() {
            @Override
            public void onResponse(Call<RetWorkoutPlan> call, Response<RetWorkoutPlan> response) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("getTrainerMealPlanWeb", "onResponse: desc:" + response.body().getRecord().getDescription());

                    try {
                        Gson gson = new Gson();
                        List<MealPlanDay> mealPlanList = gson.fromJson(response.body().getRecord().getDescription(), new TypeToken<List<MealPlanDay>>() {
                        }.getType());
                        Log.d("getTrainerMealPlanWeb", "onResponse: " + mealPlanList);

                    } catch (Exception e) {
                    }
                    Log.d("getTrainerMealPlanWeb", "onResponse: planId:" + planId);
                    //ویرایش برنامه
                    OnGetPlanFromWeb.onGetPlan(planId, response.body().getRecord().getTitle(), response.body().getRecord().getDescription());
                }
            }

            @Override
            public void onFailure(Call<RetWorkoutPlan> call, Throwable t) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();

            }
        });

    }

    private void removeTrainerMealPlanWeb(long planId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        Log.d("getView", "onClick: id:" + planId);
        waitingDialog.setMessage(getString(R.string.removing_meal_plan));
        waitingDialog.show();
        Call<RetroResult> call = apiService.removeTrainerMealPlan("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), planId);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    refreshMealPlanList();
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();

            }
        });

    }

    private void removeTrainerWorkoutPlanWeb(long planId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        Log.d("getView", "onClick: id:" + planId);
        waitingDialog.setMessage(getString(R.string.removing_workout_plan));
        waitingDialog.show();
        Call<RetroResult> call = apiService.removeTrainerWorkoutPlan("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), planId);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    refreshWorkoutPlanList();
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                if (waitingDialog.isShowing())
                    waitingDialog.dismiss();

            }
        });

    }

    /*    private void sendPlanToAthlete(long trainerId, long athleteId, String planTitle, String planBody) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            final ProgressDialog waitingDialog = new ProgressDialog(getContext());
            Log.d("getView", "onClick: id:" + planId);
            waitingDialog.setMessage("در حال حذف برنامه تمرینی...");
            waitingDialog.show();
            Call<RetroResult> call = apiService.sendTrainerMealPlan("Bearer " + ((MyApplication2) getActivity().getApplication()).getCurrentToken().getToken(), trainerId, athleteId, planTitle, planBody);
        }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQ_MEAL_PLAN_LIST_DIALOG) {
            long planId = data.getLongExtra(MyKeys.EXTRA_PLAN_ID, 0);
            if (resultCode == MyKeys.RESULT_EDIT) {
                getTrainerMealPlanWeb(planId, new OnGetPlanFromWeb() {
                            @Override
                            public void onGetPlan(long id, String title, String body) {
                                //ویرایش برنامه
                                mListener.addEditMealPlan(id, title, body, true);
                            }
                        }
                );


            } else if (resultCode == MyKeys.RESULT_DELETE) {
                removeTrainerMealPlanWeb(planId);
            } else if (resultCode == MyKeys.RESULT_SHOW) {
                getTrainerMealPlanWeb(planId, new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        //ویرایش برنامه
                        mListener.addEditMealPlan(id, title, body, false);
                    }
                });
            } else if (resultCode == MyKeys.RESULT_SEND) {
                getTrainerMealPlanWeb(planId, new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        MyAthleteListDialog dialog = MyAthleteListDialog.newInstance(mCurrentTrainer, id, title, body);
                        dialog.setTargetFragment(TrainerPlansTabFragment.this, REQ_SELECT_USER_MEAL_PLAN);
                        dialog.show(getFragmentManager(), "");
                    }
                });

            }
        } else if (requestCode == REQ_WORKOUT_PLAN_LIST_DIALOG) {
            long planId = data.getLongExtra(EXTRA_PLAN_ID, 0);
            if (resultCode == MyKeys.RESULT_EDIT) {
                getTrainerWorkoutPlanWeb(planId, new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        mListener.addEditWorkoutPlan(id, title, body, true);

                    }
                });
            } else if (resultCode == MyKeys.RESULT_DELETE) {
                removeTrainerWorkoutPlanWeb(planId);
            } else if (resultCode == MyKeys.RESULT_SHOW) {
                getTrainerWorkoutPlanWeb(planId, new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        mListener.addEditWorkoutPlan(id, title, body, false);
                    }
                });

            } else if (resultCode == MyKeys.RESULT_SEND) {
                getTrainerWorkoutPlanWeb(planId, new OnGetPlanFromWeb() {
                    @Override
                    public void onGetPlan(long id, String title, String body) {
                        MyAthleteListDialog dialog = MyAthleteListDialog.newInstance(mCurrentTrainer, id, title, body);
                        dialog.setTargetFragment(TrainerPlansTabFragment.this, REQ_SELECT_USER_WORKOUT_PLAN);
                        dialog.show(getFragmentManager(), "");
                    }
                });

            }
        } else if (requestCode == REQ_SELECT_USER_MEAL_PLAN) {
            if (resultCode == RESULT_OK) {
                sendMealPlanToAthleteWeb(data.getLongExtra(EXTRA_ATHLETE_ID, 0), data.getLongExtra(EXTRA_PLAN_ID, 0), data.getStringExtra(EXTRA_PLAN_TITLE), data.getStringExtra(EXTRA_PLAN_BODY));
            }
        } else if (requestCode == REQ_SELECT_USER_WORKOUT_PLAN) {
            if (resultCode == RESULT_OK) {
                sendWokroutPlanToAthleteWeb(data.getLongExtra(EXTRA_ATHLETE_ID, 0), data.getLongExtra(EXTRA_PLAN_ID, 0), data.getStringExtra(EXTRA_PLAN_TITLE), data.getStringExtra(EXTRA_PLAN_BODY));
            }
        }
    }

    //ارسال برنام غذایی به ورزشکار
    private void sendMealPlanToAthleteWeb(long athleteId, long mealPlanId, String title, String body) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.sending_plan));
        waitingDialog.show();
        Call<RetroResult> call = apiService.sendTrainerMealPlan("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken()
                , mealPlanId
                , athleteId
                , RequestBody.create(MediaType.parse("text/plain"), title)
                , RequestBody.create(MediaType.parse("text/plain"), body));

        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                Log.d(getClass().getSimpleName(), "onResponse: sent OK!");
                Toast.makeText(getContext(), getString(R.string.plan_sent_successfully), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                waitingDialog.dismiss();
                Log.d(getClass().getSimpleName(), "onResponse: send failed!");

            }
        });
    }

    //ارسال برنامه تمرینی به ورزشکار
    private void sendWokroutPlanToAthleteWeb(long athleteId, long planId, String title, String body) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.sending_plan));
        waitingDialog.show();
        Call<RetroResult> call = apiService.sendTrainerWorkoutPlan("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken()
                , planId
                , athleteId
                , RequestBody.create(MediaType.parse("text/plain"), title)
                , RequestBody.create(MediaType.parse("text/plain"), body));

        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                Log.d(getClass().getSimpleName(), "onResponse: sent OK!");
                Toast.makeText(getContext(), getString(R.string.plan_sent_successfully), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                waitingDialog.dismiss();
                Log.d(getClass().getSimpleName(), "onResponse: send failed!");

            }
        });
    }

    private interface OnGetPlanFromWeb {
        void onGetPlan(long id, String title, String body);
    }
}
