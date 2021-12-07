package year2015.day6

import Challenge
import kotlin.math.max

fun main() = Day6.printSolutions()

typealias IntMap = (Int) -> Int
object Day6 : Challenge() {
    val regex = """(turn on|toggle|turn off) (\d+),(\d+) through (\d+),(\d+)""".toRegex()

    data class Input(
        val instr: String,
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int
    )

    val parsed = input
        .lines()
        .mapNotNull(regex::matchEntire)
        .map(MatchResult::destructured)
        .map { (a, b, c, d, e) -> Input(a, b.toInt(), c.toInt(), d.toInt(), e.toInt()) }

    override fun part1() = solve(
        turnOn = { 1 },
        turnOf = { 0 },
        toggle = { 1 - it }
    )

    override fun part2() = solve(
        turnOn = { it + 1 },
        turnOf = { max(it - 1, 0) },
        toggle = { it + 2 }
    )

    fun solve(turnOn: IntMap, turnOf: IntMap, toggle: IntMap): Int {
        val array = IntArray(1000 * 1000)
        for ((instr, x1, y1, x2, y2) in parsed) {
            when (instr) {
                "turn on" -> mutateArray(array, x1, y1, x2, y2, turnOn)
                "turn off" -> mutateArray(array, x1, y1, x2, y2, turnOf)
                else -> mutateArray(array, x1, y1, x2, y2, toggle)
            }
        }
        return array.sum()
    }

    fun mutateArray(array: IntArray, x1: Int, y1: Int, x2: Int, y2: Int, mapping: IntMap) {
        for (y in y1..y2) {
            for (x in x1..x2) {
                val index = y * 1000 + x
                array[index] = array[index].let(mapping)
            }
        }
    }
}
