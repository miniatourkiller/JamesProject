package com.example.smartgrid

import org.json.JSONArray

class MyParser {
   fun JSONArray.toArrayList(): ArrayList<Any> {
        val arrayList = ArrayList<Any>()
        for (i in 0 until length()) {
            arrayList.add(get(i))
        }
        return arrayList
    }
}