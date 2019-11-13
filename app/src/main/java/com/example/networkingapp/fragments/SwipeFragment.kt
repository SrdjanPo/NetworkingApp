@file:Suppress("RedundantOverride")

package com.example.networkingapp.fragments


import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.example.networkingapp.*
import com.example.networkingapp.R

import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.adapters.CardStackAdapter
import com.example.networkingapp.adapters.SpotDiffCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_experience.*
import kotlinx.android.synthetic.main.fragment_swipe.*
import kotlinx.android.synthetic.main.item_spot.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SwipeFragment : Fragment(), CardStackListener {

    private lateinit var userDatabase: DatabaseReference
    private lateinit var userId: String

    private var profiled: String? = null

    private var callback: TinderCallback? = null

    private var itemsFromDB = ArrayList<Spot>()


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

                val cardsQuery = userDatabase.orderByChild("profiled").equalTo(profiled)

                cardsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                        //progressLayout.visibility = View.GONE
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        p0.children.forEach { child ->

                            val spot = child.getValue(Spot::class.java)

                            val interestsList = ArrayList<String>()
                            val goalsList = HashMap<String, String>()
                            var currentOrgList = ArrayList<CurrentOrganization>()
                            //val currentOrgListMap = mutableMapOf<String, Any>()
                            var previousOrgList = HashMap<String, String>()
                            val countPreviousChildren =
                                child.child("previousOrg").childrenCount.toInt()
                            val countCurrentChildren =
                                child.child("currentOrg").childrenCount.toInt()


                            for (snapshotInterest in child.child("interestedIn").children) {

                                val interestsFromDB = snapshotInterest.getValue(String::class.java)

                                interestsList.add(interestsFromDB!!)

                            }

                            for (snapshotGoal in child.child("goals").children) {

                                val goalsFromDB = snapshotGoal.getValue(String::class.java)

                                goalsList.put(goalsFromDB!!, goalsFromDB)

                            }

                            for (snapshotCurrentOrg in child.child("currentOrg").children) {

                                var currentFromDB =
                                    snapshotCurrentOrg.getValue(CurrentOrganization::class.java)

                                /*currentOrgListMap.put("company", currentFromDB!!.company!!)
                                currentOrgListMap.put("title", currentFromDB.title!!)
                                currentOrgListMap.put("startDate", currentFromDB.startDate!!)*/

                                currentOrgList.add(currentFromDB!!)
                            }

                            /*for (snapshotPreviousOrg in child.child("previousOrg").children) {

                                var currentFromDB =
                                    snapshotPreviousOrg.getValue(PreviousOrganization::class.java)

                                currentOrgListMap.put("company", currentFromDB!!.company!!)
                                currentOrgListMap.put("title", currentFromDB!!.title!!)
                                currentOrgListMap.put("startDate", currentFromDB!!.startDate!!)

                                Log.d("Company", currentFromDB!!.company!!)
                                Log.d("Title", currentFromDB!!.title!!)
                                Log.d("Start Date", currentFromDB!!.startDate!!)

                                //currentOrgList.add(currentFromDB!!)
                            }*/

                            Log.d("LISTA", currentOrgList.toString())

                            itemsFromDB.add(
                                Spot(
                                    name = spot?.name,
                                    profession = spot?.profession,
                                    location = spot?.location,
                                    image = spot?.thumb_image,
                                    interests = interestsList,
                                    goals = goalsList,
                                    currentOrgList = currentOrgList,
                                    //currentOrg = currentOrgListMap,
                                    //previousOrg = previousOrgList,
                                    countCurrentChildren = countCurrentChildren,
                                    countPreviousChildren = countPreviousChildren,
                                    about = spot?.about
                                )
                            )
                        }

                        setupCardStackView()

                    }
                })
            }
        })

    }


    private fun createSpots(): List<Spot> {
        var spots = ArrayList<Spot>()

        Log.d("OPET", itemsFromDB.size.toString())

        spots.addAll(itemsFromDB)

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

    /*private fun paginate() {
        val old = adapter.getSpots()
        val new = old.plus(createSpots())
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun reload() {
        val old = adapter.getSpots()
        val new = createSpots()
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }*/

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        /*if (manager.topPosition == adapter.itemCount - 5) {
            //paginate()
        }*/
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

}
