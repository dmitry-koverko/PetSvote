package com.petsvote.data

import android.content.Context

object UserInfo {

    fun getBearer(context: Context): String {
        return context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
            .getString("bearer", "") ?: ""
    }

    fun setBearer(context: Context, bearer: String){
        val sharedPref = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("bearer", bearer)
            apply()
        }
    }

    var listLanguage = listOf("ru", "en", "es", "uk", "ja", "ch")//TODO update language
    var bearer = ""
    var languge = "en"
}