package com.example.instagramclone.model

data class FCMResp(
    val canonical_ids: Int,
    val failure: Int,
    val multicast_id: Int,
    val results: List<Result>,
    val success: Int
)

data class Result(
    val message_Id: String
)