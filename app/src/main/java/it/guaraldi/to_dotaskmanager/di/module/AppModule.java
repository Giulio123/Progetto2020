package it.guaraldi.to_dotaskmanager.di.module;

import android.accounts.AccountManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.room.Room;
import com.google.firebase.auth.FirebaseAuth;


import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.data.local.TaskDatabase;
import it.guaraldi.to_dotaskmanager.data.local.LocalDataSource;
import it.guaraldi.to_dotaskmanager.data.local.dao.TaskDao;
import it.guaraldi.to_dotaskmanager.data.remote.RemoteDataSource;

import it.guaraldi.to_dotaskmanager.util.AppExecutors;
import it.guaraldi.to_dotaskmanager.util.SystemServices;
import it.guaraldi.to_dotaskmanager.util.UtilAccounts;
import it.guaraldi.to_dotaskmanager.utils.Constants;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    private final Application application;
    private static final String CATEGORY_SHARED = "CATEGORY";
    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public FirebaseAuth providesFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }


    @Singleton
    @Provides
    public TaskDatabase providesRoomDataSource() {
        return Room.databaseBuilder(application, TaskDatabase.class, "task_database").fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    public SharedPreferences providesCategorySharedPreferences(){
        return application.getSharedPreferences(CATEGORY_SHARED,Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    public TaskDao providesTaskDao(TaskDatabase localDataSource) {
        return localDataSource.getTaskDao();
    }

    @Singleton
    @Provides
    public AppExecutors providesAppExecutors(){
        return new AppExecutors();
    }

    @Singleton
    @Provides
    public LocalDataSource providesLocalDataSource(AppExecutors executors,TaskDao dao){
        return new LocalDataSource(executors,dao);
    }

    @Singleton
    @Provides
    public NotificationManager providesNotificationManager(){
        return (NotificationManager)application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Singleton
    @Provides
    public TasksRepository providesTasksRepository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource,SharedPreferences preferences){
        return new TasksRepository(localDataSource,remoteDataSource,preferences);
    }

    @Provides
    public Context providesAppContext() {
        return application;
    }



    private OkHttpClient getOkHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();
                        HttpUrl mUrl = originalHttpUrl.newBuilder()
                                .addQueryParameter(Constants.NAME_KEY_API_NEWS, Constants.VALUE_KEY_API_NEWS)
                                .addQueryParameter(Constants.NAME_COUNTRY_API_NEWS, Constants.VALUE_COUNTRY_API_NEWS)
                                .build();
                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .url(mUrl)
                                .build();
                        okhttp3.Response response = chain.proceed(request);
                        response.cacheResponse();
                        return response;
                    }
                })
                .readTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }


    @Provides
    @Singleton
    public RemoteDataSource providesRemoteDataSource( AppExecutors appExecutors, FirebaseAuth auth) {
        return new RemoteDataSource(appExecutors, auth);
    }
}
