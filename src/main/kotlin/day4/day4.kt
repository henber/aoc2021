package day4

import listPartition
import readInput
import transpose
import java.util.function.Predicate

val input = readInput("day4.txt")

val inputNumbers = input[0].split(",").map { it.toInt() }
val inputBoards = input.drop(2)

data class Board(val values: List<List<Int>>) {

    var hasWon: Boolean = false

    fun isWinner(drawnNumbers: Set<Int>): Boolean {

        return (values.any { drawnNumbers.containsAll(it) } ||
            values.transpose().any { drawnNumbers.containsAll(it) })
            .also { hasWon = it }
    }
}

val boards = listPartition(inputBoards) { it != "" }
    .map {
        it.map {
            it.split(" ")
                .filter { it != "" }
                .map { it.toInt() }
        }
    }
    .map(::Board)

val drawnNumbers = mutableSetOf<Int>()
var drawIndex = -1

fun drawNumber(): List<Board>? {
    drawIndex += 1
    drawnNumbers.add(inputNumbers[drawIndex])

    if (drawIndex >= 4) {
        return boards.filterNot { it.hasWon }
            .filter { it.isWinner(drawnNumbers) }
    }
    return null
}

fun drawNumbersUntil(predicate: Predicate<List<Board>>): Board? {
    var lastWinningBoard: Board? = null

    while (predicate.test(boards)) {
        lastWinningBoard = drawNumber()?.firstOrNull()
    }

    return lastWinningBoard
}

fun resetBingo() {
    drawnNumbers.clear()
    drawIndex = -1
    boards.forEach { it.hasWon = false }
}

private fun computeScore(winningBoard: Board): Int {
    val sum = winningBoard.values.flatten().filterNot { drawnNumbers.contains(it) }.sum()
    val number = inputNumbers[drawIndex]
    return sum * number
}

fun solveA(): Int {
    resetBingo()

    val winningBoard = drawNumbersUntil { it.any(Board::hasWon).not() }!!

    return computeScore(winningBoard)
}

fun solveB(): Int {
    resetBingo()

    val winningBoard = drawNumbersUntil { it.all(Board::hasWon).not() }!!

    return computeScore(winningBoard)
}

fun main() {
    println("Answer A: ${solveA()}") // 6592
    println("Answer B: ${solveB()}") // 31755
}
