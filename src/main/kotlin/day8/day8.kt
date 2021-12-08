package day8

import listPartition
import readInput

val input = readInput("day8.txt")
    .map { it.split(" ").listPartition { it != "|" } }

val uniqueNumberSet = hashSetOf(2,3,4,7)

val numbers = listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")

fun solveA(): Int {
    val outputsLengths = input.map { it[1] }.flatten().map { it.length }
    return outputsLengths.count { uniqueNumberSet.contains(it) }
}

fun <T> List<Set<T>>.union(): Set<T> {
    var result = this[0]
    this.drop(1).forEach { result = result union it }
    return result
}

fun translateOutput(output: List<String>, decodeMap: Map<Char, Char>): List<String> =
    output.map {
        it.map { decodeMap[it]!! }.joinToString("").sorted()
    }

fun decodeOutputNumbers(input: List<String>, output: List<String>): List<String> {
    val uniqueNumbers = input.filter { uniqueNumberSet.contains(it.length) }.map { it.toSet() }
    val one = uniqueNumbers.single { it.size == 2 }
    val seven = uniqueNumbers.single { it.size == 3 }
    val four = uniqueNumbers.single { it.size == 4 }
    val eight = uniqueNumbers.single { it.size == 7 }

    val inputSet = input.map { it.toSet() }

    val six = inputSet.single { it.size == 6 && !it.containsAll(uniqueNumbers.single { it.size == 2 }) }

    val decodeMap = mutableMapOf<Char, Char>()
    decodeMap['a'] = (seven - one).single()
    decodeMap['c'] = (one - six).single()
    decodeMap['f'] = (one - setOf(decodeMap['c']!!)).single()
    decodeMap['e'] = (six - inputSet.single { it.size == 5 && six.containsAll(it) }).single() // "6" - "5"
    decodeMap['b'] = (six - inputSet.filter { it.size == 5 && !six.containsAll(it) }.union()).single() // 6 - (union 2 3)
    decodeMap['d'] = (four - setOf(decodeMap['b']!!, decodeMap['c']!!, decodeMap['f']!!)).single()
    decodeMap['g'] = (eight - decodeMap.values.toSet()).single()

    return translateOutput(output, decodeMap.entries.associate{(k,v)-> v to k})
}

fun String.sorted(): String =
    this.toCharArray().sorted().joinToString(separator = "")

fun solveB(): Int {
    val result = input.map {
        decodeOutputNumbers(it[0], it[1])
            .map { numbers.indexOf(it) }
            .joinToString(separator = "") { it.toString() }
    }.map { it.toInt() }

    return result.sum()
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
