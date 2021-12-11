package day10

import readInput

val input = readInput("day10.txt").map { it.toCharArray() }

val starters = setOf('(', '[', '{', '<')
val closers = setOf(')', ']', '}', '>')

fun parseBrackets(chars: CharArray): Pair<Char?, ArrayDeque<Char>> {
    val stack = ArrayDeque<Char>(chars.size)

    for (char in chars) {
        if (starters.contains(char)) {
            stack.addLast(char)
        } else {
            val lastStarter = stack.removeLastOrNull()
            when {
                lastStarter == null -> throw Error("Invalid data, or bug")
                starters.indexOf(lastStarter) == closers.indexOf(char) -> continue
                else -> return Pair(char, stack)
            }
        }
    }

    return Pair(null, stack)
}

val illegalScoreMapping = mapOf(Pair(')', 3), Pair(']', 57), Pair('}', 1197), Pair('>', 25137))
val autoCompleteScoreMapping = mapOf(Pair('(', 1), Pair('[', 2), Pair('{', 3), Pair('<', 4))

fun solveA(): Int {
    val score = input.mapNotNull { parseBrackets(it).first }

    return score.sumOf { illegalScoreMapping[it]!! }
}

fun solveB(): Long {
    val incompleteRowStacks = input.map { parseBrackets(it) }.filter { it.first == null }.map { it.second }

    val results = incompleteRowStacks.map { it.reversed() }

    val scores = results.map { it.map { autoCompleteScoreMapping[it]!! } }
        .map { it.fold(0L) { acc, value ->
            acc * 5 + value
        } }.sorted()

    return scores[scores.size / 2]
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
