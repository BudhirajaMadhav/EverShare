package com.androidmadhav.evershare.models

class Post(
    val text: String = "",
    val createdBy: User = User(),
    val createdAt: Long = 0,
//    In future I'll change it to User class
    val likedBy: ArrayList<String> = ArrayList()
)