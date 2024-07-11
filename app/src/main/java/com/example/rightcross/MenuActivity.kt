package com.example.rightcross

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val playWithComputerButton: Button = findViewById(R.id.playWithComputerButton)
        val playWithSomeoneButton: Button = findViewById(R.id.playWithSomeoneButton)

        playWithComputerButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "computer")
            startActivity(intent)
        }

        playWithSomeoneButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "someone")
            startActivity(intent)
        }
    }
}
