package com.example.rightcross

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var board: Array<Array<Button>>
    private var currentPlayer = 'X'
    private var gameActive = true
    private var xWins = 0
    private var oWins = 0

    private lateinit var xWinCount: TextView
    private lateinit var oWinCount: TextView
    private lateinit var mode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mode = intent.getStringExtra("mode") ?: "someone"

        board = Array(3) { r ->
            Array(3) { c ->
                initButton(r, c)
            }
        }

        xWinCount = findViewById(R.id.xWinCount)
        oWinCount = findViewById(R.id.oWinCount)

        val resetButton: Button = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            resetBoard()
        }

        updateWinCounts()
    }

    private fun initButton(row: Int, col: Int): Button {
        val button: Button = findViewById(resources.getIdentifier("button$row$col", "id", packageName))
        button.setOnClickListener {
            if (gameActive && button.text.isEmpty()) {
                button.text = currentPlayer.toString()
                if (checkWin()) {
                    gameActive = false
                    if (currentPlayer == 'X') {
                        xWins++
                        showWinDialog("Congratulations X wins!")
                    } else {
                        oWins++
                        showWinDialog("Congratulations O wins!")
                    }
                    updateWinCounts()
                } else {
                    currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                    if (mode == "computer" && currentPlayer == 'O') {
                        makeComputerMove()
                    }
                }
            }
        }
        return button
    }

    private fun makeComputerMove() {
        // Simple AI to make a move
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].text.isEmpty()) {
                    board[i][j].text = 'O'.toString()
                    if (checkWin()) {
                        gameActive = false
                        oWins++
                        showWinDialog("Congratulations O wins!")
                        updateWinCounts()
                    } else {
                        currentPlayer = 'X'
                    }
                    return
                }
            }
        }
    }

    private fun checkWin(): Boolean {
        // Check rows
        for (i in 0..2) {
            if (board[i][0].text == currentPlayer.toString() &&
                board[i][1].text == currentPlayer.toString() &&
                board[i][2].text == currentPlayer.toString()) {
                return true
            }
        }

        // Check columns
        for (i in 0..2) {
            if (board[0][i].text == currentPlayer.toString() &&
                board[1][i].text == currentPlayer.toString() &&
                board[2][i].text == currentPlayer.toString()) {
                return true
            }
        }

        // Check diagonals
        if (board[0][0].text == currentPlayer.toString() &&
            board[1][1].text == currentPlayer.toString() &&
            board[2][2].text == currentPlayer.toString()) {
            return true
        }

        if (board[0][2].text == currentPlayer.toString() &&
            board[1][1].text == currentPlayer.toString() &&
            board[2][0].text == currentPlayer.toString()) {
            return true
        }

        return false
    }

    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j].text = ""
            }
        }
        currentPlayer = 'X'
        gameActive = true
    }

    private fun showWinDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                resetBoard()
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun updateWinCounts() {
        xWinCount.text = "X wins: $xWins"
        oWinCount.text = "O wins: $oWins"
    }
}
