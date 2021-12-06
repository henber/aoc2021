package day6

import readInputText

val input = readInputText("day6.txt").split(",").map { it.toInt() }

fun advanceDay(fish: List<Int>): List<Int> {
    val result = fish.map { it - 1 }
    var newFishes = result.count { it == -1 }
    return result.map { if (it == -1) 6 else it } + List(newFishes) { 8 }
}

fun solveA(): Int {
    var fishes = input
    for (i in 1..80) {
        fishes = advanceDay(fishes)
    }
    return fishes.size
}

data class FishCycle(
    var nrFishes: Long,
    var daysLeft: Int
)

fun advanceDayB(cycles: List<FishCycle>): List<FishCycle> {
    var result = cycles.map { it.apply { it.daysLeft -= 1 } }
    var newFishes = result.singleOrNull { it.daysLeft == -1 }?.nrFishes ?: 0
    val resetCycle = result.singleOrNull { it.daysLeft == 6 }
    if (resetCycle == null) {
       result += FishCycle(newFishes, 6)
    } else {
        resetCycle.nrFishes += newFishes
    }

    return result.filter { it.daysLeft >= 0 } + listOf(FishCycle(newFishes, 8))
}

fun solveB(): Long {
    var fishes = input.groupBy { it }.map { FishCycle(it.value.size.toLong(), it.key) }
    for (i in 1..256) {
        fishes = advanceDayB(fishes)
    }
    return fishes.sumOf { it.nrFishes }
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
