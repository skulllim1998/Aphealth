package com.example.aphealthv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.aphealthv2.network.AphealthApi
import com.example.aphealthv2.network.PatientItem
import com.example.aphealthv2.request.PostPatient
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    var emailValidate: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btn_register_patient = findViewById<Button>(R.id.btn_register_patient)
        val tv_email_patient = findViewById<EditText>(R.id.tv_email_patient)
        val tv_password_patient = findViewById<EditText>(R.id.tv_password_patient)
        val tv_name_patient = findViewById<EditText>(R.id.tv_name_patient)
        val tv_phone_patient = findViewById<EditText>(R.id.tv_phone_patient)
        val tv_address_patient = findViewById<EditText>(R.id.tv_address_patient)

        btn_register_patient.setOnClickListener {
            val email = tv_email_patient.text.toString().trim()
            val password = tv_password_patient.text.toString().trim()
            val name = tv_name_patient.text.toString().trim()
            val phone = tv_phone_patient.text.toString().trim()
            val address = tv_address_patient.text.toString().trim()

            if (email.isEmpty()) {
                tv_email_patient.error = "Email required"
                tv_email_patient.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                tv_password_patient.error = "Password required"
                tv_password_patient.requestFocus()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                tv_name_patient.error = "Name required"
                tv_name_patient.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                tv_phone_patient.error = "Phone required"
                tv_phone_patient.requestFocus()
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                tv_address_patient.error = "Address required"
                tv_address_patient.requestFocus()
                return@setOnClickListener
            }

            val patient = PostPatient(
                email, password, name, phone.toInt(), address
            )

            checkEmail(email, makeGSONRequestBody(patient))

        }

    }

    fun createPatient(requestBody: RequestBody) {
        AphealthApi.retrofitService.createPatient(requestBody)
            .enqueue(object : Callback<PatientItem?> {
                override fun onResponse(
                    call: Call<PatientItem?>,
                    response: Response<PatientItem?>
                ) {
                    Toast.makeText(this@RegisterActivity, "Register Successfully", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<PatientItem?>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun checkEmail(email: String, requestBody: RequestBody) {
        var checkedEmail = ""
        AphealthApi.retrofitService.getPatients().enqueue(object : Callback<List<PatientItem>?> {
            override fun onResponse(
                call: Call<List<PatientItem>?>,
                response: Response<List<PatientItem>?>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    for (Patient in responseBody) {
                        if (Patient.email == email) {
                            //emailValidate = false
                            checkedEmail = Patient.email
                        }
                    }
                }
                if (checkedEmail != email) {
                    createPatient(requestBody)
                    Toast.makeText(this@RegisterActivity, "Register Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Email Already Exists", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<PatientItem>?>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })
        emailValidate = false
    }

    fun makeGSONRequestBody(jsonObject: Any?): RequestBody {
        return RequestBody.create(
            MediaType.parse("multipart/form-data"),
            Gson().toJson(jsonObject))
    }

}