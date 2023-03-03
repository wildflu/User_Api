package com.example.Model

import kotlinx.serialization.Serializable

@Serializable
data class UserRespond<T>(
    val status:Boolean,
    val data:T
)
