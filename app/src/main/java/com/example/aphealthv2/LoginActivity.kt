package com.example.aphealthv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.aphealthv2.network.AphealthApi
import com.example.aphealthv2.network.PatientItem
import com.example.aphealthv2.request.GetPatientRequest
import com.example.aphealthv2.session.LoginPref
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var session: LoginPref
    var isLoggedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = LoginPref(this)

        val img_login = findViewById<ImageView>(R.id.img_login)
        val tv_email_login = findViewById<EditText>(R.id.tv_email_login)
        val tv_password_login = findViewById<EditText>(R.id.tv_password_login)
        val btn_login = findViewById<Button>(R.id.btn_login)

        if (session.isLoggedIn()) {
            var i : Intent = Intent(this, HomeActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }

        btn_login.setOnClickListener {
            val email = tv_email_login.text.toString().trim()
            val password = tv_password_login.text.toString().trim()

            if (email.isEmpty()) {
                tv_email_login.error = "Email required"
                tv_email_login.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                tv_password_login.error = "Email required"
                tv_password_login.requestFocus()
                return@setOnClickListener
            }

            AphealthApi.retrofitService.getPatients().enqueue(object : Callback<List<PatientItem>?> {
                override fun onResponse(
                    call: Call<List<PatientItem>?>,
                    response: Response<List<PatientItem>?>
                ) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        for (Patient in responseBody) {
                            if (Patient.email == email && Patient.password == password) {
                                isLoggedIn = true
                                Toast.makeText(this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                                session.createLoginSession(Patient.patientId.toString(), email)
                                var i : Intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                        }
                    }

                    if (isLoggedIn == false) {
                        Toast.makeText(this@LoginActivity, "Incorrect Email or Password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<PatientItem>?>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })

        }

    }

}