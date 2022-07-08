package com.example.instagramclone.managers.handler

import com.example.instagramclone.model.User


interface DBUsersHandler {
    fun onSuccess(user: ArrayList<User>)
    fun onError(e: Exception)
}