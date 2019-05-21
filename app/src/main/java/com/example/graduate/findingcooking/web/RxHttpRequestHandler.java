package com.example.graduate.findingcooking.web;


import com.example.graduate.findingcooking.bean.Comment;
import com.example.graduate.findingcooking.bean.Recipe;
import com.example.graduate.findingcooking.bean.Talk;
import com.example.graduate.findingcooking.bean.User;
import com.example.graduate.findingcooking.bean.Video;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RxHttpRequestHandler {


    @POST("user/register")
    Observable<WebResponse> register(@Query("nickname") String nickname, @Query("password") String password, @Query("userType") String userType);

    @POST("user/login")
    Observable<WebResponse<User>> login(@Query("nickname") String nickname, @Query("password") String password, @Query("userType") String userType);

    @Multipart
    @POST("user/avatar")
    Observable<WebResponse<String>> uploadUserAvatar(@Query("userId") long userId, @Part MultipartBody.Part file);


    @POST("recommend/push")
    Observable<WebResponse<List<Recipe>>> recommendRecipe(@Query("userId") long userId);

    @POST("food/searchAllByType")
    Observable<WebResponse<List<Recipe>>> searchByType(@Query("type") String type);

    @Multipart
    @POST("food/publish")
    Observable<WebResponse> publishRecipe(@Part MultipartBody.Part file, @Part(value = "ingredients") List<String> ingredients, @Query("foodName") String foodName, @Query("foodType") String foodType);
    @Multipart
    @POST("video/publish")
    Observable<WebResponse> publishVideo(@Query("title") String title, @Part MultipartBody.Part file);



    @POST("food/searchByName")
    Observable<WebResponse<List<Recipe>>> searchByName(@Query("name") String name);



    @POST("food/searchByStatue")
    Observable<WebResponse<List<Recipe>>> getRecipeList(@Query("statue") String statue);
    @POST("video/searchAll")
    Observable<WebResponse<List<Video>>> getVideoList(@Query("active") String active);



    @Multipart
    @POST("formu/publish")
    Observable<WebResponse> publishTalk(@Query("content") String content,@Query("userName") String userName,@Query("userAvatar") String userAvatar,@Part List<MultipartBody.Part> pictures);
    @POST("formu/searchAll")
    Observable<WebResponse<List<Talk>>> getForumList(@Query("userId")long userId);
    @POST("formu/updateStar")
    Observable<WebResponse> updateStar(@Query("updateType")String updateType,@Query("forumId")long forumId,@Query("userId")long userId);



    @POST("comment/publish")
    Observable<WebResponse> publishComment(@Query("content")String content,@Query("comment_user")String comment_user,@Query("forumId")long forumId);
    @POST("comment/searchAll")
    Observable<WebResponse<List<Comment>>> getCommentList(@Query("forumId")long forumId);


    @POST("food/updateState")
    Observable<WebResponse> updateRecipeState(@Query("state") String state, @Query("id") long id);
    @POST("video/updateState")
    Observable<WebResponse> updateVideoState(@Query("state") String state, @Query("id") long id);


    @POST("history/publish")
    Observable<WebResponse> historyPush(@Query("recipeId") long recipeId, @Query("userId") long userId);
}
