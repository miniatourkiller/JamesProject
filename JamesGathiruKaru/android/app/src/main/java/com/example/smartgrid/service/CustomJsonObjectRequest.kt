package com.example.smartgrid.service

import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class CustomJsonObjectRequest(method: Int,
                              url: String,
                              body: JSONObject,
                              private val obj: JSONObject,
                              private val contentType: String,
                              response: Response.Listener<JSONObject>,
                              error: Response.ErrorListener)
    :JsonObjectRequest(method, url, body, response, error) {
    override fun getBodyContentType(): String {
        return contentType
    }

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String, String>()
        if (obj != null) {
            headers["Authorization"] = "Bearer "+obj.get("token")
        }
        // Add more headers if needed
        return headers
    }
//    override fun getBody(): ByteArray {
//        return body.toString().toByteArray()
//    }
}