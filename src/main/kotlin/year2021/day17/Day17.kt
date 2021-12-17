package year2021.day17

import Challenge
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.math.sqrt

fun main() = Day17.printSolutions()

object Day17: Challenge("--- Day 17: Trick Shot ---") {
    private val parsed = """target area: x=(-?\d+)..(-?\d+), y=(-?\d+)..(-?\d+)""".toRegex()
        .matchEntire(input)!!
        .destructured
        .let { (xMin, xMax, yMin, yMax) -> Bounds(xMin.toInt(), xMax.toInt(), yMin.toInt(), yMax.toInt()) }

    private val winningTrajectories = winningTrajectories(parsed)

    override fun part1() = winningTrajectories.maxOf { it.maxOf(Probe::yCoord) }
    override fun part2() = winningTrajectories.size

    private fun winningTrajectories(bounds: Bounds) = startingVelocities(bounds)
        .map { (y,x) -> generateSteps(y, x, bounds) }
        .filter { it.last().madeIt(bounds) }

    private fun startingVelocities(bounds: Bounds) =
        (bounds.yMin until abs(bounds.yMin)).flatMap { y ->
            (sqrt(2.0 * bounds.xMin).roundToInt()..bounds.xMax).map { x ->
                y to x
            }
        }

    private fun generateSteps(yVelocity: Int, xVelocity: Int, bounds: Bounds) = generateSequence(Probe(xVelocity, yVelocity), Probe::step)
        .takeWhile { it.canReach(bounds) }
}

data class Bounds(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int)

data class Probe constructor(
    val xVelocity: Int,
    val yVelocity: Int,
    val xCoord: Int = 0,
    val yCoord: Int = 0
) {

    fun step() = Probe(
        xVelocity - xVelocity.sign,
        yVelocity - 1,
        xCoord + xVelocity,
        yCoord + yVelocity
    )

    fun canReach(bounds: Bounds) = when{
        xCoord > bounds.xMax && xVelocity >= 0 -> false
        xCoord < bounds.xMin && xVelocity <= 0 -> false
        yCoord < bounds.yMin && yVelocity <= 0 -> false
        else -> true
    }

    fun madeIt(bounds: Bounds) = xCoord in bounds.xMin..bounds.xMax && yCoord in bounds.yMin..bounds.yMax
}