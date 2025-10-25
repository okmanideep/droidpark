package me.okmanideep.droidpark.ui

import androidx.navigation.NavController

object Destinations {
    const val HOME = "home"
    const val DETAILS = "details/{id}"

    fun details(id: String) = "details/$id"
}

fun NavController.navigateToDetails(id: String) {
    navigate(Destinations.details(id))
}