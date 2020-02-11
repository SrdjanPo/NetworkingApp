@file:Suppress("RedundantOverride")

package com.example.networkingapp.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.example.networkingapp.*

import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.adapters.CardStackAdapter
import com.example.networkingapp.adapters.SpotDiffCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_swipe.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.graphics.PixelFormat
import android.widget.Toast
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.networkingapp.R
import com.example.networkingapp.activities.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.match_window_full_screen.*


class SwipeFragment : Fragment(), CardStackListener {

    private lateinit var userDatabase: DatabaseReference
    private lateinit var chatDatabase: DatabaseReference
    private lateinit var userId: String

    private var profiled: String? = null
    private var testId: String? = null
    private var userFirstName: String? = null
    private var userLastName: String? = null
    private var userImageUrl: String? = null
    private val swipedRightList: ArrayList<String> = arrayListOf()
    private val swipedLeftList: ArrayList<String> = arrayListOf()
    private val itemsCounter: Int = 0

    private var callback: TinderCallback? = null

    private var itemsFromDB = ArrayList<Spot>()
    private var deletedItems = ArrayList<Spot>()

    private var manager: CardStackLayoutManager = CardStackLayoutManager(activity, this)

    private var positionCounter: Int = 0

    //private val cardStackView by lazy { card_stack_view }
    //private val progessLayoutLazy by lazy { progressLayoutSwipe }

    //private val manager by lazy { CardStackLayoutManager(activity, this) }

    private val adapter by lazy { CardStackAdapter(createSpots()) }


