package com.example.aphealthv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.aphealthv2.network.AphealthApi
import com.example.aphealthv2.network.PatientItem
import com.example.aphealthv2.request.UpdatePatient
import com.example.aphealthv2.session.LoginPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    lateinit var linear_layout_profile: LinearLayout
    lateinit var img_profile: ImageView
    lateinit var tv_email_profile: TextView
    lateinit var tv_password_profile: EditText
    lateinit var tv_name_profile: EditText
    lateinit var tv_phone_profile: EditText
    lateinit var tv_address_profile: EditText
    lateinit var btn_update_profile: Button
    lateinit var bottom_navigation_profile: BottomNavigationView
    lateinit var btn_logout_profile: Button
    lateinit var session: LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        session = LoginPref(this)

        linear_layout_profile = findViewById(R.id.linear_layout_profile)
        img_profile = findViewById(R.id.img_profile)
        tv_email_profile = findViewById(R.id.tv_email_profile)
        tv_password_profile = findViewById(R.id.tv_password_profile)
        tv_name_profile = findViewById(R.id.tv_name_profile)
        tv_phone_profile = findViewById(R.id.tv_phone_profile)
        tv_address_profile = findViewById(R.id.tv_address_profile)
        btn_update_profile = findViewById(R.id.btn_update_profile)
        bottom_navigation_profile = findViewById(R.id.bottom_navigation_profile)
        btn_logout_profile = findViewById(R.id.btn_logout_profile)

        session.checkLogin()

        var user: HashMap<String, String> = session.getUserDetails()

        var patientid = user.get(LoginPref.KEY_PATIENTID)
        var email = user.get(LoginPref.KEY_EMAIL)

        tv_email_profile.setText(email)

        btn_update_profile.setOnClickListener {
            val password = tv_password_profile.text.toString().trim()
            val name = tv_name_profile.text.toString().trim()
            val phone = tv_phone_profile.text.toString().trim()
            val address = tv_address_profile.text.toString().trim()

            if (password.isEmpty()) {
                tv_password_profile.error = "Password required"
                tv_password_profile.requestFocus()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                tv_name_profile.error = "Name required"
                tv_name_profile.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                tv_phone_profile.error = "Phone required"
                tv_phone_profile.requestFocus()
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                tv_address_profile.error = "Address required"
                tv_address_profile.requestFocus()
                return@setOnClickListener
            }

            val patient = UpdatePatient(password, name, phone.toInt(), address)

            if (patientid != null) {
                updatePatient(patientid.toInt(), makeGSONRequestBody(patient))
            }
        }

        btn_logout_profile.setOnClickListener {
            session.LogoutUser()
        }

        if (patientid != null) {
            getPatient(patientid.toInt())
        }

        bottom_navigation_profile.selectedItemId = R.id.item_user

        bottom_navigation_profile.setOnItemSelectedListener { item ->
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
                    val intent = Intent(this, AppointmentActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_user -> {
                    //val intent = Intent(this, ProfileActivity::class.java)
                    //startActivity(intent)
                    true
                }
                else -> false
            }
        }



    }

    fun updatePatient(id: Int, requestBody: RequestBody) {
        AphealthApi.retrofitService.updatePatient(id, requestBody).enqueue(object : Callback<PatientItem?> {
            override fun onResponse(call: Call<PatientItem?>, response: Response<PatientItem?>) {
                Toast.makeText(this@ProfileActivity, "Update Successfully", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<PatientItem?>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Update Unsuccessfully", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getPatient(id: Int) {
        AphealthApi.retrofitService.getPatientById(id).enqueue(object : Callback<PatientItem?> {
            override fun onResponse(call: Call<PatientItem?>, response: Response<PatientItem?>) {
                val responseBody = response.body()

                if (responseBody != null) {
                    tv_email_profile = findViewById(R.id.tv_email_profile)
                    tv_password_profile = findViewById(R.id.tv_password_profile)
                    tv_name_profile = findViewById(R.id.tv_name_profile)
                    tv_phone_profile = findViewById(R.id.tv_phone_profile)
                    tv_address_profile = findViewById(R.id.tv_address_profile)

                    tv_email_profile.text = responseBody.email
                    tv_password_profile.setText(responseBody.password)
                    tv_name_profile.setText(responseBody.pName)
                    tv_phone_profile.setText(responseBody.phone.toString())
                    tv_address_profile.setText(responseBody.address)
                }
            }

            override fun onFailure(call: Call<PatientItem?>, t: Throwable) {

            }
        })
    }

    fun makeGSONRequestBody(jsonObject: Any?): RequestBody {
        return RequestBody.create(
            MediaType.parse("multipart/form-data"),
            Gson().toJson(jsonObject))
    }

}