package com.example.instagramclone.managers.handler

import com.example.instagramclone.model.Post

interface DBPostsHandler {
    fun onSuccess(posts: ArrayList<Post>)
    fun onError(e: Exception)
}