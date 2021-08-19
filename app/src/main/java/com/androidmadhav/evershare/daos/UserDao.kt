package com.androidmadhav.evershare.daos

import com.androidmadhav.evershare.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {

    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")

    fun addUser(user: User) {

//        user non null hai toh control isme ghusega
//        agar nullable hai toh isme nahi ghusega

        user?.let {

            GlobalScope.launch(Dispatchers.IO) {
//                since UID is same, toh baar baar add nahi hogi
//                wo document jiski ye uid hai, uske andar user daaldia
                usersCollection.document(user.uid).set(it)
            }

        }

    }

    fun getUserById(uid: String): Task<DocumentSnapshot> {

        return usersCollection.document(uid).get()

    }


}