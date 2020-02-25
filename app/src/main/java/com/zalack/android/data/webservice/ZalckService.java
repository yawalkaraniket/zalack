package com.zalack.android.data.webservice;

import com.zalack.android.data.models.add_project.AddedProject;
import com.zalack.android.data.models.all_projects.AllProjects;
import com.zalack.android.data.models.create_ticket.CreateTicket;
import com.zalack.android.data.models.delet_ticket.DeleteTicket;
import com.zalack.android.data.models.delete_project.DeleteProject;
import com.zalack.android.data.models.edit_project.EditProjectDetails;
import com.zalack.android.data.models.edit_task.EditTask;
import com.zalack.android.data.models.login.Login;
import com.zalack.android.data.models.MovieResponse;
import com.zalack.android.data.models.project_details.ProjectDetails;
import com.zalack.android.data.models.project_tickets.ProjectTickets;
import com.zalack.android.data.models.signup.SignUp;
import com.zalack.android.data.models.update_profile.ChangePassword;
import com.zalack.android.data.models.update_profile.UpdateProfile;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ZalckService {

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @POST("login")
    Observable<Login> login(@Body HashMap<String, String> params);

    @POST("register")
    Observable<SignUp> signUp(@Body HashMap<String, String> params);

    @POST("projects")
    Observable<AddedProject> addProject(@Header("Authorization") String token, @Body HashMap<String, String> params);

    @GET("projects")
    @Headers({"Content-Type:application/json"})
    Observable<AllProjects> getAllProjects(@Header("Authorization") String token);

    @GET("tickets/{ticket_number}")
    Observable<ProjectTickets> getTickets(@Path("ticket_number") int number, @Header("Authorization") String token);

    @PUT("tickets/{ticket_number}")
    Observable<EditTask> updateTicket(@Path("ticket_number") int number, @Header("Authorization") String token, @Body HashMap<String, String> params);

    @GET("projects/{project_id}")
    Observable<ProjectDetails> getProject(@Path("project_id") int number, @Header("Authorization") String token);

    @DELETE("projects/{project_id}")
    Observable<DeleteProject> deleteProject(@Path("project_id") int number, @Header("Authorization") String token);

    @PUT("projects/{project_id}")
    Observable<EditProjectDetails> editProject(@Path("project_id") int number, @Header("Authorization") String token, @Body HashMap<String, String> params);

    @POST("tickets")
    Observable<CreateTicket> createTicket(@Header("Authorization") String token, @Body HashMap<String, String> params);

    @DELETE("tickets/{ticket_id}")
    Observable<DeleteTicket> deleteTicket(@Path("ticket_id") int number, @Header("Authorization") String token);

    @PUT("update/profile")
    Observable<UpdateProfile> updateProfile(@Header("Authorization") String token, @Body HashMap<String, String> params);

    @PUT("update/profile")
    Observable<ChangePassword> changePassword(@Header("Authorization") String token, @Body HashMap<String, String> params);

}
