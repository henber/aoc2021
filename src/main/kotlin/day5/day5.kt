package day5

import readInput
import java.lang.Integer.max
import java.lang.Integer.min

data class Coord(val x: Int, val y: Int)
typealias Line = Pair<Coord, Coord>

val input = readInput("day5.txt").map {
    it.split("->")
        .map(String::trim)
        .map { it.split(",") }
        .map { Coord(it[0].toInt(), it[1].toInt()) }
}.map { Pair(it[0], it[1]) }

fun computeLine(line: Line, includeDiagonal: Boolean): List<Coord> {
    if (line.first.x == line.second.x) {
        return (min(line.first.y, line.second.y)..max(line.first.y, line.second.y))
            .map { Coord(line.first.x, it) }
    }
    if (line.first.y == line.second.y) {
        return (min(line.first.x, line.second.x)..max(line.first.x, line.second.x))
            .map { Coord(it, line.first.y) }
    }
    return if (includeDiagonal) computeDiagonal(line) else emptyList()
}

fun computeDiagonal(line: Line): List<Coord> {
    val xRange =
        if (line.first.x < line.second.x) (line.first.x..line.second.x)
        else (line.first.x downTo line.second.x)
    val yRange =
        if (line.first.y < line.second.y) (line.first.y..line.second.y)
        else (line.first.y downTo line.second.y)

    return xRange.zip(yRange).map { Coord(it.first, it.second) }
}

fun solveA(): Int {
    return input.map { computeLine(it, false) }
        .flatten()
        .groupBy { it }
        .count { it.value.size > 1 }
}

fun solveB(): Int {
    return input.map { computeLine(it, true) }
        .flatten()
        .groupBy { it }
        .count { it.value.size > 1 }
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
