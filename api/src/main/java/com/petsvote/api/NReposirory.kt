package com.petsvote.api

interface NReposirory {
    fun login()
    suspend fun getConfig(): Boolean
    suspend fun getLocalize(): Boolean
}
