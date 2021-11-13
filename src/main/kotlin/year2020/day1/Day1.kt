package year2020.day1

import Challenge

fun main() {
    Day1.printSolutions()
}

object Day1 : Challenge(
    "--- Day 1: Report Repair ---"
) {
    val parsed = input.lines().map(String::toInt)

    override fun part1(): Any? {
        return parsed.flatMap { x1 -> parsed.map { x2 -> x1 to x2 } }
            .first{ (x1, x2) -> x1 + x2 == 2020 }
            .let { (x1, x2) -> x1 * x2 }
    }

    override fun part2(): Any? {
        return parsed.flatMap { x1 -> parsed.flatMap { x2 -> parsed.map { x3 -> Triple(x1, x2, x3) } } }
            .first { (x1, x2, x3) -> x1 + x2 + x3 == 2020 }
            .let { (x1, x2, x3) -> x1 * x2 * x3 }
    }
}
