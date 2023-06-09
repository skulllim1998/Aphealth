package com.example.aphealthv2.network

data class AppointmentItem(
    val appointmentId: Int,
    val date: String,
    val time: String,
    val patientId: Int,
    val doctorId: Int
)
