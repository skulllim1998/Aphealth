package com.example.aphealthv2.session

import android.accounts.AccountManager.KEY_PASSWORD
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.aphealthv2.LoginActivity
import com.example.aphealthv2.MainActivity

class LoginPref {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con: Context
    var PRIVATEMODE: Int = 0

    constructor(con: Context) {
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME, PRIVATEMODE)
        editor = pref.edit()
    }

    companion object {
        val PREF_NAME = "Login_Preference"
        val IS_LOGIN = "isLoggedin"
        val KEY_PATIENTID = "patientid"
        val KEY_EMAIL = "email"
    }

    fun createLoginSession(patientid: String, email: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_PATIENTID, patientid)
        editor.putString(KEY_EMAIL, email)
        editor.commit()
    }

    fun checkLogin() {
        if (!this.isLoggedIn()) {
            var i : Intent = Intent(con, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)
        }
    }

    fun getUserDetails(): HashMap<String, String> {
        var user: Map<String, String> = HashMap<String, String>()
        (user as HashMap).put(KEY_PATIENTID, pref.getString(KEY_PATIENTID, null)!!)
        (user as HashMap).put(KEY_EMAIL, pref.getString(KEY_EMAIL, null)!!)
        return user
    }

    fun LogoutUser() {
        editor.clear()
        editor.commit()
        var i : Intent = Intent(con, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        con.startActivity(i)
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

}