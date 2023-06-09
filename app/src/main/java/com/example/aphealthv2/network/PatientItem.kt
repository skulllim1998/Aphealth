package com.example.aphealthv2.network

data class PatientItem(
    val patientId: Int,
    val email: String,
    val password: String,
    val pName: String,
    val phone: Int,
    val address: String
)