package year2020.day9

import Challenge

fun main() {
    Day9.printSolutions()
}

object Day9 : Challenge("--- Day 9: Encoding Error ---") {
    val parsed = input.lines().map(String::toLong)

    override fun part1(): Any? {
        loop@for (line in 25..parsed.size) {
            val value = parsed[line]
            val preambles = parsed.subList(line - 25, line)
            for (preamble1 in preambles) {
                for (preamble2 in preambles) {
                    if (preamble1 != preamble2) {
                        if (preamble1 + preamble2 == value)
                            continue@loop
                    }
                }
            }
            return value
        }
        error("")
    }

    override fun part2(): Any? {
        val numberToFind = part1() as Long
        for (line in parsed.indices) {
            val firstNumber = parsed[line]
            var lineCounter = line + 1
            var sum = firstNumber
            while (sum < numberToFind) {
                sum += parsed[lineCounter++]
            }
            if (sum == numberToFind) {
                val sublist = parsed.subList(line, lineCounter)
                return sublist.minOf { it } + sublist.maxOf { it }
            }
        }
        error("")
    }
}
