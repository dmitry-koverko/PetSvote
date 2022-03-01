package com.petsvote.data

import android.content.Context

object DocumentsInfo {

    private val DOCUMENTS = "documents"
    private val POLICY = "policy"
    private val TERMS = "terms"

    fun getPolicy(context: Context): String {
        return context.getSharedPreferences(DOCUMENTS, Context.MODE_PRIVATE)
            .getString(POLICY, "") ?: ""
    }

    fun setPolicy(context: Context, policy: String){
        val sharedPref = context.getSharedPreferences(DOCUMENTS, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(POLICY, policy)
            apply()
        }
    }

    fun getTerms(context: Context): String {
        return context.getSharedPreferences(DOCUMENTS, Context.MODE_PRIVATE)
            .getString(TERMS, "") ?: ""
    }

    fun setTerms(context: Context, terms: String){
        val sharedPref = context.getSharedPreferences(DOCUMENTS, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(TERMS, terms)
            apply()
        }
    }
}