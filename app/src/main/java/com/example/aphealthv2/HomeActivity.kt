package com.example.aphealthv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.aphealthv2.network.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    lateinit var linearLayoutSpecialist: LinearLayout
    lateinit var linearLayoutDoctorDetail: LinearLayout
    lateinit var linearLayoutDoctors: LinearLayout
    lateinit var imageSpecialist: ImageView
    lateinit var tvSpecialist: TextView
    lateinit var image_item_specialist: ImageView
    //lateinit var linear_layout_doctor_page: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        linearLayoutSpecialist = findViewById(R.id.linear_layout_shop)
        linearLayoutDoctorDetail = findViewById(R.id.linear_layout_products)
        linearLayoutDoctors = findViewById(R.id.linear_layout_doctors)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        //linear_layout_doctor_page = findViewById(R.id.linear_layout_doctor_page)

        bottomNavigationView.selectedItemId = R.id.item_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    //val intent = Intent(this, HomeActivity::class.java)
                    //startActivity(intent)
                    true
                }
                R.id.item_doctor -> {
                    val intent = Intent(this, DoctorActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_appointment -> {
                    val intent = Intent(this, AppointmentActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_user -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }

        loadShop()
        loadProduct()
        loadDoctor()
    }

    fun loadProduct() {
        AphealthApi.retrofitService.getDoctors().enqueue(object : Callback<List<DoctorItem>?> {
            override fun onResponse(
                call: Call<List<DoctorItem>?>,
                response: Response<List<DoctorItem>?>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    for (Doctor in responseBody) {
                        val view = layoutInflater.inflate(R.layout.item_product, linearLayoutDoctorDetail, false)
                        val imageDoctor = view.findViewById<ImageView>(R.id.image_product)
                        val tvName = view.findViewById<TextView>(R.id.tv_name_product)
                        val tvExperience = view.findViewById<TextView>(R.id.tv_final_price_product)
                        val tvPatient = view.findViewById<TextView>(R.id.tv_price_product)

                        Glide.with(this@HomeActivity).load(Doctor.dImage).into(imageDoctor)
                        tvName.text = Doctor.dName

                        getSpecialist(1, view, Doctor.specialistId)

                        tvExperience.text = Doctor.experience.toString() + " years"
                        tvPatient.text = Doctor.numPatient.toString()

                        linearLayoutDoctorDetail.addView(view)
                    }
                }
            }

            override fun onFailure(call: Call<List<DoctorItem>?>, t: Throwable) {

            }
        })

    }

    fun loadShop() {

        AphealthApi.retrofitService.getSpecialists().enqueue(object : Callback<List<SpecialistItem>?> {
            override fun onResponse(
                call: Call<List<SpecialistItem>?>,
                response: Response<List<SpecialistItem>?>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    for (Specialist in responseBody){
                        val view = layoutInflater.inflate(R.layout.item_shop, linearLayoutSpecialist, false)
                        imageSpecialist = view.findViewById(R.id.image_item_shop)
                        tvSpecialist = view.findViewById(R.id.tv_item_name_shop)
                        image_item_specialist = view.findViewById(R.id.image_item_shop)

                        Glide.with(this@HomeActivity).load(Specialist.sImage).into(imageSpecialist)
                        tvSpecialist.text = Specialist.sName
                        image_item_specialist.setOnClickListener {
                            //Toast.makeText(this@HomeActivity, Specialist.specialistId.toString(), Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@HomeActivity, DoctorActivity::class.java)
                            intent.putExtra("specialistid", Specialist.specialistId)
                            intent.putExtra("isfiltered", true)
                            startActivity(intent)
                        }

                        linearLayoutSpecialist.addView(view)
                    }
                }
            }

            override fun onFailure(call: Call<List<SpecialistItem>?>, t: Throwable) {

            }
        })
    }

    fun loadDoctor() {
        AphealthApi.retrofitService.getDoctors().enqueue(object : Callback<List<DoctorItem>?> {
            override fun onResponse(
                call: Call<List<DoctorItem>?>,
                response: Response<List<DoctorItem>?>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    for (Doctor in responseBody) {
                        val view = layoutInflater.inflate(R.layout.item_doctor, linearLayoutDoctors, false)
                        val imageDoctor = view.findViewById<ImageView>(R.id.image_doctor)
                        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                        val tvNameDoctor = view.findViewById<TextView>(R.id.tv_name_doctor)
                        //val tvSpecialist = view.findViewById<TextView>(R.id.tv_specialist)
                        val tvClinic = view.findViewById<TextView>(R.id.tv_clinic)

                        Glide.with(this@HomeActivity).load(Doctor.dImage).into(imageDoctor)
                        tvNameDoctor.text = Doctor.dName
                        //tvSpecialist.text = Doctor.specialistId.toString()
                        getSpecialist(2, view, Doctor.specialistId)

                        tvClinic.text = Doctor.docLocation

                        linearLayoutDoctors.addView(view)
                    }

                }

            }

            override fun onFailure(call: Call<List<DoctorItem>?>, t: Throwable) {

            }
        })
    }

    fun getSpecialist(case: Int, view: View, id: Int) {
        AphealthApi.retrofitService.getSpecialistById(id).enqueue(object : Callback<SpecialistItem?> {
            override fun onResponse(
                call: Call<SpecialistItem?>,
                response: Response<SpecialistItem?>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {

                    if (case == 1) {
                        val tvSpecialist = view.findViewById<TextView>(R.id.tv_pills_product)
                        tvSpecialist.text = responseBody.sName
                    } else {
                        val tvSpecialist = view.findViewById<TextView>(R.id.tv_specialist)
                        tvSpecialist.text = responseBody.sName
                    }

                }
            }

            override fun onFailure(call: Call<SpecialistItem?>, t: Throwable) {

            }
        })
    }

}