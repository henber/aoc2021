package day9

import applyMask
import readInput

val input = readInput("day9.txt").map { it.toCharArray().map { it.digitToInt() } }

fun checkNeighbours(input: List<List<Int>>): List<List<Int>> {
    return input.applyMask(1) { i, list ->
        if (list.all { it > i }) {
            i + 1
        } else 0
    }
}

fun solveA(): Int {
    val risk = checkNeighbours(input).flatten().filter { it != 0 }
    return risk.sum()
}

data class Pos(val x: Int, val y: Int) {
    fun valid(max: Int): Boolean {
        return (x < 0 || y < 0 || x >= max || y >= max).not()
    }
}

val posSet = mutableSetOf<Pos>()

tailrec fun checkZone(pos: Pos, input: List<List<Int>>): Int {
    if (posSet.contains(pos) || !pos.valid(input.size)) {
        return 0
    }
    posSet.add(pos)

    if (input[pos.y][pos.x] == 9) {
        return 0
    }

    return 1 +
        checkZone(Pos(pos.x + 1, pos.y), input) +
        checkZone(Pos(pos.x - 1, pos.y), input) +
        checkZone(Pos(pos.x, pos.y + 1), input) +
        checkZone(Pos(pos.x, pos.y - 1), input)

}

fun solveB(): Long {
    val zoneSizes = input.mapIndexed { y, list ->
        list.mapIndexed { x, _ ->
            checkZone(Pos(x, y), input) }
    }.flatten().filter { it != 0 }.sortedDescending().take(3)

    return zoneSizes.fold(1L) { acc, value ->
        acc * value
    }
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
