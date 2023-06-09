package com.example.aphealthv2.request

data class PostPatient(
    val email: String,
    val password: String,
    val pName: String,
    val phone: Int,
    val address: String
)
