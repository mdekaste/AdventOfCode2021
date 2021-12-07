package year2015.day5

import Challenge

fun main() = Day5.printSolutions()

object Day5 : Challenge("--- Day 5: Doesn't He Have Intern-Elves For This? ---") {
    val parsed = input.lines()

    override fun part1(): Any? {
        val niceRule1: (String) -> Boolean = { it.count("aeiou"::contains) >= 3 }
        val niceRule2: (String) -> Boolean = { it.zipWithNext().any { it.first == it.second } }
        val niceRule3: (String) -> Boolean = { it.windowed(2).none(listOf("ab", "cd", "pq", "xy")::contains) }
        return parsed.filter(niceRule1).filter(niceRule2).filter(niceRule3).count()
    }

    override fun part2(): Any? {
        val niceRule1: (String) -> Boolean = { line ->
            line.windowed(2)
                .withIndex()
                .groupBy({ it.value }, { it.index })
                .values
                .any { it.zipWithNext { a, b -> b - a }.none { it == 1 } }
        }
        val niceRule2: (String) -> Boolean = { line ->
            line.withIndex()
                .groupBy({ it.value }, { it.index })
                .values
                .any { indices -> indices.any { indices.contains(it - 2) } }
        }
        return parsed.filter(niceRule1).filter(niceRule2).count()
    }
}
