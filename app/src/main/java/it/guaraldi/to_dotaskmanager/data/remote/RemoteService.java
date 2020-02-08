package it.guaraldi.to_dotaskmanager.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RemoteService {
    @GET("connected")
    Call<Msg> getConnected();
}
