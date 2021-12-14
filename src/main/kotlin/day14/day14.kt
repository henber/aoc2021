package day14

import listPartition
import readInput

val input = readInput("day14.txt").listPartition { !it.isBlank() }

val polymerInput = input[0][0]
val mappingInput = input[1].map { it.split(" -> ") }.associate { Pair(it[0], it[1]) }

fun mapPolymer(polymer: String): String {
    val toAdd = polymer.windowed(2).map {
        if (mappingInput.containsKey(it)) mappingInput[it]!!
        else ""
    }.joinToString(separator = "") { it }
    return polymer.zip(toAdd + " ".repeat(polymer.length - toAdd.length))
        .map { "${it.first}${it.second}" }.joinToString("") { it }.trim()
}

fun solveA(): Int {
    val resultPolymer = runXSteps(10, polymerInput)

    val frequency =
        resultPolymer.groupBy { it }.map { it.value.size }.sortedByDescending { it }

    return frequency[0] - frequency[frequency.size - 1]
}

fun runXSteps(x: Int, input: String): String {
    var resultPolymer = input
    repeat(x) {
        resultPolymer = mapPolymer(resultPolymer)
    }
    return resultPolymer
}

fun solveB(): Long {
    val processedLookups = mappingInput.map { Pair(it.key, runXSteps(20, it.key)) }.toMap()
    val frequencyLookup = processedLookups.mapValues { it.value.groupBy { it }.mapValues { it.value.size } }

    val partialResult = runXSteps(20, polymerInput)

    val frequencyTotal = mutableMapOf<Char, Long>()
    partialResult.windowed(2).forEachIndexed { index, pair ->
        frequencyLookup[pair]?.forEach {
            if (frequencyTotal[it.key] != null) frequencyTotal[it.key] =
                frequencyTotal[it.key]!! + it.value
            else frequencyTotal[it.key] = it.value.toLong()
        }
        if (index > 0) {
            val extraKey = processedLookups[pair]!!.first()
            frequencyTotal[extraKey] = frequencyTotal[extraKey]!! - 1
        }
    }

    val sortedFrequencies = frequencyTotal.map { it.value }.sortedByDescending { it }
    return sortedFrequencies[0] - sortedFrequencies[sortedFrequencies.size - 1]
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}") // takes some time :(
}
