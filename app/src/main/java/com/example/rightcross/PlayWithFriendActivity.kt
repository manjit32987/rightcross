package com.example.rightcross

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PlayWithFriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.friendplayer)

        val offlineButton: Button = findViewById(R.id.button_offline)
        val onlineButton: Button = findViewById(R.id.button_online)

        offlineButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "friend_offline")
            startActivity(intent)
        }

        onlineButton.setOnClickListener {
            // Handle online multiplayer option if needed
        }
    }
}
