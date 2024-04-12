package com.example.smartgrid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smartgrid.SessionManagement.SessionManager
import org.json.JSONObject

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email =  findViewById<EditText>(R.id.editTextText)
        val password = findViewById<EditText>(R.id.editTextNumberPassword)
        val button = findViewById<Button>(R.id.button)

        val url = getString(R.string.url)
        val sessionManager = SessionManager(this)

        button.setOnClickListener{
            button.isActivated = false
            button.isClickable = false

            val json:JSONObject = JSONObject();
            json.put("username", email.text)
            json.put("password", password.text)

            val que = Volley.newRequestQueue(this)

            val response : JsonObjectRequest = JsonObjectRequest(Request.Method.POST,url+"/login",  json, {response->
               if(response.has("message")){
                   button.isActivated = true
                   button.isClickable = true
                   Toast.makeText(this,response.getString("message"), Toast.LENGTH_SHORT).show()
               }else{
                    sessionManager.saveSession(response)

                   val main = Intent(this, MainActivity::class.java)

                   startActivity(main)

               }
            },{
                button.isActivated = true
                button.isClickable = true
                Toast.makeText(this, "There was a problem sending the request", Toast.LENGTH_SHORT).show()
            })

            que.add(response)
        }
    }
}