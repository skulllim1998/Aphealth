package com.example.aphealthv2.network

data class DoctorItem(
    val doctorId: Int,
    val dName: String,
    val experience: Int,
    val numPatient: Int,
    val docLocation: String,
    val about: String,
    val dImage: String,
    val specialistId: Int
)