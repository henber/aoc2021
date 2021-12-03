package day2

import readInput

val input = readInput("day2.txt").map { it.split(" ") }

typealias Command = Pair<String, Int>

typealias Coord = Pair<Int, Int>

fun commandToCoord(cmd: Command): Coord {
    return when (cmd.first) {
        "forward" -> Coord(cmd.second, 0)
        "up" -> Coord(0, -cmd.second)
        "down" -> Coord(0, cmd.second)
        else -> throw Error("Invalid command")
    }
}

fun solveA(cmds: List<Command>): Int {
    val coords = cmds.map(::commandToCoord)

    val destination = coords.fold(Coord(0, 0)) { acc, value ->
        Coord(acc.first + value.first, acc.second + value.second)
    }

    return destination.first * destination.second
}

typealias Coord3 = Triple<Int, Int, Int>

fun commandToCoord3(cmd: Command): Coord3 {
    return when (cmd.first) {
        "forward" -> Coord3(cmd.second, 0, 0)
        "up" -> Coord3(0, 0, -cmd.second)
        "down" -> Coord3(0, 0, cmd.second)
        else -> throw Error("Invalid command")
    }
}

fun computeCoord3Destination(list: List<Coord3>): Coord3 {
    return list.fold(Coord3(0,0,0)) { acc, value ->
        Coord3(acc.first + value.first, acc.second + value.first * acc.third, acc.third + value.third)
    }
}

fun solveB(cmds: List<Command>): Int {
    val coords = cmds.map(::commandToCoord3)

    val destination = computeCoord3Destination(coords)

    return destination.first * destination.second
}

fun main() {
    val commandInput = input.map { Command(it[0], it[1].toInt()) }

    println("Answer A: ${solveA(commandInput)}")
    println("Answer B: ${solveB(commandInput)}")
}
