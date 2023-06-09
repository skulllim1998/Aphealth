package com.example.aphealthv2.request

data class PostAppointment(
    val date: String,
    val time: String,
    val patientId: Int,
    val doctorId: Int
)
