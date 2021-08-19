package com.androidmadhav.evershare.daos

import com.androidmadhav.evershare.models.Post
import com.androidmadhav.evershare.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = Firebase.auth

    fun addPost(postText: String) {
        /*
        Here I've two options:
        Either get the User Object from firebase or create a new object
        Since, I created an object earlier, here I should get from firebase, as I'm learning something new.
        */


        GlobalScope.launch {

//        I know my currentUser is not null, If this is null
//        then there is some illegal thing going on with my app
//        Go crash it!!

            val fireBaseUser = auth.currentUser!!
            val currentUserId = fireBaseUser.uid

            val userDao = UserDao()
            /*
            * getUserById just won't return at the same time, it will take some time to get the Tast<>
            * so that await() will wait for the function to return
            * and then operations can be performed on it
            * await() is NECESSARY.
            */
            val user = userDao
                .getUserById(currentUserId).await()
                .toObject(User::class.java)!!
            val currentTime = System.currentTimeMillis()
            val post = Post(postText, user, currentTime)

            postCollection.document().set(post)
        }

    }

    fun getPostFromId(postId: String): Task<DocumentSnapshot> {

        return postCollection.document(postId).get()

    }

    fun onLikeClicked(postId: String){

        GlobalScope.launch {

            val post = getPostFromId(postId).await().toObject(Post::class.java)
            val currentUserId = auth.currentUser!!.uid

            if (post!!.likedBy.contains(currentUserId)){
                post.likedBy.remove(currentUserId)
            }
            else{
                post.likedBy.add(currentUserId)
            }

            postCollection.document(postId).set(post)

        }
    }


}