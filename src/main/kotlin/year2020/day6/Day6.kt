package year2020.day6

import Challenge

fun main() {
    Day6.printSolutions()
}

object Day6 : Challenge(
    "--- Day 6: Custom Customs ---"
) {
    val parsed = input.split("\r\n\r\n").map { answers -> answers.lines().map { answer -> answer.toSet() } }

    override fun part1(): Any? {
        return parsed.map {
            it.fold(mutableSetOf<Char>()) { acc, s ->
                acc.apply {
                    addAll(s)
                }
            }
        }.sumOf { it.count() }
    }

    override fun part2(): Any? {
        return parsed.map {
            it.map {
                it.toMutableSet()
            }.reduce { acc, mutableSet ->
                acc.apply {
                    retainAll(mutableSet)
                }
            }
        }.sumOf { it.count() }
    }
}
