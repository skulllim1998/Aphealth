package com.example.aphealthv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import com.example.aphealthv2.network.AphealthApi
import com.example.aphealthv2.network.AppointmentItem
import com.example.aphealthv2.network.DoctorItem
import com.example.aphealthv2.network.SpecialistItem
import com.example.aphealthv2.session.LoginPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class AppointmentActivity : AppCompatActivity() {

    lateinit var linear_layout_appointment : LinearLayout
    lateinit var bottom_navigation_appointment : BottomNavigationView
    lateinit var session: LoginPref
    var patientId = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        session = LoginPref(this)

        linear_layout_appointment = findViewById(R.id.linear_layout_appointment)
        bottom_navigation_appointment = findViewById(R.id.bottom_navigation_appointment)

        session.checkLogin()
        var user: HashMap<String, String> = session.getUserDetails()
        patientId = user.get(LoginPref.KEY_PATIENTID).toString()

        bottom_navigation_appointment.selectedItemId = R.id.item_appointment

        bottom_navigation_appointment.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_doctor -> {
                    val intent = Intent(this, DoctorActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_appointment -> {
                    //val intent = Intent(this, AppointmentActivity::class.java)
                    //startActivity(intent)
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

        loadAppointment(patientId.toInt())
    }

    fun loadAppointment(id: Int) {

        AphealthApi.retrofitService.getAppointmentByPatientId(id).enqueue(object : Callback<List<AppointmentItem>?> {
            override fun onResponse(
                call: Call<List<AppointmentItem>?>,
                response: Response<List<AppointmentItem>?>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    for (Appointment in responseBody) {
                        val view = layoutInflater.inflate(R.layout.item_appointment, linear_layout_appointment,
                            false)
                        val card_appointment = view.findViewById<MaterialCardView>(R.id.card_appointment)
                        val tv_appointment_date = view.findViewById<TextView>(R.id.tv_appointment_date)
                        val tv_appointment_time = view.findViewById<TextView>(R.id.tv_appointment_time)
                        val btn_cancel = view.findViewById<Button>(R.id.btn_cancel)

                        tv_appointment_date.text = "Date\n" + Appointment.date
                        tv_appointment_time.text = "Time\n" + Appointment.time

                        getDoctor(view, Appointment.doctorId)

                        btn_cancel.setOnClickListener {
                            deleteAppointment(Appointment.appointmentId)
                            Handler().postDelayed({
                                val intent = Intent(this@AppointmentActivity, AppointmentActivity::class.java)
                                startActivity(intent)
                            }, 1000)
                        }

                        linear_layout_appointment.addView(view)

                    }
                }
            }

            override fun onFailure(call: Call<List<AppointmentItem>?>, t: Throwable) {

            }
        })

    }

    fun getDoctor(view: View, id : Int) {

        AphealthApi.retrofitService.getDoctorById(id).enqueue(object : Callback<DoctorItem?> {
            override fun onResponse(call: Call<DoctorItem?>, response: Response<DoctorItem?>) {

                val responseBody = response.body()

                if (responseBody != null) {
                    val tv_appointment_doctor = view.findViewById<TextView>(R.id.tv_appointment_doctor)
                    val tv_appointment_location = view.findViewById<TextView>(R.id.tv_appointment_location)

                    getSpecialist(view, responseBody.specialistId)

                    tv_appointment_doctor.text = "Doctor\n" + responseBody.dName
                    tv_appointment_location.text = "Place\n" + responseBody.docLocation
                }
            }

            override fun onFailure(call: Call<DoctorItem?>, t: Throwable) {

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
                    val tv_appointment_specialist = view.findViewById<TextView>(R.id.tv_appointment_specialist)
                    tv_appointment_specialist.text = "Type\n" + responseBody.sName
                }
            }

            override fun onFailure(call: Call<SpecialistItem?>, t: Throwable) {

            }
        })
    }

    fun deleteAppointment(id: Int) {
        AphealthApi.retrofitService.deleteAppointmentById(id).enqueue(object : Callback<AppointmentItem?> {
            override fun onResponse(
                call: Call<AppointmentItem?>,
                response: Response<AppointmentItem?>
            ) {
                Toast.makeText(this@AppointmentActivity, "Delete Successfully", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<AppointmentItem?>, t: Throwable) {
                Toast.makeText(this@AppointmentActivity, "Delete Unsuccessfully", Toast.LENGTH_SHORT).show()
            }
        })
    }

}