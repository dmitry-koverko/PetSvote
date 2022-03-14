package com.petsvote.data

import android.content.Context

object UserInfo {

    private val USERINFO = "userinfo"
    private val BEARER = "bearer"
    private val APPLYPHOTOS = "applyphotos"
    private val LANGUAGE = "language"
    private val TABSFILTER = "tabs"
    private val NOTIFY = "notify"
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

    fun getApplyPhotos(context: Context): Int {//0 - not vision 1 -vision
        return context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE)
            .getInt(APPLYPHOTOS, 0)
    }

    fun setApplyPhotos(context: Context, type: Int){
        val sharedPref = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(APPLYPHOTOS, type)
            apply()
        }
    }

    fun getNotify(context: Context): Boolean {
        return context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE)
            .getBoolean(NOTIFY, true)
    }

    fun setNotify(context: Context, type: Boolean){ // 0 - on    1 off
        val sharedPref = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(NOTIFY, type)
            apply()
        }
    }


    fun getTabsFilter(context: Context): Int {
        return context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE)
            .getInt(TABSFILTER, 2)//default world
    }

    fun setTabsFilter(context: Context, type: Int){
        val sharedPref = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(TABSFILTER, type)
            apply()
        }
    }

    fun getLanguage(context: Context): String {
        return context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE)
            .getString(LANGUAGE, "en") ?: "en"
    }

    fun setLanguage(context: Context, type: String){
        val sharedPref = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(LANGUAGE, type)
            apply()
        }
    }

    var listLanguage = listOf("ru", "en", "es", "uk", "ja", "ch")//TODO update language
    var bearer = ""
    var languge = "en"
}