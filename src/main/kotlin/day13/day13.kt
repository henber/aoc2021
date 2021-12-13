package day13

import Pos
import listPartition
import printMatrix
import readInput

val input = readInput("day13.txt").listPartition { !it.isBlank() }
val dotInput = input[0].map { it.split(",") }.map { Pos(it[0].toInt(), it[1].toInt()) }
val foldInput = input[1].map { it.removePrefix("fold along ").split("=") }.map { Pair(it[0], it[1].toInt()) }

fun flipHorizontal(dots: List<Pos>, column: Int): List<Pos> {
    return dots.map {
        if (it.x > column) {
            Pos(column - (it.x - column), it.y)
        } else it
    }
}

fun flipVertical(dots: List<Pos>, row: Int): List<Pos> {
    return dots.map {
        if (it.y > row) {
            Pos(it.x, row - (it.y - row))
        } else it
    }
}

fun fold(axis: String, rowColumn: Int, dots: List<Pos>): List<Pos> {
    return when (axis) {
        "y" -> flipVertical(dots, rowColumn)
        "x" -> flipHorizontal(dots, rowColumn)
        else -> throw Error("Invalid axis")
    }.distinct()
}

fun solveA(): Int {
    val result = fold(foldInput[0].first, foldInput[0].second, dotInput)

    return result.size
}

fun solveB() {
    var result = dotInput
    for (instruction in foldInput) {
        result = fold(instruction.first, instruction.second, result)
    }
    val xMax = result.maxOf { it.x } + 1
    val yMax = result.maxOf { it.y } + 1
    val matrix: MutableList<MutableList<Char>> = MutableList(yMax) { MutableList(xMax) { ' ' } }
    result.forEach { matrix[it.y][it.x] = '#' }
    printMatrix(matrix) // CAFJHZCK
}

fun main(){
    println("Answer A: ${solveA()}")
    solveB()
}
