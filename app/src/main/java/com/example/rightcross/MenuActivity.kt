package com.example.rightcross

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout)) { view, insets ->
            view.setPadding(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                insets.systemWindowInsetBottom
            )
            insets.consumeSystemWindowInsets()
        }

        val playComputerButton: Button = findViewById(R.id.playWithComputerButton)
        val playFriendButton: Button = findViewById(R.id.playWithSomeoneButton)

        playComputerButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "computer")
            startActivity(intent)
        }

        playFriendButton.setOnClickListener {
            val intent = Intent(this, PlayWithFriendActivity::class.java)
            startActivity(intent)
        }
    }
}
