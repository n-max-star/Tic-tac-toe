package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    // Represent the board as a 2D array of Buttons
    private lateinit var board: Array<Array<Button>>

    // Game state variables
    private var currentPlayer = "X"
    private var gameActive = true
    // 3x3 board state: 0 = empty, 1 = X, 2 = O
    private var gameState = arrayOf(
        intArrayOf(0, 0, 0),
        intArrayOf(0, 0, 0),
        intArrayOf(0, 0, 0)
    )

    // All possible winning combinations (indices of the 3x3 grid)
    private val winningPositions = arrayOf(
        intArrayOf(0,0, 0,1, 0,2), // Row 1
        intArrayOf(1,0, 1,1, 1,2), // Row 2
        intArrayOf(2,0, 2,1, 2,2), // Row 3
        intArrayOf(0,0, 1,0, 2,0), // Col 1
        intArrayOf(0,1, 1,1, 2,1), // Col 2
        intArrayOf(0,2, 1,2, 2,2), // Col 3
        intArrayOf(0,0, 1,1, 2,2), // Diagonal
        intArrayOf(0,2, 1,1, 2,0)  // Anti-diagonal
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)

        // Initialize the board array and set click listeners
        board = Array(3) { row ->
            Array(3) { col ->
                val button = findButtonByTag("$row,$col")
                button.setOnClickListener { onCellClick(it, row, col) }
                button
            }
        }

        findViewById<Button>(R.id.btnReset).setOnClickListener { resetGame() }
        updateStatus()
    }

    // Helper function to find a button by its tag
    private fun findButtonByTag(tag: String): Button {
        return when (tag) {
            "0,0" -> findViewById(R.id.btn00)
            "0,1" -> findViewById(R.id.btn01)
            "0,2" -> findViewById(R.id.btn02)
            "1,0" -> findViewById(R.id.btn10)
            "1,1" -> findViewById(R.id.btn11)
            "1,2" -> findViewById(R.id.btn12)
            "2,0" -> findViewById(R.id.btn20)
            "2,1" -> findViewById(R.id.btn21)
            "2,2" -> findViewById(R.id.btn22)
            else -> throw IllegalArgumentException("Invalid tag: $tag")
        }
    }

    private fun onCellClick(view: View, row: Int, col: Int) {
        val button = view as Button

        // Check if the cell is empty and the game is active
        if (gameState[row][col] == 0 && gameActive) {
            // Update the internal game state
            gameState[row][col] = if (currentPlayer == "X") 1 else 2

            // Update the button's text
            button.text = currentPlayer

            // Check for a winner or draw
            if (checkForWinner()) {
                tvStatus.text = "Player $currentPlayer Wins!"
                gameActive = false
            } else if (isBoardFull()) {
                tvStatus.text = "It's a Draw!"
                gameActive = false
            } else {
                // Switch players
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                updateStatus()
            }
        }
    }

    private fun checkForWinner(): Boolean {
        for (positions in winningPositions) {
            // Get the player values (1 for X, 2 for O) at the three positions
            val val1 = gameState[positions[0]][positions[1]]
            val val2 = gameState[positions[2]][positions[3]]
            val val3 = gameState[positions[4]][positions[5]]

            if (val1 != 0 && val1 == val2 && val2 == val3) {
                return true // Winner found
            }
        }
        return false
    }

    private fun isBoardFull(): Boolean {
        for (row in 0..2) {
            for (col in 0..2) {
                if (gameState[row][col] == 0) {
                    return false // Found an empty cell
                }
            }
        }
        return true // No empty cells found
    }

    private fun resetGame() {
        gameActive = true
        currentPlayer = "X"
        // Clear the game state array
        for (row in 0..2) {
            for (col in 0..2) {
                gameState[row][col] = 0
            }
        }
        // Clear all button texts
        for (row in 0..2) {
            for (col in 0..2) {
                board[row][col].text = ""
            }
        }
        updateStatus()
    }

    private fun updateStatus() {
        tvStatus.text = "Player $currentPlayer's Turn"
    }
}
