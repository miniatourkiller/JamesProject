package com.example.smartgrid

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.smartgrid.SessionManagement.SessionManager
import com.example.smartgrid.dto.HistoryDto
import com.example.smartgrid.service.CustomArrayListAdapter2
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.Calendar

class History : AppCompatActivity() {
    private lateinit var myDatePicker: TextView
    private lateinit var searchBtn: Button
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private lateinit var myList: ListView
    private var datePickerDialog: DatePickerDialog.OnDateSetListener? = null
    private lateinit var arrayAdapter: CustomArrayListAdapter2
    private var myHistory : ArrayList<Any>? = null
    private var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val sessionManager = SessionManager(this)
        val auth = sessionManager.getSession()

//        Toast.makeText(this, result, Toast.LENGTH_LONG).show()


        myDatePicker = findViewById(R.id.datepicker)
        searchBtn = findViewById(R.id.search_btn)
        myList = findViewById(R.id.myList)


        //showing the date picker dialog
        myDatePicker.setOnClickListener{
            val calendar = Calendar.getInstance()
            if(year == 0){
                year = calendar.get(Calendar.YEAR)
                month = calendar.get(Calendar.MONTH)
                day = calendar.get(Calendar.DAY_OF_MONTH)
            }

            val dialog = DatePickerDialog(this, R.style.CustomDatePicker, datePickerDialog,year,month,day )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        datePickerDialog = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            var newday :String
            var newMonth :String
            var myMonth: Int
            myMonth = month+ 1
            this.year = year
            this.month = month
            this.day = day
            if(day<10){
                newday = "0"+day
            }else{
                newday = ""+day
            }
            if(myMonth<10){
                newMonth = "0"+myMonth
            }else{
                newMonth = ""+myMonth
            }
            this.date ="month:"+ newMonth + " day: "+newday+" year:"+year

            myDatePicker.text = date

            this.date = newday+"-"+newMonth+"-"+year
        }


        //where the volley begins
        class CustomJsonArrayRequest(
            method: Int,
            url: String,
            obj: JSONArray,
            private val body: JSONObject,
            private val contentType: String?,
            listener: Response.Listener<JSONArray>,
            errorListener: Response.ErrorListener
        ) : JsonArrayRequest(method, url, obj,listener, errorListener) {

            // Override getHeaders() method to provide custom headers
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                if (auth != null) {
                    headers["Authorization"] = "Bearer "+auth.get("token")
                }
                // Add more headers if needed
                return headers
            }
            override fun getBodyContentType(): String? {
                return contentType
            }
            override fun getBody(): ByteArray? {
                return body.toString()?.toByteArray()
            }
        }


        val que = Volley.newRequestQueue(this)
        val url:String =getString(R.string.url)+"/getHistory"
        val jsonObject = JSONObject()


        val jsonArray = JSONArray()

        searchBtn.setOnClickListener {
            if(myDatePicker.text == "Select Date"){
                Toast.makeText(this, "Select Date!", Toast.LENGTH_SHORT).show()
            }else{
                jsonObject.put("date", this.date)
                val jsonObjectRequest = CustomJsonArrayRequest(Request.Method.POST,url, jsonArray,jsonObject,"application/json",{response ->

                    fun JSONArray.toArrayList(): ArrayList<Any> {
                        val arrayList = ArrayList<Any>()
                        for (i in 0 until length()) {
                            arrayList.add(get(i))
                        }
                        return arrayList
                    }


                    var mynewList = ArrayList<HistoryDto>()


                    for(i in 0 until(response.length())){

                        var dateString = "";
                        var readings ="";

                        val historyDto = HistoryDto()


                        var obj = JSONObject(response.getString(i))
                        dateString = ""+obj.get("date")+" "+obj.get("timeStamp")
                        historyDto.timeStamp = dateString

                        var arrObj = obj.getJSONArray("components")
                        for(i in 0 until(arrObj.length())){
                            var obj = JSONObject(arrObj.getString(i))
                            if(i == 0){
                                readings = ""+obj.get("name")+": Readings "+obj.get("usageValue")+"W"
                            }else{
                                readings += "\n"+obj.get("name")+": Readings "+obj.get("usageValue")+"W"
                            }
                        }
                        historyDto.readings = readings
                        mynewList.add(historyDto)
                    }

                    arrayAdapter = CustomArrayListAdapter2(this, mynewList)
//                    Toast.makeText(this, myHistory.toString(), Toast.LENGTH_LONG).show()

                    myList.adapter = arrayAdapter
                } ,{
                    Toast.makeText(this, "The Get request failed", Toast.LENGTH_SHORT).show()
                })

                que.add(jsonObjectRequest)
            }
        }
    }
}