package com.example.networkingapp


data class User(
    val uid: String? = "",
    val name: String? = "",
    val profession: String? = "",
    val location: String? = "",
    val about: String? = "",
    val email: String? = "",
    val instagram: String? = "",
    val linkedin: String? = "",
    val web: String? = "",
    val imageUrl: String? = ""
)

data class CurrentOrganization (
    val company: String? = "",
    val title: String? = "",
    val startDate: String? = ""
)

data class PreviousOrganization (
    val company: String? = "",
    val title: String? = "",
    val startDate: String? = "",
    val endDate: String? = ""
)



