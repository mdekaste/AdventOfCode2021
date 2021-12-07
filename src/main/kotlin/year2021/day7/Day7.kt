package year2021.day7

import Challenge
import kotlin.math.abs

fun main() {
    Day7.printSolutions()
}

object Day7 : Challenge("--- Day 7: The Treachery of Whales ---") {
    val parsed = input.split(",").map(String::toInt).sorted()
    private val rangeToCheck = parsed.first()..parsed.last()

    override fun part1() = solve { it }
    override fun part2() = solve { it * (it + 1) / 2 }

    private fun solve(mapping: (Int) -> Int) = rangeToCheck.minOf { candidate ->
        parsed.map { abs(it - candidate) }.sumOf(mapping)
    }
}
