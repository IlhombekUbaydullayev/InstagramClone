package com.example.instagramclone.managers.handler

import com.example.instagramclone.model.Post

interface DBPostHandler {
    fun onSuccess(post: Post)
    fun onError(e: Exception)
}