package day12

import readInput

val input = readInput("day12.txt").map { it.split("-") }
    .map { listOf(Pair(it[0], it[1]), Pair(it[1], it[0])) }.flatten()
    .groupBy { it.first }.toMap().mapValues { it.value.map { it.second } }

fun findPath(
    node: String,
    path: List<String>,
    blocked: Set<String>
): List<List<String>> {
    if (node == "end") {
        return listOf(path)
    }

    val blockList = blocked.toMutableSet()
    if (node == node.lowercase()) {
        blockList.add(node)
    }

    return input[node]!!
        .filterNot { blocked.contains(it) }
        .map {
            findPath(it, path + it, blockList)
        }.flatten()
}

fun solveA(): Int {
    val paths = findPath("start", listOf("start"), emptySet())

    return paths.size
}

data class Journey(
    val path: MutableList<String>,
    var blocked: MutableSet<String>,
    var finished: Boolean,
    var doubleVisited: Boolean
) {
    fun updateDoubleVisited() {
        doubleVisited = path.filter { blocked.contains(it) }.groupBy { it }.any { it.value.size > 1 }
    }

    fun takeStep(): List<Journey> {
        if (finished) return listOf(this)

        val currentPos = path.last()
        updateDoubleVisited()

        val newPositions = input[currentPos]!!.filterNot { it == "start" }
            .filterNot { blocked.contains(it) && doubleVisited }

        if (newPositions.isEmpty()) {
            finished = true
            return listOf(this)
        }

        return newPositions.map {
            val newJourney = this.copy(
                path = this.path.toMutableList(),
                blocked = this.blocked.toMutableSet()
            )
            newJourney.path.add(it)
            if (it == it.lowercase()) {
                newJourney.blocked.add(it)
            }
            if (it == "end") {
                newJourney.finished = true
            }
            newJourney
        }
    }
}

fun findPath2(
    journey: Journey
): List<Journey> {
    var journeys = journey.takeStep()

    while (journeys.any { !it.finished }) {
        journeys = journeys.map { it.takeStep() }.flatten()
    }

    return journeys.filter { it.path.last() == "end" }
}

fun solveB(): Int {
    val paths = findPath2(Journey(mutableListOf("start"), mutableSetOf(), false, false))

    return paths.size
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
