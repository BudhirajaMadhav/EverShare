package com.androidmadhav.evershare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidmadhav.evershare.models.Post
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//Options ke base pe hi data feed krta hai
//Options me wo query hogi
class PostAdapter(options: FirestoreRecyclerOptions<Post>, val listener: IPostAdapter) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
    options
) {

    val currentUserId = Firebase.auth.currentUser!!.uid

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val userImage = itemView.findViewById<ImageView>(R.id.userImage)
        val userName = itemView.findViewById<TextView>(R.id.userName)
        val createdAt = itemView.findViewById<TextView>(R.id.createdAt)
        val postText = itemView.findViewById<TextView>(R.id.postText)
        val likeButton = itemView.findViewById<ImageView>(R.id.likeButton)
        val likeCount = itemView.findViewById<TextView>(R.id.likeCount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val viewHolder =  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false))

        /*

        -> Why click listener in oncreateViewHolder, not in onBindViewHolder()?

        -> The onCreateViewHolder() method will be called the first several times a ViewHolder
        is needed of each viewType. The onBindViewHolder() method will be called every time a new item scrolls
        into view, or has its data change. You want to avoid any expensive operations in onBindViewHolder() because
        it can slow down your scrolling. This is less of a concern in onCreateViewHolder().
        Thus it's generally better to create things like OnClickListeners in onCreateViewHolder() so that they
        only happen once per ViewHolder object. You can call getLayoutPosition() inside the listener in order to get the
        current position, rather than taking the position argument provided to onBindViewHolder().
        */

        viewHolder.likeButton.setOnClickListener {
            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }

        return viewHolder
    }

//    see model's type is Post
    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {

        holder.likeCount.text = model.likedBy.size.toString()
        holder.postText.text = model.text
        holder.userName.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)

        var isLiked = model.likedBy.contains(currentUserId)

        if(isLiked){

            holder.likeButton.setImageResource(R.drawable.ic_liked)

        }
        else{

            holder.likeButton.setImageResource(R.drawable.ic_unliked)

        }

    }

    interface IPostAdapter{

        fun onLikeClicked(postId: String)

    }

}