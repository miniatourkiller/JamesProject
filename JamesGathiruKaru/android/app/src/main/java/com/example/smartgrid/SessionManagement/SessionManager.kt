package com.example.smartgrid.SessionManagement

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject


class SessionManager(context: Context) {
    var sharedPreference: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    init {
        sharedPreference = context.getSharedPreferences("mySession", Context.MODE_PRIVATE)
        editor = sharedPreference?.edit()
    }

    fun saveSession(obj: JSONObject) {
        editor!!.putString("mydata", obj.toString())
        editor!!.apply()
    }

    fun getSession(): JSONObject? {
        val myData = sharedPreference!!.getString("mydata", "")
        var `object`: JSONObject? = null
        try {
            `object` = JSONObject(myData)
        } catch (e: Exception) {
            println(e.localizedMessage)
        }
        return `object`
    }

    fun removeSession() {
        editor!!.putString(null, "s").commit()
    }
}