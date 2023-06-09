package com.example.aphealthv2.network

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val BASE_URL = "http://aphealthapi2-dev.us-east-1.elasticbeanstalk.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiInterface {

    @GET("api/doctor")
    fun getDoctors(): Call<List<DoctorItem>>

    @GET("api/doctor/{id}")
    fun getDoctorById(@Path("id") id: Int): Call<DoctorItem>

    @GET("api/doctor/filter/{id}")
    fun getDoctorBySpecialistId(@Path("id") id: Int): Call<List<DoctorItem>>

    @GET("api/specialist")
    fun getSpecialists(): Call<List<SpecialistItem>>

    @GET("api/specialist/{id}")
    fun getSpecialistById(@Path("id") id: Int): Call<SpecialistItem>

    @GET("api/patient")
    fun getPatients(): Call<List<PatientItem>>

    @GET("api/patient/{id}")
    fun getPatientById(@Path("id") id: Int): Call<PatientItem>

    @Headers("Content-Type: application/json")
    @POST("api/patient")
    fun createPatient(@Body requestBody: RequestBody): Call<PatientItem>

    @Headers("Content-Type: application/json")
    @PUT("api/patient/{id}")
    fun updatePatient(@Path("id") id: Int, @Body requestBody: RequestBody): Call<PatientItem>

    @Headers("Content-Type: application/json")
    @POST("api/appointment")
    fun createAppointment(@Body requestBody: RequestBody): Call<AppointmentItem>

    @GET("api/appointment/{id}")
    fun getAppointmentByPatientId(@Path("id") id: Int): Call<List<AppointmentItem>>

    @DELETE("api/appointment/{id}")
    fun deleteAppointmentById(@Path("id") id: Int): Call<AppointmentItem>

}

object AphealthApi {
    val retrofitService : ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}