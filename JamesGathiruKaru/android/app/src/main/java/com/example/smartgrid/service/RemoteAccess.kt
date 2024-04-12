package com.example.smartgrid.service

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.smartgrid.SessionManagement.SessionManager
import com.example.smartgrid.Status
import org.json.JSONObject

class RemoteAccess (context: Context){
    private lateinit var que : RequestQueue
    private var context = context

    private lateinit var auth: JSONObject
    fun sendComponentStatus(componentName: String, url: String){
        val sessionManager =SessionManager(context)
        auth = sessionManager.getSession()!!

        que = Volley.newRequestQueue(context)
        val componentStatus =  JSONObject()
        componentStatus.put("componentName", componentName)

        val jsonObjectRequest = CustomJsonObjectRequest(Request.Method.POST, url, componentStatus,auth, "application/json",{ response->

        }, {
            Toast.makeText(context, "failed to update status", Toast.LENGTH_SHORT).show()
        })
        que.add(jsonObjectRequest)
    }
}