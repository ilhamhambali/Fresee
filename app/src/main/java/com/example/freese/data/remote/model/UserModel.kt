package com.example.freese.data.remote.model

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)