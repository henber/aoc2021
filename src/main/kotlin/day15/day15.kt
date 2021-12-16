package day15

import Graph
import Edge
import Pos
import lazyCartesianProduct
import measureTime
import readInput

val input = readInput("day15.txt").map { it.toCharArray().map { it.digitToInt() } }

val yMax = input.size - 1
val xMax = input[0].size - 1

val offsets =
    lazyCartesianProduct(listOf(-1, 0, 1), listOf(-1, 0, 1)).map { Pos(it.first, it.second) }
        .filter { it.x == 0 || it.y == 0 }

fun validPos(pos: Pos, size: Int): Boolean {
    if (pos.x < 0 || pos.y < 0 || pos.x >= size || pos.y >= size) {
        return false
    }
    return true
}

fun solveA(): Int {

    val shortestPath = shortestPath(input, Pos(xMax, yMax))

    println(shortestPath)
    return shortestPath.last().second
}

fun shortestPath(inputMatrix: List<List<Int>>, end: Pos): List<Pair<String, Int>> {
    val graph = inputMatrix.mapIndexed { yIndex, yList ->
        inputMatrix.mapIndexed { xIndex, x ->
            offsets.map {
                val from = Pos(xIndex, yIndex)
                val to = it + from
                Pair(from, to)
            }.filter { validPos(it.second, inputMatrix.size) }.map { it to inputMatrix[it.second.y][it.second.x] }
        }
    }.flatten().flatMap { it.toList() }.toMap()
        .map { Edge(it.key.first.toString(), it.key.second.toString(), it.value) }

    with (Graph(graph, true)) {
        dijkstra(Pos(0, 0).toString())
        return getPath(end.toString())
    }
}

fun solveB(): Int {
    var inputMatrix = input
    for (i in 1..4) {
        inputMatrix = inputMatrix + input.map { it.map { if (it + i > 9) ((it + i) % 9) else it + i } }
    }

    inputMatrix = inputMatrix.mapIndexed { index, list ->
        val result = list.toMutableList()
        for (i in 1..4) {
            result += list.map { if (it + i > 9) ((it + i) % 9) else it + i }
        }
        result
    }

    val shortestPath = shortestPath(inputMatrix, Pos(inputMatrix.size-1, inputMatrix.size-1))

    println(shortestPath)

    return shortestPath.last().second
}

fun main() {
    measureTime {
        println("Answer A: ${solveA()}")
        println("Answer B: ${solveB()}")
    }
}
