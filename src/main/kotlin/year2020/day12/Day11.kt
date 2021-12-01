package year2020.day12

import Challenge
import year2020.day12.Input.*
import kotlin.math.abs

fun main() {
    Day11.printSolutions()
}

object Day11 : Challenge() {
    val regex = """(\w)(\d+)""".toRegex()
    val parsed = input.lines()
        .mapNotNull(regex::matchEntire)
        .map(MatchResult::destructured)
        .map { (a, b) -> Input.fromInput(a[0]) to b.toInt() }

    override fun part1(): Any? {
        var xCoord = 0
        var yCoord = 0
        var heading = Heading.EAST
        fun applyInstr(instr: Input, value: Int) {
            when (instr) {
                Heading.NORTH -> yCoord += value
                Heading.SOUTH -> yCoord -= value
                Heading.EAST -> xCoord += value
                Heading.WEST -> xCoord -= value
                Turn.LEFT -> heading = heading.turn(false, value)
                Turn.RIGHT -> heading = heading.turn(true, value)
                Forward -> applyInstr(heading, value)
            }
        }
        for ((instr, value) in parsed) {
            applyInstr(instr, value)
        }
        return abs(xCoord) + abs(yCoord)
    }

    override fun part2(): Any? {
        val wayPoint = Coord(1, 10)
        val coord = Coord(0, 0)
        for ((instr, value) in parsed) {
            when (instr) {
                Heading.NORTH -> wayPoint.y + value
                Heading.EAST -> wayPoint.x + value
                Heading.SOUTH -> wayPoint.y - value
                Heading.WEST -> wayPoint.x - value
                Turn.LEFT -> turnAroundCurrentCoord(false, wayPoint, coord)
                Turn.RIGHT -> turnAroundCurrentCoord(true, wayPoint, coord)
                Forward -> coord += wayPoint
            }
        }
        return ""
    }

    class Coord(
        var y: Int,
        var x: Int
    ) {
        operator fun plusAssign(coord: Coord) {
            y += coord.y
            x += coord.x
        }
    }

    fun turnAroundCurrentCoord(turnsRight: Boolean, wayP: Coord, coord: Coord) {
    }
}
sealed interface Input {
    enum class Heading : Input {
        NORTH, EAST, SOUTH, WEST;
        fun turn(turnsRight: Boolean, turnAmount: Int): Heading {
            var turningArrayNumber = turnAmount / 90
            if (!turnsRight)
                turningArrayNumber = -turningArrayNumber
            return values()[(this.ordinal + turningArrayNumber).mod(4)]
        }
    }
    enum class Turn : Input {
        LEFT, RIGHT
    }
    object Forward : Input

    companion object {
        fun fromInput(char: Char) = when (char) {
            'N' -> Heading.NORTH
            'E' -> Heading.EAST
            'S' -> Heading.SOUTH
            'W' -> Heading.WEST
            'L' -> Turn.LEFT
            'R' -> Turn.RIGHT
            'F' -> Forward
            else -> error("")
        }
    }
}
