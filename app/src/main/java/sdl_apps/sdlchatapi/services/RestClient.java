package sdl_apps.sdlchatapi.services;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import sdl_apps.sdlchatapi.models.LeaveRecords;
import sdl_apps.sdlchatapi.models.Leaves;
import sdl_apps.sdlchatapi.models.User;

public interface RestClient {

    public static String LOGIN_URL = "/auth/rest/login/";

    public static String GET_USER_URL = "/auth/rest/user/";

    public static String GET_LEAVES_URL = "/api/leaves/";

    public static String GET_LEAVE_RECORDS_URL = "/api/leaverecords/";

    public static String GET_PENDING_LEAVES_URL = "/api/leaverecords?status=pending/";

    @Multipart
    @POST(LOGIN_URL)
    Call<User> login(
            @Part("username") RequestBody email,
            @Part("password") RequestBody password);

    @GET(GET_USER_URL)
    Call<User> getUser(
            @Header("Authorization") String token
    );

    @GET(GET_LEAVE_RECORDS_URL)
    Call<List<LeaveRecords>> getLeaveRecords(
            @Header("Authorization") String token
    );


    @GET(GET_LEAVES_URL)
    Call<List<Leaves>> getLeaves(
            @Header("Authorization") String token
    );

    @GET(GET_PENDING_LEAVES_URL)
    Call<List<LeaveRecords>> getPendingLeaves(
            @Header("Authorization") String token
    );

}
