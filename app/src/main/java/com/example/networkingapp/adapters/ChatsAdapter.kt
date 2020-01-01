package com.example.networkingapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.networkingapp.Chat
import com.example.networkingapp.R
import de.hdodenhof.circleimageview.CircleImageView
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.networkingapp.activities.ChatActivity


class ChatsAdapter(private var chats:ArrayList<Chat>): RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    fun addElement(chat: Chat) {
        chats.add(chat)
        notifyDataSetChanged()
    }

    fun removeAllElements() {

        chats.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))

    override fun getItemCount() = chats.size


    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    class ChatsViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        private var layout = view.findViewById<View>(R.id.chatLayout)
        private var image = view.findViewById<CircleImageView>(R.id.chatPictureIV)
        private var name = view.findViewById<TextView>(R.id.chatNameTV)
        private var profession = view.findViewById<TextView>(R.id.chatProfessionTV)

        fun bind(chat: Chat) {


            name.text = chat.firstName.plus(" ").plus(chat.lastName)
            profession.text = chat.profession
            if(image != null && chat.imageUrl != "default") {
                Glide.with(view)
                    .load(chat.imageUrl)
                    .into(image)
            }

            else {

                image.setImageResource(R.drawable.profile_pic)
            }

            layout.setOnClickListener {
                val intent = ChatActivity.newIntent(view.context, chat.chatId, chat.userId, chat.imageUrl, chat.otherUserId)
                view.context.startActivity(intent)
            }

        }

    }
}