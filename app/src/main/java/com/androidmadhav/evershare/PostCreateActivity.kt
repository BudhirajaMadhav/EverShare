package com.androidmadhav.evershare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.androidmadhav.evershare.daos.PostDao

class PostCreateActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_create)

        postDao = PostDao()

        findViewById<Button>(R.id.postButton).setOnClickListener{

            val input = findViewById<EditText>(R.id.postInput).editableText.toString().trim()
            if(input.isNotEmpty()){

                postDao.addPost(input)
                finish()
            }

        }

    }
}