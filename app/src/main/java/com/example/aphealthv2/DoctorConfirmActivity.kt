package com.example.aphealthv2

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat.format
import android.widget.*
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.example.aphealthv2.network.AphealthApi
import com.example.aphealthv2.network.AppointmentItem
import com.example.aphealthv2.request.PostAppointment
import com.example.aphealthv2.request.PostPatient
import com.example.aphealthv2.session.LoginPref
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.internal.bind.util.ISO8601Utils.format
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.http.HttpDate.format
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DoctorConfirmActivity : AppCompatActivity() {

    lateinit var btn_doctor_confirm: Button
    lateinit var linear_layout_doctor_confirm: LinearLayout
    lateinit var linear_layout_doctor_confirm_horizontal: LinearLayout
    lateinit var session: LoginPref
    lateinit var card_doctor_confirm: MaterialCardView

    val namesDoctorTime = arrayOf("10.10 am", "10.30 am", "10.50 am", "2.10 pm", "2.50 am")
    var date = ""
    var time = ""
    var patientId = ""
    var doctorId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_confirm)

        session = LoginPref(this)

        linear_layout_doctor_confirm = findViewById(R.id.linear_layout_doctor_confirm)
        linear_layout_doctor_confirm_horizontal = findViewById(R.id.linear_layout_doctor_confirm_horizontal)
        btn_doctor_confirm = findViewById(R.id.btn_doctor_confirm)
        doctorId = intent.getIntExtra("doctorid", 0).toString()

        session.checkLogin()
        var user: HashMap<String, String> = session.getUserDetails()
        patientId = user.get(LoginPref.KEY_PATIENTID).toString()

        val picker = findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()
        date = today.get(Calendar.DAY_OF_MONTH).toString() + "/" + today.get(Calendar.MONTH).toString() +
                "/" + today.get(Calendar.YEAR).toString()

        picker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)){
            view, year, month, day ->
            val month = month + 1
            date = "$day/$month/$year"
        }

        loadDoctorTime()

        btn_doctor_confirm.setOnClickListener {

            createAppointment()
        }


    }

    fun loadDoctorTime() {
        for (i in 0 .. namesDoctorTime.size - 1) {

            val view = layoutInflater.inflate(R.layout.item_doctor_time, linear_layout_doctor_confirm_horizontal, false)
            val card_doctor_confirm = view.findViewById<MaterialCardView>(R.id.card_doctor_confirm)
            val linear_layout_doctor_confirm_time = view.findViewById<LinearLayout>(R.id.linear_layout_doctor_confirm_time)
            val linear_layout_doctor_confirm_center = view.findViewById<LinearLayout>(R.id.linear_layout_doctor_confirm_center)
            val btn_name_doctor_confirm = view.findViewById<Button>(R.id.btn_name_doctor_confirm)

            btn_name_doctor_confirm.text = namesDoctorTime[i]

            linear_layout_doctor_confirm_horizontal.addView(view)

            btn_name_doctor_confirm.setOnClickListener {
                btn_name_doctor_confirm.setBackgroundColor(Color.rgb(0, 83, 156))
                time = btn_name_doctor_confirm.text.toString()
                Handler().postDelayed({
                    btn_name_doctor_confirm.setBackgroundColor(Color.WHITE)
                }, 100)
            }
        }

    }

    fun createAppointment() {
        val appointment = PostAppointment(date, time, patientId.toInt(), doctorId.toInt())

        AphealthApi.retrofitService.createAppointment(makeGSONRequestBody(appointment))
            .enqueue(object : Callback<AppointmentItem?> {
                override fun onResponse(
                    call: Call<AppointmentItem?>,
                    response: Response<AppointmentItem?>
                ) {
                    Toast.makeText(this@DoctorConfirmActivity, "Book Successfully",
                        Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<AppointmentItem?>, t: Throwable) {
                    Toast.makeText(this@DoctorConfirmActivity, "Book Unsuccessfully",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun makeGSONRequestBody(jsonObject: Any?): RequestBody {
        return RequestBody.create(
            MediaType.parse("multipart/form-data"),
            Gson().toJson(jsonObject))
    }

}