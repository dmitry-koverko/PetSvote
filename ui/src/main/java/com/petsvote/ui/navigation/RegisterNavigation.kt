package com.petsvote.ui.navigation

interface RegisterNavigation {
    fun closeRegister()
    fun startRegisterNavigation()
    fun toLegal()
    fun toInfoLegal(type: Int)
    fun infoLegalToLegal()
    fun legalToRegister()
}