package com.example.rightcross

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var gridLayout: GridLayout
    private lateinit var xWinCount: TextView
    private lateinit var oWinCount: TextView
    private lateinit var coinCount: TextView
    private lateinit var resetButton: Button

    private var xWins = 0
    private var oWins = 0
    private var coins = 0
    private var gameBoard = Array(3) { Array(3) { "" } }
    private var currentPlayer: String = "X"
    private var mode: String? = null
    private var isComputerMove = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)
        xWinCount = findViewById(R.id.xWinCount)
        oWinCount = findViewById(R.id.oWinCount)
        coinCount = findViewById(R.id.coinCount)
        resetButton = findViewById(R.id.resetButton)

        mode = intent.getStringExtra("mode")

        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.setOnClickListener {
                onGridButtonClick(button)
            }
        }

        resetButton.setOnClickListener {
            resetGame()
        }
    }

    private fun onGridButtonClick(button: Button) {
        val tag = button.tag.toString()
        val row = tag[0].toString().toInt()
        val col = tag[1].toString().toInt()

        if (gameBoard[row][col].isEmpty() && currentPlayer == "X") {
            gameBoard[row][col] = currentPlayer
            button.text = currentPlayer
            if (checkWin(currentPlayer)) {
                xWins++
                coins += 20
                updateScores()
                showResult("Player X wins!")
                return
            }
            if (isBoardFull()) {
                showResult("It's a draw!")
                return
            }
            currentPlayer = "O"
            if (mode == "computer") {
                isComputerMove = true
                Handler(Looper.getMainLooper()).postDelayed({
                    computerMove()
                    isComputerMove = false
                }, 500)
            }
        } else if (gameBoard[row][col].isEmpty() && currentPlayer == "O" && mode == "friend_offline") {
            gameBoard[row][col] = currentPlayer
            button.text = currentPlayer
            if (checkWin(currentPlayer)) {
                oWins++
                coins -= 20
                updateScores()
                showResult("Player O wins!")
                return
            }
            if (isBoardFull()) {
                showResult("It's a draw!")
                return
            }
            currentPlayer = "X"
        }
    }

    private fun computerMove() {
        val bestMove = findBestMove()
        if (bestMove != null) {
            gameBoard[bestMove.first][bestMove.second] = "O"
            val buttonId = resources.getIdentifier("button${bestMove.first}${bestMove.second}", "id", packageName)
            val button = findViewById<Button>(buttonId)
            button.text = "O"
            if (checkWin("O")) {
                oWins++
                coins -= 20
                updateScores()
                showResult("Computer wins!")
                return
            }
            if (isBoardFull()) {
                showResult("It's a draw!")
                return
            }
            currentPlayer = "X"
        }
    }

    private fun findBestMove(): Pair<Int, Int>? {
        var bestVal = Int.MIN_VALUE
        var bestMove: Pair<Int, Int>? = null

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (gameBoard[i][j].isEmpty()) {
                    gameBoard[i][j] = "O"
                    val moveVal = minimax(0, false)
                    gameBoard[i][j] = ""
                    if (moveVal > bestVal) {
                        bestMove = Pair(i, j)
                        bestVal = moveVal
                    }
                }
            }
        }
        return bestMove
    }

    private fun minimax(depth: Int, isMax: Boolean): Int {
        val score = evaluate()

        if (score == 10) return score - depth
        if (score == -10) return score + depth
        if (isBoardFull()) return 0

        if (isMax) {
            var best = Int.MIN_VALUE
            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    if (gameBoard[i][j].isEmpty()) {
                        gameBoard[i][j] = "O"
                        best = maxOf(best, minimax(depth + 1, !isMax))
                        gameBoard[i][j] = ""
                    }
                }
            }
            return best
        } else {
            var best = Int.MAX_VALUE
            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    if (gameBoard[i][j].isEmpty()) {
                        gameBoard[i][j] = "X"
                        best = minOf(best, minimax(depth + 1, !isMax))
                        gameBoard[i][j] = ""
                    }
                }
            }
            return best
        }
    }

    private fun evaluate(): Int {
        for (i in 0 until 3) {
            if (gameBoard[i][0] == gameBoard[i][1] && gameBoard[i][1] == gameBoard[i][2]) {
                if (gameBoard[i][0] == "O") return 10
                if (gameBoard[i][0] == "X") return -10
            }
        }
        for (i in 0 until 3) {
            if (gameBoard[0][i] == gameBoard[1][i] && gameBoard[1][i] == gameBoard[2][i]) {
                if (gameBoard[0][i] == "O") return 10
                if (gameBoard[0][i] == "X") return -10
            }
        }
        if (gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2]) {
            if (gameBoard[0][0] == "O") return 10
            if (gameBoard[0][0] == "X") return -10
        }
        if (gameBoard[0][2] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][0]) {
            if (gameBoard[0][2] == "O") return 10
            if (gameBoard[0][2] == "X") return -10
        }
        return 0
    }

    private fun isBoardFull(): Boolean {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (gameBoard[i][j].isEmpty()) return false
            }
        }
        return true
    }

    private fun checkWin(player: String): Boolean {
        for (i in 0 until 3) {
            if (gameBoard[i][0] == player && gameBoard[i][1] == player && gameBoard[i][2] == player) return true
            if (gameBoard[0][i] == player && gameBoard[1][i] == player && gameBoard[2][i] == player) return true
        }
        if (gameBoard[0][0] == player && gameBoard[1][1] == player && gameBoard[2][2] == player) return true
        if (gameBoard[0][2] == player && gameBoard[1][1] == player && gameBoard[2][0] == player) return true
        return false
    }

    private fun resetGame() {
        gameBoard = Array(3) { Array(3) { "" } }
        currentPlayer = "X"
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.text = ""
        }
    }

    private fun updateScores() {
        xWinCount.text = "Player A: $xWins"
        oWinCount.text = "Player B: $oWins"
        coinCount.text = "Coins: $coins"
    }

    private fun showResult(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            resetGame()
            currentPlayer = if (mode == "computer") "X" else currentPlayer
            if (mode == "computer") {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (currentPlayer == "O") {
                        computerMove()
                    }
                }, 50)
            }
        }, 2000)
    }
}
