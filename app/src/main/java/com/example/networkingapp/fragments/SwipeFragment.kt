@file:Suppress("RedundantOverride")

package com.example.networkingapp.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator

import com.example.networkingapp.R
import com.example.networkingapp.Spot
import com.example.networkingapp.User
import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.adapters.CardStackAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_swipe.*
import kotlinx.android.synthetic.main.item_spot.*


class SwipeFragment : Fragment(), CardStackListener {

    private lateinit var userDatabase: DatabaseReference
    private lateinit var userId: String

    private var profiled: String? = null

    private var callback: TinderCallback? = null

    private val manager by lazy { CardStackLayoutManager(activity, this) }

    private val adapter by lazy { CardStackAdapter(createSpots()) }


    fun setCallback(callback: TinderCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_swipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDatabase.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)

                profiled = user?.profiled

            }
        })

       // setupCardStackView()

    }


    private fun createSpots(): List<Spot> {
        val spots = ArrayList<Spot>()
        /*spots.add(
            Spot(
                name = "Yasaka Shrine",
                profession = "Kyoto",
                image = "https://source.unsplash.com/Xq1ntWruZQI/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Fushimi Inari Shrine",
                profession = "Kyoto",
                image = "https://source.unsplash.com/NYyCqdBOKwc/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Bamboo Forest",
                profession = "Kyoto",
                image = "https://source.unsplash.com/buF62ewDLcQ/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Brooklyn Bridge",
                profession = "New York",
                image = "https://source.unsplash.com/THozNzxEP3g/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Empire State Building",
                profession = "New York",
                image = "https://source.unsplash.com/USrZRcRS2Lw/600x800"
            )
        )
        spots.add(
            Spot(
                name = "The statue of Liberty",
                profession = "New York",
                image = "https://source.unsplash.com/PeFk7fzxTdk/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Louvre Museum",
                profession = "Paris",
                image = "https://source.unsplash.com/LrMWHKqilUw/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Eiffel Tower",
                profession = "Paris",
                image = "https://source.unsplash.com/HN-5Z6AmxrM/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Big Ben",
                profession = "London",
                image = "https://source.unsplash.com/CdVAUADdqEc/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Great Wall of China",
                profession = "China",
                image = "https://source.unsplash.com/AWh9C-QjhE4/600x800"
            )
        )*/

        populateItems(spots)
        return spots
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(45.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(false)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        card_stack_view.layoutManager = manager
        card_stack_view.adapter = adapter
        card_stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            //paginate()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.profileName)
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.profileName)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }

    fun populateItems(spots: ArrayList<Spot>) {

        val cardsQuery = userDatabase.orderByChild("profiled").equalTo(profiled)

        cardsQuery.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                progressLayout.visibility = View.GONE

            }

            override fun onDataChange(p0: DataSnapshot) {

                /*spots.add(
                    Spot(
                        name = "The statue of Liberty",
                        profession = "New York",
                        image = "https://source.unsplash.com/PeFk7fzxTdk/600x800"
                    )
                )*/

                val spot = p0.getValue(Spot::class.java)

                spots.add(Spot(name = spot?.name, profession = spot?.profession, image = spot?.thumb_image))

                Log.d("SPOT", spots.toString())

                // Basic info

                /*var profileName = spot?.name
                var profession = spot?.profession
                var location = spot?.location

                var image = p0.child("image").value.toString()
                var thumbnail = p0.child("thumb_image").value.toString()


                if (!image!!.equals("default")) {
                    Picasso.with(context).load(image).noPlaceholder()
                        .into(photoIV, object : Callback {
                            override fun onError() {
                                Log.d("TAG", "error")
                            }

                            override fun onSuccess() {

                                Log.d("TAG", "success")
                            }
                        })
                }*/

                /*// Interests
                for (snapshotInterest in p0.child("interestedIn").children) {

                    var interestsFromDB = snapshotInterest.getValue(String::class.java)

                    populateInterests(interestsFromDB)
                }

                // Goals
                for (snapshotGoal in p0.child("goals").children) {

                    var goalFromDB = snapshotGoal.getValue(String::class.java)

                    populateGoals(goalFromDB)
                }

                // Current Organization
                for (snapshotCurrentOrg in p0.child("currentOrg").children) {

                    var currentFromDB =
                        snapshotCurrentOrg.getValue(CurrentOrganization::class.java)

                    if (countPreviousChildren == 0) {

                        populateCurrentOrgIfNoPreviousOrg(currentFromDB, countCurrentChildren)
                    } else {

                        populateCurrentOrg(currentFromDB)
                    }
                }

                for (snapshotPreviousOrg in p0.child("previousOrg").children) {

                    var previousFromDB =
                        snapshotPreviousOrg.getValue(PreviousOrganization::class.java)

                    populatePreviousOrg(previousFromDB, countPreviousChildren)

                }*/


            }
        })

    }

}
