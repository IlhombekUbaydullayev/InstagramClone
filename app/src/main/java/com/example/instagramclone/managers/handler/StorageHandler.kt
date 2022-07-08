package com.example.instagramclone.managers.handler

interface StorageHandler {
    fun onSuccess(imgUrl: String)
    fun onError(exception: Exception?)
}