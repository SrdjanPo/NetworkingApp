package com.example.networkingapp.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkingapp.Chat

import com.example.networkingapp.R
import com.example.networkingapp.User
import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.adapters.ChatsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_matches.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class MatchesFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    private lateinit var chatDatabase: DatabaseReference

    //private val matches by lazy { matchesRV }

    private val chatsAdapter = ChatsAdapter(ArrayList())

    private var callback: TinderCallback? = null

    fun setCallback(callback: TinderCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabase()
        chatDatabase = callback.getChatDatabase()

        //fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        chatsAdapter.removeAllElements()
        fetchData()
         matchesRV.apply {
             setHasFixedSize(false)
             layoutManager = LinearLayoutManager(context)
             adapter = chatsAdapter
             matchesRV.addItemDecoration(
                 DividerItemDecoration(
                     matchesRV.getContext(),
                     DividerItemDecoration.VERTICAL
                 )
             )

         }
    }

    fun fetchData() {


        userDatabase.child(userId).child("matched").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {

                    p0.children.forEach { child ->
                        val matchId = child.key
                        val chatId = child.value.toString()

                        if (!matchId.isNullOrEmpty()) {
                            userDatabase.child(matchId).addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    val user = p0.getValue(User::class.java)
                                    if (user != null) {
                                        val chat = Chat(userId, chatId, user.uid!!, user.firstName!!, user.lastName!!, user.thumb_image!!, user.profession!!)
                                        chatsAdapter.addElement(chat)
                                    }
                                }
                            })
                        }
                    }

                    matchesRV.visibility = View.VISIBLE
                    progressLayoutMatches.visibility = View.GONE

                }
            }

        })

        matchesRV.visibility = View.VISIBLE
        progressLayoutMatches.visibility = View.GONE
    }
}
