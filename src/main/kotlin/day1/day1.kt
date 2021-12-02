package day1

import readInput

fun foldCountIncreases(list: List<Int>): Int {
    return list.fold(Pair(0, Int.MAX_VALUE)) { acc, value ->
        if (acc.second < value) {
            return@fold Pair(acc.first + 1, value)
        }
        return@fold Pair(acc.first, value)
    }.first
}

fun solveA(input: List<Int>): Int = foldCountIncreases(input)

fun solveB(input: List<Int>): Int {
    val windowSize = 3
    val windows = input.drop(windowSize - 1)
        .mapIndexed { index, i ->
            (index downTo index - (windowSize - 1)).map { input[it + windowSize - 1] }.sum()
        }

    return foldCountIncreases(windows)
}

fun main() {
    val inputNumbers = readInput("day1.txt").map { it.toInt() }

    println("answer A: ${solveA(inputNumbers)}") // 1532

    println("answer B: ${solveB(inputNumbers)}") // 1571
}
