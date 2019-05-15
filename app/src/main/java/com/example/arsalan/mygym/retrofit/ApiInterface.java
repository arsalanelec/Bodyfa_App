package com.example.arsalan.mygym.retrofit;

/**
 * Created by Arsalan on 03-10-2017.
 */

import com.example.arsalan.mygym.models.RetCommentList;
import com.example.arsalan.mygym.models.RetGalleryList;
import com.example.arsalan.mygym.models.RetGym;
import com.example.arsalan.mygym.models.RetGymList;
import com.example.arsalan.mygym.models.RetHonorList;
import com.example.arsalan.mygym.models.RetInboxList;
import com.example.arsalan.mygym.models.RetMealPlan;
import com.example.arsalan.mygym.models.RetMealPlanList;
import com.example.arsalan.mygym.models.RetNewsDetail;
import com.example.arsalan.mygym.models.RetNewsList;
import com.example.arsalan.mygym.models.RetPMList;
import com.example.arsalan.mygym.models.RetTrainer;
import com.example.arsalan.mygym.models.RetTrainerList;
import com.example.arsalan.mygym.models.RetTrainerWorkoutPlanReqList;
import com.example.arsalan.mygym.models.RetTransactionList;
import com.example.arsalan.mygym.models.RetTutorialList;
import com.example.arsalan.mygym.models.RetTutorialVideoList;
import com.example.arsalan.mygym.models.RetUserList;
import com.example.arsalan.mygym.models.RetUserProfile;
import com.example.arsalan.mygym.models.RetWorkoutPlan;
import com.example.arsalan.mygym.models.RetWorkoutPlanList;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.RetroResultActivation;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.models.UserCredit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface ApiInterface {


    @FormUrlEncoded
    @POST("/api/token")
    Call<Token> getToken(@Field("Grant_type") String grantType, @Field("password") String password, @Field("username") String username);
    //Call<Token> getToken(@Field("Grant_type") String grantType, @Field("password") RequestBody password, @Field("username") RequestBody username);

    @Multipart
    @POST("/api/users/getprofile")
    Call<RetUserProfile> getProfile(@Header("Authorization") String token, @Part("username") RequestBody username);


    @Multipart
    @POST("/api/users/editprofile")
    Call<RetroResult> editProfile(@Header("Authorization") String token, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part image, @Part MultipartBody.Part thumb);

    @Multipart
    @POST("/api/users/GetUserCredit")
    Call<UserCredit> getUserCredit(@Header("Authorization") String token, @Part("userId") long userId);

    @Multipart
    @POST("/api/users/GetTransactionsList")
    Call<RetTransactionList> getTransactionList(@Header("Authorization") String token, @Part("userId") long userId);

    @Multipart
    @POST("/api/public/getnewslist")
    Call<RetNewsList> getNewsList(@Part("offset") int offset, @Part("limit") int limit, @Part("TypeId") int TypeId, @Part("PublisherId") long publisherId);

    @Multipart
    @POST("/api/users/GetNews")
    Call<RetNewsDetail> getNewsDetail(@Header("Authorization") String token, @Part("UserId") long userId, @Part("NewsId") long newsId);

    @Multipart
    @POST("/api/Trainer/EditTrainerDetail")
    Call<RetroResult> editTrainerDetail(@Header("Authorization") String token, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part docImage, @Part MultipartBody.Part docThumb, @Part MultipartBody.Part natCardImage, @Part MultipartBody.Part natCardThumb);

    @Multipart
    @POST("/api/public/gettrainerdetail")
    Call<RetTrainer> getTrainerDetail(@Part("trainerId") long userId);


    @Multipart
    @POST("/api/public/gettrainerlist")
    Call<RetTrainerList> getTrainerList(@Part("offset") int offset, @Part("limit") int limit, @Part("GymId") int gymId, @Part("CityId") int cityId, @Part("Sort") int sort);


    @Multipart
    @POST("/api/users/GetNewsComment")
    Call<RetCommentList> getCommentList(@Header("Authorization") String token, @Part("offset") int offset, @Part("limit") int limit, @Part("NewsId") long newsId);

    @Multipart
    @POST("/api/users/SendNewsComment")
    Call<RetroResult> sendNewsComment(@Header("Authorization") String token, @Part("UserId") long userId, @Part("NewsId") long newsId, @Part("Comment") String Comment);

    @Multipart
    @POST("/api/users/GetTutorialSubCategories")
    Call<RetTutorialList> getTutorialList(@Header("Authorization") String token, @Part("TutorialCategoryId") int tutorialCatId);

    @Multipart
    @POST("/api/public/GetTutorialList")
    Call<RetTutorialVideoList> getTutorialVideoList(@Part("offset") int offset, @Part("limit") int limit, @Part("TutorialsubCategoryId") long tutorialSubCatId);

    @Multipart
    @POST("/api/users/SendTutorialClip")
    Call<RetroResult> sendTutorialVideo(@Header("Authorization") String token, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part video, @Part MultipartBody.Part thumb);// @Part("UserId") long userId, @Part("tutorialCategoryId") int tutorialCategoryId, @Part("TutorialSubCategoryId") int TutorialSubCategoryId, @Part("Title") String title, @Part MultipartBody.Part video, @Part MultipartBody.Part thumb);


    @Multipart
    @POST("/api/users/SendContent")
    Call<RetroResult> sendContent(@Header("Authorization") String token, @Part("UserId") long userId, @Part("NewsTypeId") int newsTypeId, @Part("Title") RequestBody title, @Part("Content") RequestBody content, @Part MultipartBody.Part image, @Part MultipartBody.Part thumb);

    @Multipart
    @POST("/api/public/GetActivationCode")
    Call<RetroResultActivation> getActivationCode(@Part("Mobile") RequestBody mobileNo);

    @Multipart
    @POST("/api/public/checkactivationcode")
    Call<RetroResult> checkActivationCode(@Part("UserId") long userId, @Part("Mobile") RequestBody mobileNo, @Part("activationcode") RequestBody activationCode);

    @Multipart
    @POST("/api/users/addUserToRole")
    Call<RetroResult> addUserToRole(@Header("Authorization") String token, @Part("UserId") long userId, @Part("RoleName") RequestBody roleName);

    //خبر
    @Multipart
    @POST("/api/users/NewsLike")
    Call<RetroResult> likeANews(@Header("Authorization") String token, @Part("UserId") long userId, @Part("NewsId") long newsId);

    @Multipart
    @POST("/api/users/NewsDisLike")
    Call<RetroResult> dislikeANews(@Header("Authorization") String token, @Part("UserId") long userId, @Part("NewsId") long newsId);

    //ارسال پیام خصوصی
    @Multipart
    @POST("/api/users/SendPrivateMessage")
    Call<RetroResult> sendPrivateMessage(@Header("Authorization") String token, @Part("UserId") long userId, @Part("UserIdReceiver") long receiverId, @Part("message") RequestBody message);

    //ارسال کد پوشه
    @Multipart
    @POST("/api/users/EditUsersPublishIds")
    Call<RetroResult> addEditPushehId(@Header("Authorization") String token, @PartMap Map<String, RequestBody> param);

    //لیست پیام ها با یک شخص
    @Multipart
    @POST("/api/users/GetPmList")
    Call<RetPMList> getPmList(@Header("Authorization") String token, @Part("UserId") long userId, @Part("partyId") long partyId);

    //لیست چت ها
    @Multipart
    @POST("/api/users/GetInboxList")
    Call<RetInboxList> getInboxList(@Header("Authorization") String token, @Part("UserId") long userId, @Part("Offset") int offset, @Part("Limit") int limit);

    @Multipart
    @POST("/api/users/GetMyGallery")
    Call<RetGalleryList> getMyGallery(@Header("Authorization") String token, @Part("UserId") long userId);

    @Multipart
    @POST("/api/users/AddToMyGallery")
    Call<RetroResult> addToMyGallery(@Header("Authorization") String token, @Part("UserId") long userId, @Part MultipartBody.Part image, @Part MultipartBody.Part thumb);

    @Multipart
    @POST("/api/users/RemoveFromMyGallery")
    Call<RetroResult> removeFromMyGallery(@Header("Authorization") String token, @Part("GalleryId") long galleryId);

    //////////////////////////////
    ///////////////مربوط به مربی//
    //////////////////////////////

    @Multipart
    @POST("/api/trainer/EditTrainerMealPlans")
    Call<RetroResult> editTrainerMealPlan(@Header("Authorization") String token, @Part("UserId") long userId, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("/api/trainer/EditTrainerWorkoutPlans")
    Call<RetroResult> editTrainerWorkoutPlan(@Header("Authorization") String token, @Part("UserId") long userId, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("/api/trainer/GetTrainerWorkoutPlan")
    Call<RetWorkoutPlan> getTrainerWorkoutPlan(@Header("Authorization") String token, @Part("TrainerWorkoutPlanId") long planId);

    @Multipart
    @POST("/api/trainer/GetListOfTrainerWorkoutPlans")
    Call<RetWorkoutPlanList> getTrainerWorkoutPlanList(@Header("Authorization") String token, @Part("UserId") long userId, @Part("offset") int offset, @Part("limit") int limit);


    @Multipart
    @POST("/api/trainer/GetListOfTrainerMealPlans")
    Call<RetMealPlanList> getTrainerMealPlanList(@Header("Authorization") String token, @Part("UserId") long userId, @Part("offset") int offset, @Part("limit") int limit);

    @Multipart
    @POST("/api/trainer/GetTrainerMealPlans")
    Call<RetMealPlan> getTrainerMealPlan(@Header("Authorization") String token, @Part("TrainerMealPlanId") long planId);

    @Multipart
    @POST("/api/trainer/SendTrainerMealPlanToAthlete")
    Call<RetroResult> sendTrainerMealPlan(@Header("Authorization") String token, @Part("TrainerMealPlanId") long planId, @Part("AthleteUserId") long athleteId, @Part("Title") RequestBody planTitle, @Part("Description") RequestBody planBody);


    @Multipart
    @POST("/api/trainer/SendTrainerWorkoutPlanToAthlete")
    Call<RetroResult> sendTrainerWorkoutPlan(@Header("Authorization") String token, @Part("TrainerWorkoutPlanId") long planId, @Part("AthleteUserId") long athleteId, @Part("Title") RequestBody planTitle, @Part("Description") RequestBody planBody);


    @Multipart
    @POST("/api/trainer/RemoveTrainerWorkoutPlan")
    Call<RetroResult> removeTrainerWorkoutPlan(@Header("Authorization") String token, @Part("TrainerWorkoutPlanId") long planId);

    @Multipart
    @POST("/api/trainer/RemoveTrainerMealPlans")
    Call<RetroResult> removeTrainerMealPlan(@Header("Authorization") String token, @Part("TrainerMealPlanId") long planId);


    @Multipart
    @POST("/api/Trainer/GetTrainerAthletes")
    Call<RetUserList> getMyAthleteList(@Header("Authorization") String token, @Part("TrainerId") long trainerId);

    @Multipart
    @POST("api/Trainer/EditHonors")
    Call<RetroResult> addEditHonor(@Header("Authorization") String token, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part image, @Part MultipartBody.Part thumb);

    @Multipart
    @POST("api/Trainer/GetHonors")
    Call<RetHonorList> getHonorList(@Header("Authorization") String token, @Part("UserId") long userId);

    @Multipart
    @POST("api/Trainer/RemoveHonor")
    Call<RetroResult> removeHonor(@Header("Authorization") String token, @Part("HonorId") long honorId);

    @Multipart
    @POST("api/Trainer/GetMyAthleteWorkoutPlanRequests")
    Call<RetTrainerWorkoutPlanReqList> getTrainerWorkoutPlanRequests(@Header("Authorization") String token, @Part("UserId") long userId);

    @Multipart
    @POST("api/Trainer/CancelAthleteWorkoutPlanRequest")
    Call<RetroResult> trainerCancelWorkoutPlanRequest(@Header("Authorization") String token, @Part("AthleteWorkoutPlanRequestId") long requestId);

    //////////////
    //باشگاه
    //////////////
    @Multipart
    @POST("/api/public/getgymlist")
    Call<RetGymList> getGymList(@Part("offset") int offset, @Part("limit") int limit, @Part("CityId") int cityId, @Part("Sort") int sort);

    @Multipart
    @POST("/api/public/getgym")
    Call<RetGym> getGymDetail(@Part("UserId") long userId);

    @Multipart
    @POST("api/gyms/GetGymDetail")
    Call<RetGym> getGymProfile(@Header("Authorization") String token, @Part("UserId") long userId);

    @Multipart
    @POST("/api/gyms/EditGymDetail")
    Call<RetroResult> addEditGymDetail(@Header("Authorization") String token, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part image, @Part MultipartBody.Part thumb);

    ///////////////////////////
    ////////////////////ورزشکار
    ///////////////////////////

    @Multipart
    @POST("api/Athletes/JoinAthleteToTrainer")
    Call<RetroResult> selectMyTrainer(@Header("Authorization") String token, @Part("AthleteId") long userId, @Part("TrainerId") long trainerId);

    @Multipart
    @POST("api/athletes/GetMyMealPlanList")
    Call<RetMealPlanList> getAthleteMealPlanList(@Header("Authorization") String token, @Part("UserId") long userId);

    @Multipart
    @POST("api/athletes/GetMyMealPlan")
    Call<RetMealPlan> getAthleteMealPlan(@Header("Authorization") String token, @Part("AthleteMealPlanId") long planId);

    @Multipart
    @POST("api/athletes/GetMyWorkoutPlanList")
    Call<RetWorkoutPlanList> getAthleteWorkoutPlanList(@Header("Authorization") String token, @Part("UserId") long userId);

    @Multipart
    @POST("api/athletes/GetMyWorkoutPlan")
    Call<RetWorkoutPlan> getAthleteWorkoutPlan(@Header("Authorization") String token, @Part("AthleteWorkoutPlanId") long planId);


    @Multipart
    @POST("api/athletes/AddPointToTrainer")
    Call<RetroResult> AddRateToTrainer(@Header("Authorization") String token, @Part("Trainerid") long trainerid, @Part("Athleteid") long athleteId, @Part("Point") int rate);

    @Multipart
    @POST("api/Athletes/AthleteWorkoutPlanRequest")
    Call<RetroResult> requestWorkoutPlan(@Header("Authorization") String token, @PartMap Map<String, RequestBody> params);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);

   }