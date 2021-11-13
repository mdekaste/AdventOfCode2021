package year2020.day2

import Challenge

fun main() {
    Day2.printSolutions()
}

object Day2 : Challenge("--- Day 2: Password Philosophy ---") {
    private val regex = """(\d+)-(\d+) (\w): (\w+)""".toRegex()
    val parsed = input.lines()
        .mapNotNull(regex::matchEntire)
        .map(MatchResult::destructured)
        .map { (x1, x2, x3, x4) -> Input(x1.toInt(), x2.toInt(), x3[0], x4) }

    override fun part1(): Any? {
        return parsed.count { it.string.count { char -> char == it.char } in it.lower..it.upper }
    }

    override fun part2(): Any? {
        return parsed.count { (it.string[it.lower - 1] == it.char) xor (it.string[it.upper - 1] == it.char) }
    }
}

data class Input(
    val lower: Int,
    val upper: Int,
    val char: Char,
    val string: String
)
