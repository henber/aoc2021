package day3

import readInput
import transpose

val input = readInput("day3.txt")

fun solveA(): Int {
    val columns = input.map { it.toCharArray().toList() }.transpose()

    val mostCommonBits = columns
        .map {
            it.groupingBy { it }.eachCount().maxByOrNull { it.value }
        }.map { it?.key }.filterNotNull()

    val mask = Integer.parseInt("1".repeat(mostCommonBits.size), 2)
    val gamma = Integer.parseInt(String(mostCommonBits.toCharArray()), 2)
    val epsilon = gamma xor mask

    return gamma * epsilon
}

enum class FindOperation {
    MAX,
    MIN
}

fun findMostLeastCommonBit(bits: List<Char>, operation: FindOperation): Char {
    val count = bits.groupingBy { it }.eachCount()

    return when {
        operation == FindOperation.MAX && count['0'] == count['1'] -> '1'
        operation == FindOperation.MIN && count['0'] == count['1'] -> '0'
        operation == FindOperation.MAX -> count.maxByOrNull { it.value }!!.key
        operation == FindOperation.MIN -> count.minByOrNull { it.value }!!.key
        else -> throw Error("Invalid data")
    }
}

tailrec fun processColumns(operation: FindOperation, input: List<String>, index: Int = 0): List<String> {
    if (input.size == 1) {
        return input
    }

    val column = input.map { it[index] }

    val bit = findMostLeastCommonBit(column, operation)

    return processColumns(operation, input.filter { it[index] == bit }, index + 1)
}

fun solveB(): Int {
    val oxygen = Integer.parseInt(processColumns(FindOperation.MAX, input).first(), 2)
    val co2 = Integer.parseInt(processColumns(FindOperation.MIN, input).first(), 2)

    return oxygen * co2
}

fun main() {

    println("AnswerA: ${solveA()}")
    println("AnswerB: ${solveB()}")
}
