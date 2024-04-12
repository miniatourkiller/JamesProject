package com.example.smartgrid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartgrid.SessionManagement.SessionManager
import com.example.smartgrid.service.SimpleAdaper
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private var mSocket: Socket? = null
    private var myRecord: Any? = null
    private lateinit var text: TextView
    private lateinit var listView: ListView
    private lateinit var myComponents: ArrayList<String>
    private lateinit var arrayAdapter: SimpleAdaper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val controls = findViewById<Button>(R.id.button5)

        controls.setOnClickListener {
            val intent = Intent(this, Status::class.java)
            startActivity(intent)
        }


        val sessionManager = SessionManager(this)

        val auth = sessionManager.getSession()

        text = findViewById<TextView>(R.id.text1)

        if (auth != null) {
            text.text = ""+auth.get("username")
        }

        val socketUrl = getString(R.string.socketurl)

        mSocket = IO.socket(socketUrl)
        mSocket?.connect()
        mSocket?.on(auth?.get("username").toString(), onNewMessage)


        val history = findViewById<Button>(R.id.button2)
        listView = findViewById(R.id.listView)

        history.setOnClickListener{
            val history = Intent(this, History::class.java)

            startActivity(history)
        }



    }
    private val onNewMessage =
        Emitter.Listener { args ->
            this.runOnUiThread(Runnable {
                val data = args[0] as JSONObject
                var component: String
                val componentsArray:  JSONArray
                myComponents = ArrayList()
                try {
                    componentsArray = JSONArray(data.getString("components"))
                        // put them in my array list to display as simple array adaper
                    for(i in 0 until componentsArray.length()){
                        val comp = JSONObject(componentsArray.getString(i))
                        component = ""+comp.get("name")+": Usage Value "+comp.get("usageValue")+"W"
                        myComponents.add(component)
                    }

                    myRecord = data
                } catch (e: JSONException) {
                    return@Runnable
                }

                // add the message to view
                arrayAdapter = SimpleAdaper(this, myComponents)
                listView.adapter = arrayAdapter
            })
        }


    }