package com.example.aphealthv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.aphealthv2.network.AphealthApi
import com.example.aphealthv2.network.ApiInterface
import com.example.aphealthv2.network.DoctorItem
import com.example.aphealthv2.network.SpecialistItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorActivity : AppCompatActivity() {
    lateinit var linear_layout_doctor_page: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)

        linear_layout_doctor_page = findViewById(R.id.linear_layout_doctor_page)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_doctor)
        val specialistid = intent.getIntExtra("specialistid", 0)
        val isfiltered = intent.getBooleanExtra("isfiltered", false)

        bottomNavigationView.selectedItemId = R.id.item_doctor

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    val intent =Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_doctor -> {
                    //val intent = Intent(this, LoginActivity::class.java)
                    //startActivity(intent)
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

        if (isfiltered == true) {
            getDoctorBySpecialistId(specialistid)
        } else {
            loadDoctor()
        }


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
                        val view = layoutInflater.inflate(R.layout.item_product, linear_layout_doctor_page, false)
                        val cardProduct = view.findViewById<MaterialCardView>(R.id.card_product)
                        val imageDoctor = view.findViewById<ImageView>(R.id.image_product)
                        val tvName = view.findViewById<TextView>(R.id.tv_name_product)
                        val tvExperience = view.findViewById<TextView>(R.id.tv_final_price_product)
                        val tvPatient = view.findViewById<TextView>(R.id.tv_price_product)

                        Glide.with(this@DoctorActivity).load(Doctor.dImage).into(imageDoctor)
                        tvName.text = Doctor.dName

                        getSpecialist(view, Doctor.specialistId)

                        tvExperience.text = Doctor.experience.toString() + " years"
                        tvPatient.text = Doctor.numPatient.toString()

                        linear_layout_doctor_page.addView(view)
                        cardProduct.setOnClickListener {
                            val intent = Intent(this@DoctorActivity, DoctorSubActivity::class.java)
                            intent.putExtra("doctorid", Doctor.doctorId)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<DoctorItem>?>, t: Throwable) {

            }
        })

    }

    fun getDoctorBySpecialistId(id: Int) {
        AphealthApi.retrofitService.getDoctorBySpecialistId(id).enqueue(object : Callback<List<DoctorItem>?> {
            override fun onResponse(
                call: Call<List<DoctorItem>?>,
                response: Response<List<DoctorItem>?>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    for (Doctor in responseBody) {
                        val view = layoutInflater.inflate(R.layout.item_product, linear_layout_doctor_page, false)
                        val cardProduct = view.findViewById<MaterialCardView>(R.id.card_product)
                        val imageDoctor = view.findViewById<ImageView>(R.id.image_product)
                        val tvName = view.findViewById<TextView>(R.id.tv_name_product)
                        val tvExperience = view.findViewById<TextView>(R.id.tv_final_price_product)
                        val tvPatient = view.findViewById<TextView>(R.id.tv_price_product)

                        Glide.with(this@DoctorActivity).load(Doctor.dImage).into(imageDoctor)
                        tvName.text = Doctor.dName
                        getSpecialist(view, Doctor.specialistId)

                        tvExperience.text = Doctor.experience.toString() + " years"
                        tvPatient.text = Doctor.numPatient.toString()

                        linear_layout_doctor_page.addView(view)
                        cardProduct.setOnClickListener {
                            val intent = Intent(this@DoctorActivity, DoctorSubActivity::class.java)
                            intent.putExtra("doctorid", Doctor.doctorId)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<DoctorItem>?>, t: Throwable) {
                Toast.makeText(this@DoctorActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getSpecialist(view: View, id: Int) {
        AphealthApi.retrofitService.getSpecialistById(id).enqueue(object : Callback<SpecialistItem?> {
            override fun onResponse(
                call: Call<SpecialistItem?>,
                response: Response<SpecialistItem?>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    val tvSpecialist = view.findViewById<TextView>(R.id.tv_pills_product)
                    tvSpecialist.text = responseBody.sName
                }
            }

            override fun onFailure(call: Call<SpecialistItem?>, t: Throwable) {

            }
        })
    }

}