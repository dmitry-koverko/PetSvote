package com.petsvote.data

import android.content.Context

object UserInfo {

    private val USERINFO = "userinfo"
    private val BEARER = "bearer"
    private val APPLYPHOTOS = "applyphotos"
    private val ID = "id"

    fun getBearer(context: Context): String {
        return context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE)
            .getString(BEARER, "") ?: ""
    }

    fun setBearer(context: Context, bearer: String){
        val sharedPref = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(BEARER, bearer)
            apply()
        }
    }

    fun getUserId(context: Context): Int {
        return context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE)
            .getInt(ID, 0)
    }

    fun setUserId(context: Context, id: Int){
        val sharedPref = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(ID, id)
            apply()
        }
    }

    fun getApplyPhotos(context: Context): Int {
        return context.getSharedPreferences(APPLYPHOTOS, Context.MODE_PRIVATE)
            .getInt(APPLYPHOTOS, 0)
    }

    fun setApplyPhotos(context: Context, type: Int){
        val sharedPref = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(APPLYPHOTOS, type)
            apply()
        }
    }

    var listLanguage = listOf("ru", "en", "es", "uk", "ja", "ch")//TODO update language
    var bearer = ""
    var languge = "en"
}