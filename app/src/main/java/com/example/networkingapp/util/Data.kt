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
    val imageUrl: String? = "",
    var image: String? = "",
    var thumb_image: String? = "",
    var profiled: String? = ""
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

data class InterestCounters (
    val technology: Int? = 0,
    val sport: Int? = 0,
    val science: Int? = 0,
    val hobby: Int? = 0,
    val design: Int? = 0
)

data class Spot(
    val uid: String? = "",
    val name: String? = "",
    val profession: String? = "",
    val location: String? = "",
    val interests: ArrayList<String> = arrayListOf(),
    val goals: HashMap<String, String> = hashMapOf(),
    val currentOrg: MutableMap<String, Any> = mutableMapOf(),
    //val previousOrg: HashMap<String, String> = hashMapOf(),
    val about: String? = "",
    val email: String? = "",
    val instagram: String? = "",
    val linkedin: String? = "",
    val web: String? = "",
    val imageUrl: String? = "",
    var image: String? = "",
    var thumb_image: String? = "",
    var countCurrentChildren: Int = 0,
    var countPreviousChildren: Int = 0
) {
    companion object {
        private var counter = 0L
    }
}



