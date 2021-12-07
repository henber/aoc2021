package day7

import readInputText
import kotlin.math.abs

val input = readInputText("day7.txt").split(",").map { it.toInt() }

val maxCrab = input.maxOf { it }

fun fuelConsumption(crab: Int, target: Int): Int = abs(crab - target)

fun fuelConsumption2(crab: Int, target: Int): Int {
    val rawDist = abs(crab - target)
    return rawDist * (rawDist + 1) / 2
}

fun findMinFuel(calcFuel: (Int, Int) -> Int): Int {
    var minValue = Int.MAX_VALUE
    for (i in 0..maxCrab) {
        val sumFuel = input.sumOf { calcFuel(it, i) }
        if (sumFuel < minValue) {
            minValue = sumFuel
        }
    }

    return minValue
}

fun solveA(): Int = findMinFuel(::fuelConsumption)

fun solveB(): Int = findMinFuel(::fuelConsumption2)

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
