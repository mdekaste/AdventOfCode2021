package year2021.day6

import Challenge
import java.util.Collections.rotate

fun main() {
    Day6.printSolutions()
}

object Day6 : Challenge("--- Day 6: Lanternfish ---") {
    val parsed = input.split(",").map(String::toInt)

    override fun part1() = solve(80)

    override fun part2() = solve(256)

    fun solve(days: Int): Long = parsed
        .groupingBy { it }
        .eachCount()
        .let { MutableList(9) { index -> it[index]?.toLong() ?: 0L } }
        .apply {
            repeat(days) {
                rotate(this, -1)
                this[6] += this[8]
            }
        }
        .sum()
}