    fun setCallback(callback: TinderCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabase()
        chatDatabase = callback.getChatDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_swipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //manager = CardStackLayoutManager(activity, this)

        //buttons.background = context!!.getDrawable(R.drawable.gradient_transparent)

        setupButtons()

        card_stack_view.visibility = View.GONE
        buttons.visibility = View.GONE
        progressLayoutSwipe.visibility = View.VISIBLE

        //itemsFromDB.clear()

        if (itemsFromDB.isEmpty()) {


            userDatabase.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {


                    val user = p0.getValue(User::class.java)

                    profiled = user?.profiled
                    testId = user?.uid
                    userFirstName = user?.firstName
                    userLastName = user?.lastName
                    userImageUrl = user?.image

                    for (child in p0.child("swipedRight").children) {
                        swipedRightList.add(child.getValue().toString())
                    }

                    for (child in p0.child("swipedLeft").children) {
                        swipedLeftList.add(child.getValue().toString())
                    }

                    val cardsQuery = userDatabase.orderByChild("profiled").equalTo(profiled)

                    cardsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                            //progressLayout.visibility = View.GONE
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            p0.children.forEach { child ->

                                var childId = child.child("uid").getValue()

                                if (childId == userId
                                    || swipedRightList.contains(childId)
                                    || swipedLeftList.contains(childId)
                                ) {

                                    Log.d("USERID", childId.toString())

                                } else {

                                    val spot = child.getValue(Spot::class.java)

                                    val interestsList = ArrayList<String>()
                                    val goalsList = HashMap<String, String>()
                                    var currentOrgList = ArrayList<CurrentOrganization>()
                                    var previousOrgList = ArrayList<PreviousOrganization>()
                                    val countPreviousChildren =
                                        child.child("previousOrg").childrenCount.toInt()
                                    val countCurrentChildren =
                                        child.child("currentOrg").childrenCount.toInt()


                                    for (snapshotInterest in child.child("interestedIn").children) {

                                        val interestsFromDB =
                                            snapshotInterest.getValue(String::class.java)

                                        interestsList.add(interestsFromDB!!)

                                    }

                                    for (snapshotGoal in child.child("goals").children) {

                                        val goalsFromDB = snapshotGoal.getValue(String::class.java)

                                        goalsList.put(goalsFromDB!!, goalsFromDB)

                                    }

                                    for (snapshotCurrentOrg in child.child("currentOrg").children) {

                                        var currentFromDB =
                                            snapshotCurrentOrg.getValue(CurrentOrganization::class.java)

                                        currentOrgList.add(currentFromDB!!)
                                    }

                                    for (snapshotPreviousOrg in child.child("previousOrg").children) {

                                        var previousFromDB =
                                            snapshotPreviousOrg.getValue(PreviousOrganization::class.java)

                                        previousOrgList.add(previousFromDB!!)
                                    }


                                    itemsFromDB.add(
                                        Spot(
                                            uid = spot?.uid,
                                            firstName = spot?.firstName,
                                            lastName = spot?.lastName,
                                            profession = spot?.profession,
                                            location = spot?.location,
                                            image = spot?.thumb_image,
                                            interests = interestsList,
                                            goals = goalsList,
                                            currentOrgList = currentOrgList,
                                            previousOrgList = previousOrgList,
                                            countCurrentChildren = countCurrentChildren,
                                            countPreviousChildren = countPreviousChildren,
                                            about = spot?.about
                                        )
                                    )

                                }
                            }

                            setupCardStackView()

                            Log.d("EMPTY", itemsFromDB.size.toString())

                            progressLayoutSwipe.visibility = View.GONE
                            card_stack_view.visibility = View.VISIBLE
                            buttons.visibility = View.VISIBLE
                        }
                    })
                }
            })

        } else {

            setupCardStackView()
            reload()

            Log.d("NOTEMPTY", itemsFromDB.size.toString())


            progressLayoutSwipe.visibility = View.GONE
            card_stack_view.visibility = View.VISIBLE
            buttons.visibility = View.VISIBLE
        }
    }

    private fun reload() {
        val old = adapter.getSpots()
        val new = createSpots()
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }


    private fun createSpots(): List<Spot> {

        val spots = ArrayList<Spot>()
        spots.clear()
        spots.addAll(itemsFromDB)

        return spots
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun initialize() {
        manager = CardStackLayoutManager(activity, this)
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(90.0f)
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

    private fun setupButtons() {

        skip_button.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Slow.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            card_stack_view.swipe()
        }


        rewind_button.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            card_stack_view.rewind()
        }

        like_button.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Slow.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            card_stack_view.swipe()
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")

        /*if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }*/

        //val uid = adapter.getIDs()

        if (direction == Direction.Right) {

            /*userDatabase.child(userId).child("swipedRight").child(uid[manager.topPosition - 1])
                .setValue(uid[manager.topPosition - 1])*/

            userDatabase.child(userId).child("swipedRight").child(itemsFromDB[itemsCounter].uid!!)
                .setValue(itemsFromDB[itemsCounter].uid!!)


            userDatabase.child(itemsFromDB[itemsCounter].uid!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        val selectedUser = p0.getValue(User::class.java)
                        val selectedUserFirstName = selectedUser?.firstName
                        val selectedUserLastName = selectedUser?.lastName
                        val selectedUserProfession = selectedUser?.profession
                        val selectedUserImageUrl = selectedUser?.image
                        val selectedUserId = selectedUser?.uid

                        for (child in p0.child("swipedRight").children) {


                            if (child.value == userId) {

                                /*val calendar = Calendar.getInstance()
                                val currentDate = DateFormat.getDateInstance(DateFormat.MONTH_FIELD)
                                    .format(calendar.time)
                                val currentDateWOYear = currentDate.substring(0, 6)

                                Log.d("Datum", currentDate)
                                Log.d("DatumWOY", currentDateWOYear)*/

                                //Toast.makeText(context, "IT'S A MATCHHHHH", Toast.LENGTH_LONG).show()

                                val mDialogView = LayoutInflater.from(context)
                                    .inflate(R.layout.match_window_full_screen, null)

                                val dialog =
                                    Dialog(
                                        activity,
                                        android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
                                    )
                                dialog.setContentView(mDialogView)
                                var window = dialog.window

                                window.setBackgroundDrawable(ColorDrawable (Color.TRANSPARENT));

                                window.setBackgroundDrawableResource(R.color.transparent_grey);
                                dialog.show()

                                val button = mDialogView.findViewById<View>(R.id.matchLayout)
                                val matchName = mDialogView.findViewById<TextView>(R.id.matchName)
                                val userImage = mDialogView.findViewById<CircleImageView>(R.id.userPic)
                                val matchPic = mDialogView.findViewById<CircleImageView>(R.id.matchPic)
                                val sendMessage = mDialogView.findViewById<Button>(R.id.sendMessage)
                                val keepSwiping = mDialogView.findViewById<Button>(R.id.keepSwiping)


                                if(userImage != null && userImageUrl != "default") {
                                    Glide.with(userImage)
                                        .load(userImageUrl)
                                        .into(userImage)
                                }

                                else {

                                    userImage.setImageResource(R.drawable.profile_pic)
                                }

                                if(matchPic != null && selectedUserImageUrl != "default") {
                                    Glide.with(matchPic)
                                        .load(selectedUserImageUrl)
                                        .into(matchPic)
                                }

                                else {

                                    matchPic.setImageResource(R.drawable.profile_pic)
                                }



                                matchName.text = selectedUserFirstName.plus(" is your new connection!")

                                button.setOnClickListener {

                                    dialog.dismiss()
                                }

                                keepSwiping.setOnClickListener {

                                    dialog.dismiss()
                                }

                                val chatKey = chatDatabase.push().key

                                sendMessage.setOnClickListener {

                                    val intent = ChatActivity.newIntent(context, chatKey, userId, selectedUserImageUrl, selectedUserId)
                                    context!!.startActivity(intent)

                                    dialog.dismiss()
                                }

                                // chatDatabase.child(chatKey!!).child("dateMatched").setValue(currentDate)

                                userDatabase.child(userId).child("matched").child(selectedUserId!!)
                                    .setValue(chatKey)
                                userDatabase.child(selectedUserId).child("matched").child(userId)
                                    .setValue(chatKey)


                                chatDatabase.child(chatKey!!).child(userId).child("firstName")
                                    .setValue(userFirstName)
                                chatDatabase.child(chatKey).child(userId).child("lastName")
                                    .setValue(userLastName)
                                chatDatabase.child(chatKey).child(userId).child("imageUrl")
                                    .setValue(userImageUrl)


                                chatDatabase.child(chatKey).child(selectedUserId).child("firstName")
                                    .setValue(selectedUserFirstName)
                                chatDatabase.child(chatKey).child(selectedUserId).child("lastName")
                                    .setValue(selectedUserLastName)
                                chatDatabase.child(chatKey).child(selectedUserId).child("imageUrl")
                                    .setValue(selectedUserImageUrl)

                            }
                        }

                        //deletedItems.add(itemsFromDB[0])
                        //itemsFromDB.removeAt(0)

                    }
                })
        }

        if (direction == Direction.Left) {

            /*userDatabase.child(userId).child("swipedLeft").child(uid[manager.topPosition - 1])
                .setValue(uid[manager.topPosition - 1])*/

            userDatabase.child(userId).child("swipedLeft").child(itemsFromDB[itemsCounter].uid!!)
                .setValue(itemsFromDB[itemsCounter].uid!!)

            //deletedItems.add(itemsFromDB[manager.topPosition])

            //itemsFromDB.removeAt(0)
        }

        itemsFromDB.removeAt(0)

        if (itemsFromDB.isEmpty()) {

            noMoreConnections.visibility = View.VISIBLE
        }

    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")

        val uid = adapter.getIDs()
        val spots = adapter.getSpots()

        userDatabase.child(userId).child("swipedRight").child(uid[manager.topPosition])
            .removeValue()
        userDatabase.child(userId).child("swipedLeft").child(uid[manager.topPosition]).removeValue()

        //itemsFromDB.add(spots[manager.topPosition])


        /*userDatabase.child(userId).child("swipedRight")
            .child(deletedItems[deletedItems.size - 1].uid!!).removeValue()

        Log.d("deletedd", deletedItems[deletedItems.size - 1].firstName!!)

        userDatabase.child(userId).child("swipedLeft")
            .child(deletedItems[deletedItems.size - 1].uid!!).removeValue()

        var copyItemsList = itemsFromDB

        itemsFromDB.clear()

        itemsFromDB.add(0, deletedItems[deletedItems.size - 1])
        itemsFromDB.addAll(copyItemsList)

        deletedItems.removeAt(deletedItems.size - 1)

        Log.d("lista1", itemsFromDB.toString())
        Log.d("lista2", deletedItems.toString())*/
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


    /* override fun onAttachFragment(childFragment: Fragment?) {
         super.onAttachFragment(childFragment)
         val window = activity!!.window
         window.setFormat(PixelFormat.RGBA_8888)
     }*/

}
