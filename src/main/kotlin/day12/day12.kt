package day12

import readInput

val input = readInput("day12.txt").map { it.split("-") }
    .map { listOf(Pair(it[0], it[1]), Pair(it[1], it[0])) }.flatten()
    .groupBy { it.first }.toMap().mapValues { it.value.map { it.second } }

fun findPath(
    node: String,
    path: List<String>,
    blocked: Set<String>,
    allowDoubleVisit: Boolean
): Sequence<List<String>> =
    sequence {
        if (node == "end") {
            return@sequence yield(path)
        }

        val doubleVisited = !allowDoubleVisit ||
            path.filter { blocked.contains(it) }.groupBy { it }.any { it.value.size > 1 }

        val blockList = blocked.toMutableSet()
        if (node == node.lowercase()) {
            blockList.add(node)
        }

        input[node]!!
            .filterNot { it == "start" }
            .filterNot { blocked.contains(it) && doubleVisited }
            .forEach {
                yieldAll(findPath(it, path + it, blockList, allowDoubleVisit))
            }
    }

fun solveA(): Int {
    val paths = findPath("start", listOf("start"), emptySet(), false).toList()

    return paths.size
}

fun solveB(): Int {
    val paths = findPath("start", listOf("start"), emptySet(), true).toList()

    return paths.size
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
