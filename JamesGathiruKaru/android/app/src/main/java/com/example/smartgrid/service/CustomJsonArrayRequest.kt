package com.example.smartgrid.service

import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import org.json.JSONObject

class CustomJsonArrayRequest(
    method: Int,
    url: String,
    obj: JSONArray,
    val auth: JSONObject,
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

}
