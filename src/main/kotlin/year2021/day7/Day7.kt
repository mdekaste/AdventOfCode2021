package year2021.day7

import Challenge
import kotlin.math.absoluteValue

fun main() {
    Day7.printSolutions()
}

object Day7 : Challenge("--- Day 7: The Treachery of Whales ---") {
    val parsed = input.split(",").map(String::toInt).sorted()
    val rangeToCheck = parsed.first()..parsed.last()

    override fun part1(): Any? {
        return rangeToCheck.minOf { candidate ->
            parsed.map { it - candidate }.sumOf(Int::absoluteValue)
        }
    }

    override fun part2(): Any? {
        return rangeToCheck.minOf { candidate ->
            parsed.map { it - candidate }.map(Int::absoluteValue).sumOf { it * (it + 1) / 2 }
        }
    }
}
