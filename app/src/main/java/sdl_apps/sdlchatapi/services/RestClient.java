package sdl_apps.sdlchatapi.services;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import sdl_apps.sdlchatapi.models.LeaveRecords;
import sdl_apps.sdlchatapi.models.Leaves;
import sdl_apps.sdlchatapi.models.User;

public interface RestClient {

    String LOGIN_URL = "/auth/rest/login/";

    String GET_USER_URL = "/auth/rest/user/";

    String GET_LEAVES_URL = "/api/leaves/";

    String GET_LEAVE_APPROVAL_URL = "/api/leaverecords?status=approved";

    String GET_LEAVE_RECORDS_URL = "/api/leaverecords/";

    String GET_PENDING_LEAVES_URL = "/api/leaverecords?status=pending";

    String FCM_REGISTRATION = "/api/devices/";

    String UPDATE_LEAVERECORDS = "/api/leaverecords/{id}/";

    @Multipart
    @POST(LOGIN_URL)
    Call<User> login(
            @Part("username") RequestBody email,
            @Part("password") RequestBody password);

    @GET(GET_USER_URL)
    Call<User> getUser();

    @GET(GET_LEAVE_APPROVAL_URL)
    Call<List<LeaveRecords>> getLeaveHistory();


    @GET(GET_LEAVES_URL)
    Call<List<Leaves>> getLeaves();

    @GET(GET_PENDING_LEAVES_URL)
    Call<List<LeaveRecords>> getPendingLeaves();


    @GET(GET_PENDING_LEAVES_URL)
    Call<List<LeaveRecords>> getApproved();

    @POST(GET_LEAVE_RECORDS_URL)
    @FormUrlEncoded
    Call<LeaveRecords> applyLeave(
            @Field("reason") String reason,
            @Field("to_date") String from,
            @Field("from_date") String to
    );

    @POST(FCM_REGISTRATION)
    @FormUrlEncoded
    Call<ResponseBody> registerFCM(
            @Field("name") String name,
            @Field("registration_id") String registration_id,
            @Field("device_id") String id,
            @Field("active") boolean active,
            @Field("type") String type
    );

    @PATCH(UPDATE_LEAVERECORDS)
    Call<LeaveRecords> approveLeave(
            @Body LeaveRecords leaveRecords,
            @Path( "id" ) int id
        );
}
