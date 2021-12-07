package day7

import readInputText
import kotlin.math.abs

val input = readInputText("day7.txt").split(",").map { it.toInt() }

val maxCrab = input.maxOf { it }

fun fuelConsumption(crab: Int, target: Int): Int = abs(crab - target)

fun solveA(): Int {
    var minValue = Int.MAX_VALUE
    for (i in 0..maxCrab) {
        val sumFuel = input.sumOf { fuelConsumption(it, i) }
        if (sumFuel < minValue) {
            minValue = sumFuel
        }
    }

    return minValue
}

fun fuelConsumption2(crab: Int, target: Int): Int {
    val rawDist = abs(crab - target)
    return rawDist * (rawDist + 1) / 2
}

fun solveB(): Int {
    var minValue = Int.MAX_VALUE
    for (i in 0..maxCrab) {
        val sumFuel = input.sumOf { fuelConsumption2(it, i) }
        if (sumFuel < minValue) {
            minValue = sumFuel
        }
    }

    return minValue
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
