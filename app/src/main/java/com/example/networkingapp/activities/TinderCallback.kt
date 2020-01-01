package com.example.networkingapp.activities

import android.provider.ContactsContract
import com.google.firebase.database.DatabaseReference

interface TinderCallback {
    fun onSignout()
    fun onGetUserId(): String
    fun getUserDatabase(): DatabaseReference
    fun getChatDatabase(): DatabaseReference
}