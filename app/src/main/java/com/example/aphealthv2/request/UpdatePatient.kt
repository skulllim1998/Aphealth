package com.example.aphealthv2.request

data class UpdatePatient(
    val password: String,
    val pName: String,
    val phone: Int,
    val address: String
)
