package com.example.instagramclone.network.service

import com.example.instagramclone.model.FCMNote
import com.example.instagramclone.model.FCMResp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface NoteService {
    companion object {
        private const val SERVER_KEY =
            "AAAAuauOQTw:APA91bGfVZYPj74Ic3eGy0t37AUzcC8ApZVrKctQJdjPEooYP6sO5UKzV4GH8p-p9ecAlWDOkvDJhW1rfEMuEJcyl25Mu_yC4S6Lq-uvwVCaeK0nGU6WWa8lcgPt1UEw6B8pl3JBGarU"
    }

    @Headers("Authorization:key=$SERVER_KEY")
    @POST("/fcm/send")
    fun sendNote(@Body fcmNote: FCMNote): Call<FCMResp>
}