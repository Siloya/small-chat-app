package com.example.myapplication;

import com.example.myapplication.notification.Myresponse;
import com.example.myapplication.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface Apiservice {
    @Headers(
            {"Content-Type:application/json",
              "Authorization:key= AAAA1p7h8yc:APA91bHXsdKqY_96jgcLjIMor5LvY9FKXXCMXWe6M9wBBPYum-yuK207E4mFTyWLRe8v9ELj6m9oTAaXKdsTGUyrpSJYYO0sugYQ_nzEeCI-CVuBzY96g_2szWqK9RNJt8T6D84suRs1"
            }
    )
    @POST("fcm/send")
    Call<Myresponse> sendNotification(Sender body);

}
