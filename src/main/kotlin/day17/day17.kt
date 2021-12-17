package day17

import Pos
import readInputText

val regex = "target area: x=([0-9]+)..([0-9]+), y=(-?[0-9]+)..(-?[0-9]+)".toRegex()
val input =
    readInputText("day17.txt")
//    "target area: x=20..30, y=-10..-5"

data class Area(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int) {

    fun hit(pos: Pos): Boolean = pos.x in xMin..xMax && pos.y in yMin..yMax
}

fun runTrajectory(pos: Pos): Sequence<Pos> {
    return sequence {
        var velocity = pos
        var position = Pos(0, 0)
        while (true) {
            position += velocity
            velocity = Pos(
                when {
                    velocity.x > 0 -> velocity.x - 1
                    velocity.x == 0 -> 0
                    velocity.x < 0 -> velocity.x + 1
                    else -> error("Invalid velocity")
                }, velocity.y - 1
            )
            yield(position)
        }
    }
}

data class TrajectoryResult(val yMax: Int?, val hit: Boolean, val lastPos: Pos?, val initVelocity: Pos)

fun testTrajectory(xV: Int, yV: Int, area: Area): TrajectoryResult {
    val initVelocity = Pos(xV, yV)
    val trajectory = runTrajectory(initVelocity).takeWhile { it.x <= area.xMax && it.y >= area.yMin }
    val hit = trajectory.any { area.hit(it) }
    val yMax = trajectory.maxOfOrNull { it.y }
    return TrajectoryResult(yMax, hit, trajectory.lastOrNull(), initVelocity)
}

fun computeAllTrajectories(): List<TrajectoryResult> {
    val (xmin, xmax, ymin, ymax) = regex.matchEntire(input)!!.destructured
    val area = Area(xmin.toInt(), xmax.toInt(), ymin.toInt(), ymax.toInt())

    return (200 downTo area.yMin).map { y ->
        (-area.xMax..area.xMax).map { x ->
            testTrajectory(x, y, area)
        }
    }.flatten().filter { it.hit }.filter { it.yMax != null }
}

val trajectories = computeAllTrajectories()

fun solveA(): Int = trajectories.maxOf { it.yMax!! }

fun solveB(): Int = trajectories.size

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
