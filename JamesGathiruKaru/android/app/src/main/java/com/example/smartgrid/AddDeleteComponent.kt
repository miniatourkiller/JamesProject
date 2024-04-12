package com.example.smartgrid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smartgrid.service.RemoteAccess
import org.json.JSONObject

class AddDeleteComponent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_delete_component)

        val back = findViewById<Button>(R.id.button7)
        val create = findViewById<Button>(R.id.button8)
        val delete = findViewById<Button>(R.id.button9)
        val componentName = findViewById<EditText>(R.id.editTextText2)

        back.setOnClickListener {
            val intent = Intent(this, Status::class.java)
            startActivity(intent)
        }

        var url = getString(R.string.url)
        val remoteAccess = RemoteAccess(this)

        create.setOnClickListener {
            if(componentName.text.length<= 0){
                Toast.makeText(this, "Enter a value", Toast.LENGTH_SHORT).show()
            }
            url += "/addOrUpdateComponentStatus"
            val name :String = componentName.text.toString()

            remoteAccess.sendComponentStatus(name, url)
            Toast.makeText(this, name+" created", Toast.LENGTH_SHORT).show()
            componentName.text = null
        }
        delete.setOnClickListener {
            if(componentName.text.length<= 0){
                Toast.makeText(this, "Enter a value", Toast.LENGTH_SHORT).show()
            }
            url += "/removeComponent"

            val name :String = componentName.text.toString()
            remoteAccess.sendComponentStatus(name, url)
            Toast.makeText(this, name+" deleted", Toast.LENGTH_SHORT).show()
            componentName.text = null
        }
    }
}