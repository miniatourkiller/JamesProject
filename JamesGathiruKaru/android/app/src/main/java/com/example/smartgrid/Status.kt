package com.example.smartgrid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smartgrid.SessionManagement.SessionManager
import com.example.smartgrid.dto.ComponentStatus
import com.example.smartgrid.dto.Message
import com.example.smartgrid.service.CustomArrayAdapter
import com.example.smartgrid.service.CustomJsonArrayRequest
import com.example.smartgrid.service.CustomJsonObjectRequest
import io.socket.client.IO
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Status : AppCompatActivity() {

    private lateinit var customArrayAdapter: CustomArrayAdapter
    private lateinit var que: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        val socketUrl = getString(R.string.socketurl)
        val sessionManager = SessionManager(this)

        val auth = sessionManager.getSession()

        val mSocket = IO.socket(socketUrl)
        mSocket?.connect()
        val message = JSONObject()
        message.put("from", auth!!.getString("username")+"status")

        mSocket.emit("fromClient", message)



        que = Volley.newRequestQueue(this)

        val back = findViewById<Button>(R.id.button4)
        val manageComponents = findViewById<Button>(R.id.button6)

        manageComponents.setOnClickListener {
            val intent = Intent(this, AddDeleteComponent::class.java)
            startActivity(intent)
        }

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }

        val url = getString(R.string.url)

        val listView = findViewById<ListView>(R.id.listView2)

        fun addList(){
            val arr = JSONArray()
            val customJsonArrayRequest = CustomJsonArrayRequest(Request.Method.GET, url+ "/getComponents", arr,auth!!,{response->
                val componentStatusList = ArrayList<ComponentStatus>()
                for(i in 0 until(response.length())){
                    val componentStatus = ComponentStatus()
                    val jsonObject = JSONObject(response.getString(i))
                    componentStatus.isStatus = jsonObject.getBoolean("status")
                    componentStatus.id = jsonObject.getInt("id")
                    componentStatus.componentName = jsonObject.getString("componentName")
                    componentStatusList.add(componentStatus)
                }

                //view list inflation
                customArrayAdapter = CustomArrayAdapter(this, componentStatusList)

                listView.adapter = customArrayAdapter

            }, {
                Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show()
            })

            que.add(customJsonArrayRequest)
        };

        val onNewMessage =
            Emitter.Listener { args ->
                this.runOnUiThread(Runnable {
                    addList();
                })
            }

        mSocket?.on(auth.getString("username")+"status", onNewMessage)

    }

}
