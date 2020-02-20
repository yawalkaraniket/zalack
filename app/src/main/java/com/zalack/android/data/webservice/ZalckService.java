package com.zalack.android.data.webservice;

import com.zalack.android.data.models.all_projects.AllProjects;
import com.zalack.android.data.models.login.Login;
import com.zalack.android.data.models.MovieResponse;
import com.zalack.android.data.models.project_details.ProjectDetails;
import com.zalack.android.data.models.project_tickets.ProjectTickets;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ZalckService {

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @POST("login")
    Observable<Login> login(@Body HashMap<String, String> params);

    @GET("projects")
    @Headers({"Content-Type:application/json"})
    Observable<AllProjects> getAllProjects(@Header("Authorization") String token);

    @GET("tickets/{ticket_number}")
    Observable<ProjectTickets> getTickets(@Path("ticket_number") int number, @Header("Authorization") String token);

    @GET("projects/{project_id}")
    Observable<ProjectDetails> getProject(@Path("project_id") int number, @Header("Authorization") String token);

}
