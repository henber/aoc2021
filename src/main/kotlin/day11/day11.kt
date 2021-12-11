package day11

import lazyCartesianProduct
import printMatrix
import readInput

val input = readInput("day11.txt").map { it.toCharArray().map { it.digitToInt() } }

data class Pos(val x: Int, val y: Int)

val offsets = lazyCartesianProduct(listOf(-1, 0, 1), listOf(-1, 0, 1))

fun getNeighbours(pos: Pos, maxLength: Int): List<Pos> {
    return offsets.map { Pos(pos.x + it.first, pos.y + it.second) }
        .filter { it != pos }
        .filterNot { it.x < 0 || it.y < 0 || it.x >= maxLength || it.y >= maxLength }
        .toList()
}

var flashedCount = 0

fun computeRound(input: List<List<Int>>): Pair<List<List<Int>>, Set<Pos>> {
    var incremented = input.map { it.map { it + 1 } }
    var flashed: Set<Pos> = emptySet()

    while (incremented.any { it.any { it > 9 } }) {
        val (inc, flshd) = flashOctopusses(incremented, emptySet())
        incremented = inc
        flashed = flshd
    }

    return Pair(incremented, flashed)
}

tailrec fun flashOctopusses(input: List<List<Int>>, flashed: Set<Pos>): Pair<List<List<Int>>, Set<Pos>> {
    val newFlashes = input.mapIndexed { yIndex, yList ->
        yList.mapIndexed { xIndex, x ->
            if (x > 9) {
                flashedCount += 1
                Pair(Pos(xIndex, yIndex), getNeighbours(Pos(xIndex, yIndex), yList.size))
            } else Pair(Pos(xIndex, yIndex), emptyList())
        }
    }.flatten().filter { it.second.isNotEmpty() }.filterNot { flashed.contains(it.first) }

    if (newFlashes.isEmpty()) {
        return Pair(input, flashed)
    }

    val updatedInput = input.mapIndexed { yIndex, yList ->
        yList.mapIndexed { xIndex, x ->
            val currentPos = Pos(xIndex, yIndex)
            when {
                flashed.contains(currentPos) -> 0
                newFlashes.any { it.first == currentPos } -> 0
                else -> x + newFlashes.sumOf { it.second.count { it == currentPos } }
            }
        }
    }

    return flashOctopusses(updatedInput, flashed + newFlashes.map { it.first }.toSet())
}

fun solveA(): Int {
    flashedCount = 0

    var currentState = input

    for (i in 1..100) {
        val (result, _) = computeRound(currentState)
        currentState = result
    }

    return flashedCount
}

fun solveB(): Int {
    var currentState = input
    var flashed: Set<Pos> = emptySet()
    var counter = 0

    while (flashed.size != 100) {
        val (resultState, flshd) = computeRound(currentState)
        currentState = resultState
        flashed = flshd
        counter += 1
    }

    return counter
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
