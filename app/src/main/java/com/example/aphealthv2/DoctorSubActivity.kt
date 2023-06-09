package com.example.aphealthv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.aphealthv2.network.AphealthApi
import com.example.aphealthv2.network.DoctorItem
import com.example.aphealthv2.network.SpecialistItem
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorSubActivity : AppCompatActivity() {

    lateinit var image_btn_arrow_doctor_sub : ImageButton
    lateinit var image_doctor_sub : ImageView
    //lateinit var tv_id_doctor_sub : TextView
    lateinit var tv_name_doctor_sub : TextView
    lateinit var tv_specialist_sub : TextView
    lateinit var tv_location_sub : TextView
    lateinit var tv_about_sub : TextView
    lateinit var tv_patient_sub : TextView
    lateinit var tv_experience_sub : TextView
    lateinit var btn_add_to_basket_sub : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_sub)

        loadDoctorDetail()

    }

    fun loadDoctorDetail() {
        val doctorid = intent.getIntExtra("doctorid", 0)

        if (doctorid != null) {
            AphealthApi.retrofitService.getDoctorById(doctorid).enqueue(object : Callback<DoctorItem?> {
                override fun onResponse(call: Call<DoctorItem?>, response: Response<DoctorItem?>) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        image_btn_arrow_doctor_sub = findViewById(R.id.image_btn_arrow_doctor_sub)
                        image_doctor_sub = findViewById(R.id.image_doctor_sub)
                        tv_name_doctor_sub = findViewById(R.id.tv_name_doctor_sub)
                        //tv_specialist_sub = findViewById(R.id.tv_specialist_sub)
                        tv_location_sub = findViewById(R.id.tv_location_sub)
                        tv_about_sub = findViewById(R.id.tv_about_sub)
                        tv_patient_sub = findViewById(R.id.tv_patient_sub)
                        tv_experience_sub = findViewById(R.id.tv_experience_sub)
                        btn_add_to_basket_sub = findViewById(R.id.btn_add_to_basket_sub)

                        Glide.with(this@DoctorSubActivity).load(responseBody.dImage).into(image_doctor_sub)
                        //tv_id_doctor_sub.text = responseBody.doctorId.toString()
                        tv_name_doctor_sub.text = responseBody.dName
                        //tv_specialist_sub.text = responseBody.specialistId.toString()
                        getSpecialist(responseBody.specialistId)

                        tv_location_sub.text = responseBody.docLocation
                        tv_about_sub.text = responseBody.about
                        tv_patient_sub.text = "Patients\n" + responseBody.numPatient.toString()
                        tv_experience_sub.text = "Experience\n" + responseBody.experience.toString() + " years"

                        image_btn_arrow_doctor_sub.setOnClickListener {
                            val intent = Intent(this@DoctorSubActivity, DoctorActivity::class.java)
                            startActivity(intent)
                        }

                        btn_add_to_basket_sub.setOnClickListener {
                            val intent = Intent(this@DoctorSubActivity, DoctorConfirmActivity::class.java)
                            intent.putExtra("doctorid", doctorid)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<DoctorItem?>, t: Throwable) {
                    Toast.makeText(this@DoctorSubActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun getSpecialist(id: Int) {
        AphealthApi.retrofitService.getSpecialistById(id).enqueue(object : Callback<SpecialistItem?> {
            override fun onResponse(
                call: Call<SpecialistItem?>,
                response: Response<SpecialistItem?>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    tv_specialist_sub = findViewById(R.id.tv_specialist_sub)
                    tv_specialist_sub.text = responseBody.sName
                }
            }

            override fun onFailure(call: Call<SpecialistItem?>, t: Throwable) {

            }
        })
    }

}